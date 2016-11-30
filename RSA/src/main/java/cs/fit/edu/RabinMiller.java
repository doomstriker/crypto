/**
 * Rabin-Miller Algorithm test
 */
package cs.fit.edu;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

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

	

	/**
	 * Test based on Fermat's Theorem
	 * 
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
		// find integers k.q k>0 q odd n-1 = 2^k q
		do {
			flag = q.mod(b).equals(BigInteger.ZERO);

			if (flag) {
				k++;
				q = q.divide(b);
			}
		} while (flag);

		// substract 2
		BigInteger bb = n.subtract(b);
		// random integer a, 1 < a < n-1
		// BigInteger random 0 based need to add one
		BigInteger t = new BigInteger(bb.bitCount(), new Random());
		BigInteger a = t.add(BigInteger.ONE);

		if (a.modPow(q, n).equals(BigInteger.ONE)) {
			return true;
		}

		BigInteger two = new BigInteger("2");
		String strVal = "";
		for (int j = 0; j < k; j++) {
			strVal = (String) Integer.toString((int) Math.pow(2, j));
			if (a.modPow(q.multiply(new BigInteger(strVal)), n) == nMin) {
				return true;
			}
		}
		return retVal;
	}

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


	// RSAPrivateKey: private ( modulus: n, publicExponent: e, privateExponent:
	// d, prime1: p, prime2: q, exponent1: d mod (p-1), exponent2: d mod (q-1),
	// coefficient: q-1 mod p)
	// RSAPublicKey: public (modulus: n, publicExponent: e)
////create message by converting string to integer
    // String s = "test";
    // byte[] bytes = s.getBytes();
    // BigInteger message = new BigInteger(bytes);


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

		if( argMap.containsKey(PUBKEY_FILENAME_KEY)) {
			//generate key
			RSAPrivateKey pp = RSAPrivateKey.generateKey(bitLength, certainty);
			
			pp.saveKeyPair(secretKeyFileName,publicKeyFileName);
		}
		
	}

}
