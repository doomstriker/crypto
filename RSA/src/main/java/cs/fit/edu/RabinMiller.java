/**
 * Rabin-Miller Algorithm test
 */
package cs.fit.edu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author Wilson Burgos
 *
 */
public class RabinMiller {

	private static final String BIT_SIZE_KEY = "-b";
	private static final String BIT_SIZE_DEFAULT = "1024";
	private static final String PUBKEY_FILENAME_KEY = "-p";
	private static final String PUBKEY_FILENAME_DEFAULT = "public.key";
	private static final String SECKEY_FILENAME_KEY = "-s";
	private static final String SECKEY_FILENAME_DEFAULT = "secret.key";
	private static final String CERTAINTY_KEY = "-y";
	private static final String CERTAINTY_DEFAULT = "0.99999";
	private static final String CREATE_KEY = "-K";
	private static final String ENCRYPT_KEY = "-e";
	private static final String DECRYPT_KEY = "-d";
	private static final Object CIPHER_FILENAME_KEY = "-c";
	private static final String CIPHER_FILENAME_DEFAULT = "cipher";
	private static final String PLAIN_TEXT_FILENAME_DEFAULT = "plain.txt";
	private static final Object PLAIN_TEXT_FILENAME_KEY = "-m";
	private static final String SEPARATOR = ".";

	/**
	 * Prints the help screen
	 */
	public static void printHelp() {
		System.out.println("usage: RSA [options] ");
		System.out.println("\nOptions: ");
		System.out.println("\n\tcreate RSA keys ");
		System.out.println(
				"\t\t-K -p public_key_file -s secret_key_file -b bits [1024 default] -y Miller_Rabin_Certainty [99.9999]");
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

		String tempKey, tempVal;

		// gather all arguments to map
		for (int i = 0; i < args.length;) {
			tempKey = args[i];
			tempVal = args[i + 1];

			if (tempVal.contains("-")) {
				tempVal = "";
				i++;
			} else {
				i += 2;
			}
			argMap.put(tempKey, tempVal);
		}

		if (argMap.size() == 0 || argMap.size() > 5 || argMap.containsKey("h")) {
			printHelp();
			System.exit(0);
		}

		// create a BigInteger object
		BigInteger p;

		// create and assign value to bitLength
		int bitLength = Integer.parseInt(argMap.getOrDefault(BIT_SIZE_KEY, BIT_SIZE_DEFAULT));
		double certainty = Double.parseDouble(argMap.getOrDefault(CERTAINTY_KEY, CERTAINTY_DEFAULT));
		String publicKeyFileName = argMap.getOrDefault(PUBKEY_FILENAME_KEY, PUBKEY_FILENAME_DEFAULT);
		String secretKeyFileName = argMap.getOrDefault(SECKEY_FILENAME_KEY, SECKEY_FILENAME_DEFAULT);
		String cipherFileName = argMap.getOrDefault(CIPHER_FILENAME_KEY, CIPHER_FILENAME_DEFAULT);
		String plainTextFileName = argMap.getOrDefault(PLAIN_TEXT_FILENAME_KEY, PLAIN_TEXT_FILENAME_DEFAULT);

		if( argMap.containsKey(CREATE_KEY)) {
			//generate key
			RSAPrivateKey pp = RSAPrivateKey.generateKey(bitLength, certainty);
			
			pp.saveKeyPair(secretKeyFileName,publicKeyFileName);
		} else if( argMap.containsKey(ENCRYPT_KEY)) {
			//load public key
			String content;
			try {
				content = new String(Files.readAllBytes(Paths.get(publicKeyFileName)));
				RSAKey pub = new RSAKey();
				pub.unmarshall(content);
				
				//load plain text file
				content = new String(Files.readAllBytes(Paths.get(plainTextFileName)));
				//split data into blocks
				StringBuilder msg = new StringBuilder();
				int end = 0;
				int len = pub.getModulus().bitLength()/8;
				for(int i=0; i< content.length() - len; i+=len) {
					end = end > content.length() ? content.length() : end+len;
					String str = content.substring(i,end);
				    BigInteger message = new BigInteger(str.getBytes());	
				    BigInteger cipher = pub.encrypt(message);
				    
				    //avoid use BigInteger.toString(16) it doesn't add leading zeros
				    msg.append(javax.xml.bind.DatatypeConverter.printHexBinary(cipher.toByteArray()));
				}
				//open up cipher filename
				Path path = Paths.get(cipherFileName);
				
				//open up the BufferedWriter by default it auto-closes the file
				try (BufferedWriter writer = Files.newBufferedWriter(path)) {
					writer.write(msg.toString());
					System.out.println(msg);
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if( argMap.containsKey(DECRYPT_KEY)) {
			//load private key
			String content;
			try {
				//load the private key
				content = new String(Files.readAllBytes(Paths.get(secretKeyFileName)));
				RSAPrivateKey priv = new RSAPrivateKey();
				priv.unmarshall(content);
			    
			    //load cipher file
				content = new String(Files.readAllBytes(Paths.get(cipherFileName)));
				String[] tokens = content.split(" ");
				StringBuilder msg = new StringBuilder();
				
				
				int end = 0;
				int len = (priv.getModulus().bitLength()/8)*2; //times 2 because each number is a 2 character hex string
				content = content.replaceAll(" ", "");
				//decrypt block by block
				for(int i=0; i< content.length(); i+=len) {
					end = end > content.length() ? content.length() : end+len;
					String str = content.substring(i,end);
				    BigInteger message = new BigInteger(str,16);	
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
	}

}
