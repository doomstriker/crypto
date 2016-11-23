/**
 * Rabin-Miller Algorithm test
 */
package cs.fit.edu;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

/**
 * @author Wilson Burgos
 *
 */
public class RabinMiller {

	private static final String BIT_SIZE_KEY = "-b";
	private static final String BIT_SIZE_DEFAULT = "1024";
	private static final String PUBKEY_FILENAME_DEFAULT = "-p";
	private static final String PUBKEY_FILENAME_KEY = "public.key";
	private static final String SECKEY_FILENAME_DEFAULT = "-s";
	private static final String SECKEY_FILENAME_KEY = "secret.key";
	private static final String CERTAINTY_KEY = "-y";
	private static final String CERTAINTY_DEFAULT = "0.99999";
	
	
	/**
	 * Test based on Fermat's Theorem
	 * @param n
	 * @return false if composite, otherwise true
	 */
	public static boolean isPrime(BigInteger n) {
		boolean retVal = false; 
		BigInteger q = n.subtract(BigInteger.ONE);
		BigInteger nMin = n.subtract(BigInteger.ONE);
		BigInteger b = new BigInteger("2");
		int k = 0;
		boolean flag = true;
		//find integers k.q k>0 q odd n-1 = 2^k q
		do {
			flag = q.mod(b).equals(BigInteger.ZERO);
			
			if( flag ) {
				k++;
				q = q.divide(b);
			}
	    } while( flag );
		
		//substract 2
		BigInteger bb = n.subtract(b);
		//random integer a, 1 < a < n-1
		//BigInteger random 0 based need to add one
		BigInteger t = new BigInteger(bb.bitCount(),new Random());
		BigInteger a = t.add(BigInteger.ONE); 
		
		if (a.modPow(q, n).equals(BigInteger.ONE)) {
			return true;
		}
		
		BigInteger two = new BigInteger("2");
		String strVal = "";
		for (int j = 0; j < k; j++) {
			strVal = (String)Integer.toString((int)Math.pow(2, j));
			if( a.modPow(q.multiply(new BigInteger(strVal)), n) == nMin) {
				return true;  
			}
		}
		return retVal;
	}
	
	public static void printHelp() {
		System.out.println("usage: RSA [options] ");
		System.out.println("\nOptions: ");
		System.out.println("\n\tcreate RSA keys ");
		System.out.println("\t\t-K -p public_key_file -s secret_key_file -b bits [1024 default] -y Miller_Rabin_Certainty [99.9999]");
		System.out.println("\tEncrypt");
		System.out.println("\t\t-e -m plaintext_file -p public_key_file -c ciphertext_file");
		System.out.println("\tDecrypt");
		System.out.println("\t\t-d -c ciphertext_file -s secret_key_file -m plaintext_file");
		System.out.println("\n");
	}
	public static void main(String[] args) {

		HashMap<String, String> argMap = new HashMap<String, String>();
		
		String tempKey,tempVal;
		
		//gather all arguments to map
		for (int i = 0; i < args.length; ) {
			tempKey = args[i];
			tempVal = args[i+1];
			
			if( tempVal.contains("-")) {
				tempVal = "";
				i++;
			} else {
				i+=2;
			}
			argMap.put(tempKey,tempVal);
		}
		
		if( argMap.size() == 0 || argMap.size() > 5 || argMap.containsKey("h")) {
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

		// create a random object
		Random rnd = new Random();

		// assign probablePrime result to bi using bitLength and rnd
		// static method is called using class name
		p = BigInteger.probablePrime(bitLength, rnd);
//		p = new BigInteger("17");
		int c = (int)(Math.log(1-certainty)/Math.log(0.5));
		if( p.isProbablePrime(c)) {
			String str = "ProbablePrime of bitlength " + bitLength + " is p=" +p;
			// print bi value
			System.out.println( str );
		}

		BigInteger q ;
		
		do {
			q = BigInteger.probablePrime(bitLength, new Random());
		} while ( q.equals(p) );
		
//		q = new BigInteger("11");
		if( q.isProbablePrime(c)) {
			String str = "ProbablePrime of bitlength " + bitLength + " is q=" +q;
			// print bi value
			System.out.println( str );
		}
		
		BigInteger n = q.multiply(p);
		
		//compute euler totient
		//euler theorem & prime factorization
		//assumption q & p primer
		BigInteger phi = totient(p,q);
		
		//select e random
		BigInteger e;
		//make sure gcd(phi,e)==1
		do {
			e = BigInteger.probablePrime(phi.bitLength()-1,new Random());
		} while( !e.gcd(phi).equals(BigInteger.ONE));
		
		//store d
		BigInteger d = e.modInverse(phi);
		System.out.println("Public Key: e="+e+ " n="+n);
		System.out.println("Private Key: d="+d+ " p="+p + " q="+q );
//		double cert = 1 - Math.pow(4, -10);
//		for (int i = 0; i < 10; i++) {	
//			System.out.println(isPrime(p));
//		}
		
	}
	
	 public static BigInteger totient(BigInteger p,BigInteger q){ 
		 //make sure GCD = 1
		 BigInteger t = p.gcd(q);
		 if( !t.equals(BigInteger.ONE) ) {
			 throw new IllegalArgumentException("p & q GCD != 1");
		 }
		 BigInteger a = p.subtract(BigInteger.ONE);
		 BigInteger b = q.subtract(BigInteger.ONE);
		 
		 return a.multiply(b);
		 }
	
}
