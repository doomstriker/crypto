/**
 * 
 */
package cs.fit.edu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;

/**
 * @author Wilson Burgos
 *
 */
public class RSAPrivateKey extends RSAKey {

	private BigInteger privateExponent;
	private BigInteger p;
	private BigInteger q;
	BigInteger dP;
	BigInteger dQ;
	BigInteger qInv;
	
	/**
	 * Returns the private exponent value
	 * @return the private exponent value
	 */
	public BigInteger getPrivateExponent() {
		return privateExponent;
	}

	/**
	 * Sets the private exponent value 
	 * @param privateExponent the private exponent
	 */
	public void setPrivateExponent(BigInteger privateExponent) {
		this.privateExponent = privateExponent;
		if( p != null ) {
			//recalculate dp
			setP(p);
		}
		if( q != null ) {
			//recalculate dq
			setQ(q);
		}
	}

	/**
	 * Returns the prime P value
	 * @return the prime p Value
	 */
	public BigInteger getP() {
		return p;
	}

	/**
	 * Sets the prime p value
	 * @param p the prime value
	 */
	public void setP(BigInteger p) {
		if( p != null ) {
			this.p = p;
			if( privateExponent != null ) {
				setdP(privateExponent.mod(p.subtract(BigInteger.ONE)));
			}
			//update qInv if q != null
			if( q != null ) {
				setqInv(q.modInverse(p));
			}
	//		BigInteger qInv = q.modInverse(p);
		}
	}

	/**
	 * Returns the fast decryption CRT dP value
	 * @return the current value
	 */
	public BigInteger getdP() {
		return dP;
	}

	/**
	 * Sets the fast decryption CRT dP value
	 * @param dP the dp value
	 */
	public void setdP(BigInteger dP) {
		this.dP = dP;
	}

	/**
	 * Returns the fast decryption CRT dQ value
	 * @return the current value
	 */
	public BigInteger getdQ() {
		return dQ;
	}

	/**
	 * Sets the fast decryption CRT dQ value
	 * @param dQ the value
	 */
	public void setdQ(BigInteger dQ) {
		if( dQ != null ) {
			this.dQ = dQ;
		}
	}

	/**
	 * Returns the q inverse, c coefficient
	 * @return the q inverse value
	 */
	public BigInteger getqInv() {
		return qInv;
	}

	/**
	 * Stores the q inverse, c coefficient
	 * @param qInv the value to use
	 */
	public void setqInv(BigInteger qInv) {
		this.qInv = qInv;
	}

	/**
	 * Rerturns the value of the prime Q
	 * @return the q Value
	 */
	public BigInteger getQ() {
		return q;
	}

	/**
	 * Sets the value of the prime Q
	 * @param q prime value
	 */
	public void setQ(BigInteger q) {
		if( q != null ) {
			this.q = q;
			if( privateExponent != null ) {
				setdQ(privateExponent.mod(q.subtract(BigInteger.ONE)));
			}
			
			if( p != null ) {
				setqInv(q.modInverse(p));
			}
		}
	}

	/**
	 * Decrypts c using fast decryption CRT
	 * @param c cipher
	 * @return decrypted message
	 */
	public BigInteger decrypt(BigInteger c) {
      BigInteger m1 = c.modPow(dP, p);
      BigInteger m2 = c.modPow(dQ, q);
      BigInteger h = ((m1.subtract(m2)).multiply(qInv)).mod(p);
      BigInteger m = m2.add(q.multiply(h));
      return m;
	}
	/**
	 * Generate Key using Rabin Miller test certainty and bitlength
	 * @param bitLength the bitlength to use
	 * @param certainty the Rabin Miller certainty
	 * @return RSAPrivate keys
	 */
	public static RSAPrivateKey generateKey(int bitLength,double certainty) {
		// create a random object
		Random rnd = new Random();
		
		
		int c = (int) (Math.log(1 - certainty) / Math.log(0.5));

		// assign probablePrime result to bi using bitLength and rnd
		// static method is called using class name
		BigInteger p;
		int len = bitLength;
		do {
			p = BigInteger.probablePrime(len, rnd);
		} while( !p.isProbablePrime(c));
		
		BigInteger q;
		
		do {
			q = BigInteger.probablePrime(len, new Random());
		} while (q.equals(p) && !q.isProbablePrime(c));
		
		
		BigInteger n = q.multiply(p);
		
		// compute euler totient
		// euler theorem & prime factorization
		// assumption q & p primer
		BigInteger phi = totient(p, q);
		
		// select e random
		BigInteger e;
		// make sure gcd(phi,e)==1
		do {
			e = BigInteger.probablePrime(phi.bitLength() - 1, new Random());
		} while (!e.gcd(phi).equals(BigInteger.ONE));
		
		// store d
		BigInteger d = e.modInverse(phi);
			
		RSAPrivateKey pp = new RSAPrivateKey();
		pp.setExponent(e);
		pp.setModulus(n);
		pp.setP(p);
		pp.setQ(q);
		pp.setPrivateExponent(d);
		
		return pp;
	}
 	/**
	 * Generates a private & public key pair
	 * @param privFilename the filename for the private key
	 * @param pubFileName the filename for the public key
	 */
	public void saveKeyPair(String privFilename,String pubFileName) {
		savePrivateKey(privFilename);
		savePublic(pubFileName);
	}
	
	/**
	 * 
	 * @param privFilename
	 * @param pubFileName
	 */
	public void savePrivateKey(String privFilename) {
		Path path = Paths.get(privFilename);
		String msg = marshall();
		
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(msg);
			msg = msg.replace(',', '\n').replace("(", "(\n");
			System.out.println(msg);
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

	}
	@Override
	public void unmarshall(String obj) {
		//check if string exist
		if( obj != null && !obj.isEmpty()) {
			String[] tokens = obj.trim().split("[\\(\\,\\)\\:]");
			if( tokens.length == 17) {
				BigInteger m = new BigInteger(tokens[2].trim(),16);
				setModulus(m);
				BigInteger e = new BigInteger(tokens[4].trim(),16);
				setExponent(e);
				BigInteger pe = new BigInteger(tokens[6].trim(),16);
				setPrivateExponent(pe);
				BigInteger p = new BigInteger(tokens[8].trim(),16);
				setP(p);
				BigInteger q = new BigInteger(tokens[10].trim(),16);
				setQ(q);
				BigInteger dp = new BigInteger(tokens[12].trim(),16);
				setdP(dp);
				BigInteger dq = new BigInteger(tokens[14].trim(),16);
				setdQ(dq);
				BigInteger qinv = new BigInteger(tokens[16].trim(),16);
				setqInv(qinv);
			}
		}
	}

	// RSAPrivateKey: private ( modulus: n, publicExponent: e, privateExponent:
	// d, prime1: p, prime2: q, exponent1: d mod (p-1), exponent2: d mod (q-1),
	// coefficient: q-1 mod p)

	@Override
	public String marshall() {
		String nHex = modulus.toString(16);
		String eHex = exponent.toString(16);
		String dHex = privateExponent.toString(16);
		String pHex = p.toString(16);
		String qHex = q.toString(16);
		String dpHex = dP.toString(16);
		String dqHex = dQ.toString(16);
		String qInvHex = qInv.toString(16);
		StringBuilder str = new StringBuilder();
		
		str.append("private ( modulus: ");
		str.append(nHex);
		str.append(", publicExponent: ");
		str.append(eHex);
		str.append(", privateExponent: ");
		str.append(dHex);
		str.append(", prime1: ");
		str.append(pHex);
		str.append(", prime2: ");
		str.append(qHex);
		str.append(", exponent1: ");
		str.append(dpHex);
		str.append(", exponent2: ");
		str.append(dqHex);
		str.append(", coefficient: ");
		str.append(qInvHex);
		
		
		str.append(")");
		
		return str.toString();
	}
	
	@Override
	public boolean equals(Object o) {
	    // self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;
	    RSAPrivateKey key = (RSAPrivateKey) o;
	    // field comparison
	    return super.equals((RSAKey)o)
	            && Objects.equals(privateExponent, key.privateExponent)
	            && Objects.equals(p,key.p) && Objects.equals(q,key.q)
	            && Objects.equals(dP, key.dP) && Objects.equals(dQ, key.dQ)
	            && Objects.equals(qInv, key.qInv);
	} 
	
	/**
	 * Generate the Euler totient for p & q
	 * @param p the p prime to use
	 * @param q the q prime to use
	 * @return the Euler totient
	 */
	public static BigInteger totient(BigInteger p, BigInteger q) {
		// make sure GCD = 1
		BigInteger t = p.gcd(q);
		if (!t.equals(BigInteger.ONE)) {
			throw new IllegalArgumentException("p & q GCD != 1");
		}
		BigInteger a = p.subtract(BigInteger.ONE);
		BigInteger b = q.subtract(BigInteger.ONE);

		return a.multiply(b);
	}
}
