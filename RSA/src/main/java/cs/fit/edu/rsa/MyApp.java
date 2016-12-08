package cs.fit.edu.rsa;

import java.math.BigInteger;

import cs.fit.edu.rsa.asn1.Decoder;
import cs.fit.edu.rsa.asn1.Encoder;
import cs.fit.edu.rsa.asn1.RsaAsn;
import cs.fit.edu.rsa.key.RSAKey;
import cs.fit.edu.rsa.key.RSAPrivateKey;
import cs.fit.edu.rsa.output.HexadecimalOutput;
import cs.fit.edu.rsa.output.RSAFileStorage;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

@Deprecated
public class MyApp {

	public static void main(String[] args) {
		
		String RSAPublicKeyFile = "RSAPublicKey.txt";
		String RSAPrivateKeyFile = "RSAPrivateKey.txt";
		String PlainTextFile = "PlainTextMessage.txt";
		String EncryptionFile = "EncryptedMessage.txt";
		String DecryptionFileNormal = "DecryptedMessageNormal.txt";
		String DecryptionFileCRT = "DecryptedMessageCRT.txt";
		int bits = 512;
		int highbits = bits/2;
		int lowbits = highbits - 1;
		
		int certainty = 100; 
		
		//java RSA -K -p public_key_file -s secret_key_file -b bits -y Miller_Rabin_certainty
		LongOpt[] longopts = new LongOpt[1];
		longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
		 
		Getopt g = new Getopt("MyApp", args, "K:p:s:c:d:b:y:h",longopts);
		 int c;
		 String arg;
		 while ((c = g.getopt()) != -1)
		   {
		     switch(c)
		       {
		          case 'K':
			      case 'p':
			    	arg = g.getOptarg();
			    	System.out.print("You picked " + (char)c + 
	                         " with an argument of " +
	                         ((arg != null) ? arg : "null") + "\n");
			    	RSAPublicKeyFile = arg; 
			        break;
			        //
			      case 'b':
					  arg = g.getOptarg();
					    System.out.print("You picked " + (char)c + 
					                 " with an argument of " +
					                 ((arg != null) ? arg : "null") + "\n");
					 lowbits= Integer.parseInt(arg);
					 highbits = lowbits+1;
					 break;
			      case 's':
			        arg = g.getOptarg();
			        System.out.print("You picked " + (char)c + 
			                         " with an argument of " +
			                         ((arg != null) ? arg : "null") + "\n");
			        RSAPrivateKeyFile = arg; 
			        break;
			      case 'c':
				        arg = g.getOptarg();
				        System.out.print("You picked " + (char)c + 
				                         " with an argument of " +
				                         ((arg != null) ? arg : "null") + "\n");
				        EncryptionFile = arg; 
				        break;
			      case 'd':
				        arg = g.getOptarg();
				        System.out.print("You picked " + (char)c + 
				                         " with an argument of " +
				                         ((arg != null) ? arg : "null") + "\n");
				        DecryptionFileNormal = arg; 
				        break;
			      case 'y':
				        arg = g.getOptarg();
				        System.out.print("You picked " + (char)c + 
				                         " with an argument of " +
				                         ((arg != null) ? arg : "null") + "\n");
				        certainty = Integer.parseInt(arg);
				        break;
			      case 'h':
				        System.out.print("-K -p public_key_file -s secret_key_file -b bits -y Miller_Rabin_certainty");
				        System.out.print("-e -m plaintext_file -p public_key_file -c ciphertext_file");
				        System.out.print("-d -c ciphertext_file -s secret_key_file -m plaintext_file");
				        return;
				        
			        //
			      case '?':
			        break; // getopt() already printed an error
			        //
			      default:
			    	  System.out.print("getopt() returned " + c + "\n");
		       }
		   }


		
		
		
		RSACryptosystem rsa = new RSACryptosystem();
		
		BigInteger p = rsa.GeneratePrimeByBitSize(lowbits, highbits, certainty);
		BigInteger q = rsa.GeneratePrimeByBitSize(lowbits, highbits, certainty);
		
		BigInteger n = p.multiply(q);
		
		BigInteger phi = rsa.ComputeEulerTotient(p, q);
		BigInteger Message = new BigInteger("1978");
		
		
		BigInteger TWO = new BigInteger("2");
		BigInteger max = TWO.pow(50);
		BigInteger min = TWO.pow(51);
		//Message=rsa.randomNumber(rand,min , max);
		
		RSAFileStorage store = new RSAFileStorage();
		
		String msg = store.ReadStringFromFile(PlainTextFile);
		
		msg = msg.substring(0, msg.indexOf("\r\n"));
		BigInteger m = new BigInteger(msg);
		Message = m;
		
		System.out.println("Message: "+Message);
		BigInteger eNext = rsa.FindE(certainty, phi);
		System.out.println("e: "+eNext);
		BigInteger d = rsa.FindD(eNext, phi);
		System.out.println("d: "+d);
		
		BigInteger CipherText = rsa.ComputeCipherText(Message, eNext, n);
		System.out.println("CipherText: " + CipherText);
		
		store.WriteToFile(CipherText, EncryptionFile);
		
		BigInteger exponent1 = d.mod(p.subtract(BigInteger.ONE));
		BigInteger exponent2 = d.mod(q.subtract(BigInteger.ONE));
		BigInteger coefficient = rsa.FindD(q, p);
		
		RSAPrivateKey rsaPrivateKey = new RSAPrivateKey();
		rsaPrivateKey.setqInv(coefficient);
		rsaPrivateKey.setdP(exponent1);
		rsaPrivateKey.setdQ(exponent2);
		rsaPrivateKey.setModulus(n);
		rsaPrivateKey.setP(p);
		rsaPrivateKey.setQ(q);
		rsaPrivateKey.setExponent(eNext);
		rsaPrivateKey.setPrivateExponent(d);
//		rsaPrivateKey.SetCoefficient(coefficient);
		
		RSAKey rsaPublicKey = new RSAKey();
		rsaPublicKey.setModulus(n);
		rsaPublicKey.setExponent(eNext);
		
		RsaAsn encodingLib = new RsaAsn();
		
		Encoder privateKeyEncoder = encodingLib.EncodePrivateKey(rsaPrivateKey);
		Encoder publicKeyEncoder = encodingLib.EncodePublicKey(rsaPublicKey);
		
		
	
		store.WriteToFile(publicKeyEncoder, RSAPublicKeyFile);
		store.WriteToFile(privateKeyEncoder, RSAPrivateKeyFile);
		
		HexadecimalOutput output = new HexadecimalOutput();
		output.LogEncoderToConsoleAsHexadecimal(publicKeyEncoder, "Public Key ");
		output.LogEncoderToConsoleAsHexadecimal(privateKeyEncoder, "Private Key ");
		
		byte[] privateKeyData = store.ReadFromFile(RSAPrivateKeyFile);
		byte[] publicKeyData = store.ReadFromFile(RSAPublicKeyFile);
		
		Decoder publicKeyDecoder = encodingLib.DecodeKey(publicKeyData);
		Decoder privateKeyDecoder = encodingLib.DecodeKey(privateKeyData);
				
		System.out.println(privateKeyDecoder);
		System.err.println("N-Real=" + n + " n: "+privateKeyDecoder.getFirstObject(true).getInteger());
		System.err.println("E-Real=" + eNext + " e: "+privateKeyDecoder.getFirstObject(true).getInteger());
		System.err.println("D-Real=" + d + " d: "+privateKeyDecoder.getFirstObject(true).getInteger());
		System.err.println("P-Real=" + p + " p: "+privateKeyDecoder.getFirstObject(true).getInteger());
		System.err.println("Q-Real=" + q + " q: "+privateKeyDecoder.getFirstObject(true).getInteger());
		System.err.println("Exp1-Real=" + exponent1 + " exponent1: "+privateKeyDecoder.getFirstObject(true).getInteger());
		System.err.println("Exp2-Real=" + exponent2 + " exponent2: "+privateKeyDecoder.getFirstObject(true).getInteger());
		System.err.println("Coeff-Real=" + coefficient + " coefficient: "+privateKeyDecoder.getFirstObject(true).getInteger());
		System.out.println(privateKeyDecoder);
		
		System.out.println(publicKeyDecoder);
		System.err.println("N-Real=" + n + " n: "+publicKeyDecoder.getFirstObject(true).getInteger());
		System.err.println("E-Real=" + eNext + " e: "+publicKeyDecoder.getFirstObject(true).getInteger());
		
		System.out.println(publicKeyDecoder);
		
		long startTime,estimatedTime;
		
		String cipherFromFile = store.ReadStringFromFile(EncryptionFile);
		cipherFromFile=cipherFromFile.replace("\r","").replace("\n","");
		BigInteger cipherBigInt = new BigInteger(cipherFromFile);
		
		startTime = System.nanoTime();    
		BigInteger PlainText = rsa.RecoverPlainText(cipherBigInt, d, n);
		System.out.println("PlainText: " + PlainText);
		estimatedTime = System.nanoTime() - startTime;
		System.out.println("Time: " + estimatedTime);
		store.WriteToFile(PlainText, DecryptionFileNormal);
		
		startTime = System.nanoTime();    
		rsa.PrepareRecoveryCRT(d, n, p, q);
		BigInteger PlainText2 = rsa.RecoverPlainTextCRT(cipherBigInt);
		System.out.println("PlainText2: " + PlainText2);
		// ... the code being measured ...    
		estimatedTime = System.nanoTime() - startTime;
		System.out.println("Time: " + estimatedTime);
		store.WriteToFile(PlainText, DecryptionFileCRT);
}
	
}
