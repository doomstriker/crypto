package cs.fit.edu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;

public class RSAKey implements Marshall<String>, Unmarshall<String>{
	protected BigInteger modulus;
	protected BigInteger exponent;
	protected boolean pkcsEnabled;
	
	/**
	 * Returns true if PKCS is enabled or not
	 * @return true if pkcs enabled, false otherwise
	 */
	public boolean isPkcsEnabled() {
		return pkcsEnabled;
	}

	/**
	 * Enables or disables pkcs encryption
	 * @param pkcsEnabled true to enable pkcs encryption, false otherwise
	 */
	public void setPkcsEnabled(boolean pkcsEnabled) {
		this.pkcsEnabled = pkcsEnabled;
	}

	/**
	 * Returns the modulus value p*q
	 * @return the modulues value p*q
	 */
	public BigInteger getModulus() {
		return modulus;
	}
	
	/**
	 * Sets the value p*q
	 * @param modulus the value p*q
	 */
	public void setModulus(BigInteger modulus) {
		this.modulus = modulus;
	}
	
	/**
	 * Returns the exponent encryption key value
	 * @return the encrytion key
	 */
	public BigInteger getExponent() {
		return exponent;
	}
	
	/**
	 * Sets the exponent, encryption key value
	 * @param exponent the encryption key value
	 */
	public void setExponent(BigInteger exponent) {
		this.exponent = exponent;
	}

	/**
	 * Unmarshalls the content of the string updating this object internals
	 */
	@Override
	public void unmarshall(String obj) {
		//check if string exist
		if( obj != null && !obj.isEmpty()) {
			String[] tokens = obj.trim().split("[\\(\\,\\)\\:]");
			if( tokens.length == 5) {
				BigInteger m = new BigInteger(tokens[2].trim(),16);
				setModulus(m);
				BigInteger e = new BigInteger(tokens[4].trim(),16);
				setExponent(e);
			}
		}
	}

	/**
	 * Internal representation
	 * @return
	 */
	private Object publicKey() {
		String nHex = modulus.toString(16);
		String eHex = exponent.toString(16);
		StringBuilder str = new StringBuilder();
		
		str.append("public ( modulus: ");
		str.append(nHex);
		str.append(", publicExponent: ");
		str.append(eHex);
		str.append(")");
		return str.toString();

	}
	/**
	 * Marshals the content of this object to a String
	 */
	@Override
	public String marshall() {
		return (String) publicKey();		
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
	    RSAKey key = (RSAKey) o;
	    // field comparison
	    return Objects.equals(modulus, key.modulus)
	            && Objects.equals(exponent, key.exponent);
	}
	
	/**
	 * Generates public key
	 * @param pubFilename the filename to store the key to
	 */
	public void savePublic(String pubFileName) {
		String msg = (String) publicKey();
		Path path = Paths.get(pubFileName);
		
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(msg);
			msg = msg.replace(',', '\n').replace("(", "(\n");
			System.out.println(msg);
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
	}
	
	/**
	 * Returns a string representation of this object. 
	 */
	  @Override
	public String toString() {
		return marshall();
	}

	  /**
	   * Encrypts the content of the value m, using the internal key
	   * @param m the value to encrypt
	   * @return the 
	   */
	public BigInteger encrypt(BigInteger m) {
		if ( m == null) {
			throw new IllegalArgumentException("Invalid Message");
		}
		
		if( m.bitLength() > modulus.bitLength()) {
			throw new IllegalArgumentException("Message too long");
		}
		BigInteger preProc = preEncryption(m);

		return preProc.modPow(exponent, modulus);
	 }

	  /**
	   * Encrypts the content of the value m, using the internal key
	   * @param m the value to encrypt
	   * @return the 
	   */
	public BigInteger encrypt(byte[] m) {
		return encrypt(new BigInteger(1,m));
	 }
	
	protected BigInteger preEncryption(BigInteger m) {
		
		if( !isPkcsEnabled()) {
			return (m);
		} 
		
		if (modulus == null) {
			throw new IllegalArgumentException("Modulus not set");
		}
		int k = (int) Math.round(modulus.bitLength()/8.0);
		BigInteger cipher;
		byte[] mData = m.toByteArray();
		int mLen = mData.length;
		
		if (mLen > k - 11) {
			throw new IllegalArgumentException("Message too long");
		}

		int psLength = k - mLen -3;;
		byte[] pad = new byte[k];
		int i=0;
		pad[i++] = 0;
		pad[i++] = 2;
	
		Random rnd = new Random();
		//generate non zero bytes
		while(psLength > 0) {
			int j=0;
			boolean zero=false;
			do {
				j = rnd.nextInt();
				zero = (( (j & 0xff) == 0) || (((j >>8) & 0xff) == 0) || (((j >> 16) & 0xff) == 0) || (((j >> 24) & 0xff) == 0));
			} while(zero);
			byte b ;
			if( psLength > 0) {
				pad[i++] = (byte) (j & 0xff);
				psLength--;
				if( psLength > 0) {
					pad[i++] = (byte) ((j >> 8) & 0xff);
					psLength--;
					if( psLength > 0) {
						pad[i++] = (byte) ((j>> 16) & 0xff);
						psLength--;
						if( psLength > 0) {
							pad[i++] = (byte) ((j >> 24) & 0xff);
							psLength--;
						}
					}
				}
			}
		}
		
		pad[i++]=0;
		
		
		for( byte tmp : mData) {
			pad[i++] = tmp;
		}
		cipher = new BigInteger(pad);
		return cipher;
	}
}
