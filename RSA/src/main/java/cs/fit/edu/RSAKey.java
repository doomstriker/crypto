package cs.fit.edu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RSAKey implements Marshall<String>, Unmarshall<String>{
	private BigInteger modulus;
	private BigInteger exponent;
	
	public BigInteger getModulus() {
		return modulus;
	}
	
	public void setModulus(BigInteger modulus) {
		this.modulus = modulus;
	}
	
	public BigInteger getExponent() {
		return exponent;
	}
	
	public void setExponent(BigInteger exponent) {
		this.exponent = exponent;
	}

	@Override
	public void unmarshall(String obj) {
		//check if string exist
		if( obj != null && !obj.isEmpty()) {
			String[] tokens = obj.trim().split("[\\(\\,\\)\\:]");
			if( tokens.length == 5) {
				BigInteger m = new BigInteger(tokens[2],16);
				setModulus(m);
				BigInteger e = new BigInteger(tokens[4],16);
				setExponent(e);
			}
		}
	}

	@Override
	public String marshall() {
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
	
	
}
