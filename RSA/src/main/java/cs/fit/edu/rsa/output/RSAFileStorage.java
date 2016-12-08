package cs.fit.edu.rsa.output;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import cs.fit.edu.rsa.asn1.Encoder;


public class RSAFileStorage {
	//Store the keys in two files. For a bonus of 10%, use the PKCS #1 ASN1, RFC3447, encoding for files:
	//RSAPrivateKey: private ( modulus: n, publicExponent: e, privateExponent: d, prime1: p, prime2: q, exponent1: d mod (p-1), exponent2: d mod (q-1), coefficient: q-1 mod p)
	//RSAPublicKey: public (modulus: n, publicExponent: e)
	
	public boolean WriteToFile(Encoder enc, String filepath)
	{
		boolean result = false;
		
		byte data[] = enc.getBytes();
		FileOutputStream out;
		try {
			out = new FileOutputStream(filepath);
			try {
				out.write(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean WriteToFile(BigInteger enc, String filepath)
	{
		boolean result = false;
		
		
		
			try {
				try(  PrintWriter next = new PrintWriter( filepath )  ){
					next.println( enc.toString() );
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
		return result;
	}
	
	public byte[] ReadFromFile(String filepath)
	{
		boolean result = false;
		byte[] data = {0};
		Path path = Paths.get(filepath);
		try {
			data = Files.readAllBytes(path);			
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}

	public String ReadStringFromFile(String path)
	{
		BufferedReader br;
		String everything = "";
		try {
			br = new BufferedReader(new FileReader(path));
		    StringBuilder sb = new StringBuilder();
		    String line = "";
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        try {
					line = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    everything = sb.toString();
		    br.close();
		    
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return everything;
		
	}
}
