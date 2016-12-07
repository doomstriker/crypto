package cs.fit.edu;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

public class RSAKeyTest {

	@Test
	public final void testSetModulus() {
		BigInteger test  = BigInteger.probablePrime(10, new Random());
		
		RSAKey r = new RSAKey();
		r.setModulus(test);
		
		assertEquals("Failing setModulus ", test,r.getModulus());
		
		test = BigInteger.probablePrime(64, new Random());
		r.setModulus(test);
		assertEquals("Failing setModulus 64bit ", test,r.getModulus());

		test = BigInteger.probablePrime(1024, new Random());
		r.setModulus(test);
		assertEquals("Failing setModulus 1024bit ", test,r.getModulus());

		test = BigInteger.probablePrime(2048, new Random());
		r.setModulus(test);
		assertEquals("Failing setModulus 2048bit ", test,r.getModulus());
	}

	@Test
	public final void testSetExponent() {
		BigInteger test  =BigInteger.probablePrime(10, new Random());
		
		RSAKey r = new RSAKey();
		r.setExponent(test);
		
		assertEquals("Failing setModulus ", test,r.getExponent());
		
		test = BigInteger.probablePrime(64, new Random());
		r.setExponent(test);
		assertEquals("Failing setModulus 64bit ", test,r.getExponent());

		test = BigInteger.probablePrime(1024, new Random());
		r.setExponent(test);
		assertEquals("Failing setModulus 1024bit ", test,r.getExponent());

		test = BigInteger.probablePrime(2048, new Random());
		r.setExponent(test);
		assertEquals("Failing setModulus 2048bit ", test,r.getExponent());
	}

	@Test
	public final void testUnmarshall() {
	
		BigInteger exp = BigInteger.probablePrime(64, new Random());
		BigInteger n = BigInteger.probablePrime(64, new Random());

		RSAKey rr = new RSAKey();
		
		rr.setExponent(exp);
		rr.setModulus(n);
		
		RSAKey zz = new RSAKey();
		zz.unmarshall(rr.marshall());
		assertEquals("Failed unmarshall",zz,rr );
	}

	@Test
	public final void testMarshall() {
		RSAKey pk = new RSAKey();
		pk.setExponent(new BigInteger("7"));
	    pk.setModulus(new BigInteger("187"));
	   
	    String str = pk.marshall();
	    assertEquals("RSA Key not marshalling correctly","public ( modulus: bb, publicExponent: 7)",str);
	   
	    pk.setExponent(new BigInteger("129"));
	    pk.setModulus(new BigInteger("357"));
	   
	    str = pk.marshall();
	    assertEquals("RSA Key not marshalling correctly","public ( modulus: 165, publicExponent: 81)",str);
	
	}

	@Test
	public final void testRSAEncrypt() {
		// using slide examples
		RSAKey pk = new RSAKey();
		byte[] bytes = new byte[] { (byte)88 };
	    //BigInteger msg = new BigInteger(bytes);

		boolean exception =false;
//		pk.setP(new BigInteger("17"));
//		pk.setQ(new BigInteger("11"));
		pk.setExponent(new BigInteger("7"));
		BigInteger cipher;
		
		try {
			//should fail modulus not set
			cipher = pk.encrypt(bytes);
		} catch(Exception e) {
			exception = true;
		}
	    assertTrue("Encryption method not throwing exception", exception);
	    
	    //update modulus
	    pk.setModulus(new BigInteger("187"));
	    cipher = pk.encrypt(bytes);
	    assertSame("Encryption failed", 11, cipher.intValue());
	    
		pk.setPkcsEnabled(true);
		
		try {
			//ecnryption should fail
			cipher = pk.encrypt(bytes);
		} catch( Exception ex) {
			exception = true;
		}
		
	    assertTrue("PKCS Encryption failed, not throwing exception",exception);
	    
	    
	    pk = RSAPrivateKey.generateKey(128, 99.999);
	    pk.setPkcsEnabled(true);
		String content ="W";
		bytes = content.getBytes();
		exception = false;
		try {
		// at this point just testing encryption not throwing exception
	    cipher = pk.encrypt(bytes);
		} catch(Exception ex) {
			exception = true;
		}
		
		assertFalse("PKCS Encryption failing",exception);
		
	    //repeat with longer string
	    content ="sdkfjlklsdfjklasdjfklsjdklfjskdfjklsjdklfajlskdjf;klasjdkfljaslkdfjla;ksdjfl;asjd;fklasjdklfjasdklfjas;kldfjksdfjskdljd";
		bytes = content.getBytes();
	    //BigInteger msg = new BigInteger(bytes);
		exception = false;
	    try {
	    	//should fail message longer than key
	    	cipher = pk.encrypt(bytes);
	    } catch(Exception ex) {
	    	exception = true;
	    }
	 
	    assertTrue( "Failed encryption long string 64bit",exception);
	}

}
