/**
 * Rabin-Miller Algorithm test
 */
package cs.fit.edu.rsa;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import cs.fit.edu.rsa.key.RSAKey;
import cs.fit.edu.rsa.key.RSAPrivateKey;

/**
 * @author Wilson Burgos
 *
 */
public class RSA {

	private static final String BIT_SIZE_KEY = "-b";
	private static final String BIT_SIZE_DEFAULT = "1024";
	private static final String PUBKEY_FILENAME_KEY = "-p";
	private static final String PUBKEY_FILENAME_DEFAULT = "public.key";
	private static final String SECKEY_FILENAME_KEY = "-s";
	private static final String SECKEY_FILENAME_DEFAULT = "sec.key";
	private static final String ASN1_KEY = "-A";
	private static final String ASN1_DEFAULT = "false";
	private static final String CERTAINTY_KEY = "-y";
	private static final String CERTAINTY_DEFAULT = "0.99999";
	private static final String HELP_KEY = "-h";
	private static final String CREATE_KEY = "-K";
	private static final String ENCRYPT_KEY = "-e";
	private static final String DECRYPT_KEY = "-d";
	private static final Object CIPHER_FILENAME_KEY = "-c";
	private static final String CIPHER_FILENAME_DEFAULT = "cipher";
	private static final String PLAIN_TEXT_FILENAME_DEFAULT = "plain.txt";
	private static final Object PLAIN_TEXT_FILENAME_KEY = "-m";
	private static final String SEPARATOR = " ";

	/**
	 * Prints the help screen
	 */
	public static void printHelp() {
		System.out.println("usage: RSA [options] ");
		System.out.println("\nOptions: ");
		System.out.println("\n\tcreate RSA keys ");
		System.out.println(
				"\t\t-K -p public_key_file -s secret_key_file -b bits [1024 default] -y Miller_Rabin_Certainty [99.9999]");
		System.out.println(
				"\t\tto encode key pair using ASN1 use -A attribute ");
		System.out.println("\tEncrypt");
		System.out.println("\t\t-e -m plaintext_file -p public_key_file -c ciphertext_file");
		System.out.println("\tDecrypt");
		System.out.println("\t\t-d -c ciphertext_file -s secret_key_file -m plaintext_file");
		System.out.println("\n");
	}

	/**
	 * Main 
	 * @param args
	 */
	public static void main(String[] args) {

		HashMap<String, String> argMap = new HashMap<String, String>();

		String tempKey="", tempVal="";

		// gather all arguments to map
		for (int i = 0; i < args.length;) {
			tempKey = args[i];
			tempVal="";
			if( i+1 < args.length )  {
				tempVal = args[i + 1];
			}
			if (tempVal.contains("-")) {
				tempVal = "";
				i++;
			} else {
				i += 2;
			}
			argMap.put(tempKey, tempVal);
		}

		if (argMap.size() == 0 || argMap.size() > 6 || argMap.containsKey(HELP_KEY)) {
			printHelp();
			System.exit(0);
		}

		// create a BigInteger object
		BigInteger p;

		// create and assign value to bitLength
		boolean asn1 = argMap.containsKey(ASN1_KEY);
		int bitLength = Integer.parseInt(argMap.getOrDefault(BIT_SIZE_KEY, BIT_SIZE_DEFAULT));
		double certainty = Double.parseDouble(argMap.getOrDefault(CERTAINTY_KEY, CERTAINTY_DEFAULT));
		String publicKeyFileName = argMap.getOrDefault(PUBKEY_FILENAME_KEY, PUBKEY_FILENAME_DEFAULT);
		String secretKeyFileName = argMap.getOrDefault(SECKEY_FILENAME_KEY, SECKEY_FILENAME_DEFAULT);
		String cipherFileName = argMap.getOrDefault(CIPHER_FILENAME_KEY, CIPHER_FILENAME_DEFAULT);
		String plainTextFileName = argMap.getOrDefault(PLAIN_TEXT_FILENAME_KEY, PLAIN_TEXT_FILENAME_DEFAULT);

		if( argMap.containsKey(CREATE_KEY)) {
			//generate key
			RSAPrivateKey pp = RSAPrivateKey.generateKey(bitLength, certainty);
			
			pp.saveKeyPair(secretKeyFileName,publicKeyFileName,asn1);
		} else if( argMap.containsKey(ENCRYPT_KEY)) {
			if( asn1 ) {
				System.out.println("Feature not yet supported!");
				System.exit(0);
			}
			//load public key
			String content;
			try {
				content = new String(Files.readAllBytes(Paths.get(publicKeyFileName)));
				RSAKey pub = new RSAKey();
				pub.unmarshall(content);
				
				//load plain text file
				byte[] data = Files.readAllBytes(Paths.get(plainTextFileName));
				//split data into blocks
				StringBuilder msg = new StringBuilder();
				int end = 0;
				int len = ((int)Math.round(pub.getModulus().bitLength()/8.0));
				byte[] m = new byte[len];
				
				System.out.println("Encrypted data");
				int j =0;
				for(int i=0; i< data.length - len; i+=len) {
					end = len+i > data.length ? data.length-len : len;
					System.arraycopy(data, i, m, 0, end);
				    BigInteger cipher = pub.encrypt(m);
				    
				    //BigInteger.toString(16) it doesn't add leading zeros
				    //need to add separator
				    String t = cipher.toString(16);
				    msg.append(t);
				    msg.append(SEPARATOR);
				    System.out.println("block"+ j++ +" length="+end);
					System.out.println(t);
				}
				//open up cipher filename
				Path path = Paths.get(cipherFileName);
				
				//open up the BufferedWriter by default it auto-closes the file
				try (BufferedWriter writer = Files.newBufferedWriter(path)) {
					writer.write(msg.toString());
				} catch (IOException e3) {
					e3.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else if( argMap.containsKey(DECRYPT_KEY)) {
			if( asn1 ) {
				System.out.println("Feature not yet supported!");
				System.exit(0);
			}
			//load private key
			String content;
			try {
				//load the private key
				content = new String(Files.readAllBytes(Paths.get(secretKeyFileName)));
				RSAPrivateKey priv = new RSAPrivateKey();
				priv.unmarshall(content);
			    
			    //load cipher file
				content = new String(Files.readAllBytes(Paths.get(cipherFileName)));
				String[] tokens = content.split(SEPARATOR);
				StringBuilder msg = new StringBuilder();
								
//				int end = 0;
//				int len = ((int)Math.round(priv.getModulus().bitLength()/8.0))*2; //times 2 because each number is a 2 character hex string
//				//content = content.replaceAll(" ", "");
//				//decrypt block by block
//				for(int i=0; i< content.length()-len; i+=len) {
//					end = end > content.length() ? content.length() : end+len;
//					System.out.println("data block="+(end-i));
//					String str = content.substring(i,end);
//				    BigInteger message = new BigInteger(str,16);	
//				    BigInteger cipher = priv.decrypt(message);
//				    byte[] d = cipher.toByteArray();
//				    
//				    if( d.length != len/2) {
//				    	int z = 0;
//				    	z++;
//				    }
//  			    	msg.append(new String(d));
//				}
				
				//decrypt block by block
				for(String block : tokens) {
					BigInteger message = new BigInteger(block,16);	
				    BigInteger cipher = priv.decrypt(message);
				     msg.append(new String(cipher.toByteArray()));
				}
			    
				//open up plain text file
			    Path path = Paths.get(plainTextFileName);
				
				//open up the BufferedWriter by default it auto-closes the file
				try (BufferedWriter writer = Files.newBufferedWriter(path)) {
					writer.write(msg.toString());
					System.out.println(msg);
					writer.close();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
	}

}
