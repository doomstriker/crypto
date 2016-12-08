/**
 * 
 */
package cs.fit.edu;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import cs.fit.edu.rsa.key.RSAPrivateKey;

/**
 * @author J28243
 *
 */
public class RSAPrivateKeyTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link cs.fit.edu.rsa.key.RSAPrivateKey#unmarshall(java.lang.String)}.
	 */
	@Test
	public final void testUnmarshall() {
		BigInteger exp = BigInteger.probablePrime(64, new Random());
		BigInteger n = BigInteger.probablePrime(64, new Random());

		RSAPrivateKey rr = new RSAPrivateKey();
		
		rr.setExponent(exp);
		rr.setModulus(n);
		rr.setP( BigInteger.probablePrime(64, new Random()));
		rr.setQ( BigInteger.probablePrime(64, new Random()));
		rr.setPrivateExponent( BigInteger.probablePrime(64, new Random()));
		RSAPrivateKey zz = new RSAPrivateKey();
		zz.unmarshall(rr.marshall());
		assertEquals("Failed unmarshall",zz,rr );
	}

	/**
	 * Test method for {@link cs.fit.edu.rsa.key.RSAPrivateKey#decrypt(java.math.BigInteger)}.
	 */
	@Test
	public final void testDecrypt() {
		// using slide examples
		RSAPrivateKey pk = new RSAPrivateKey();
		byte[] bytes = new byte[] { (byte)88 };
	    //BigInteger msg = new BigInteger(bytes);

		boolean exception =false;
		pk.setP(new BigInteger("17"));
		pk.setQ(new BigInteger("11"));
		pk.setExponent(new BigInteger("7"));
		pk.setPrivateExponent(new BigInteger("23"));
		BigInteger cipher;
	   
	    //update modulus
	    pk.setModulus(pk.getP().multiply(pk.getQ()));
	    cipher = pk.encrypt(bytes);		
	    BigInteger msg2 = pk.decrypt(cipher);
	    
	    assertEquals( "Failed decrypting single byte",msg2.byteValue(),bytes[0]);
	    
	    
		pk = RSAPrivateKey.generateKey(64, 99.999);
		String content ="Hello!!";
		bytes = content.getBytes();
	    //BigInteger msg = new BigInteger(bytes);
	    
	    cipher = pk.encrypt(bytes);
	    msg2 = pk.decrypt(cipher);
	    
	    String tmp = new String(msg2.toByteArray());
	    
	    pk = RSAPrivateKey.generateKey(1024, 99.999);
		content ="Hello World!!";
		bytes = content.getBytes();
	    //BigInteger msg = new BigInteger(bytes);
	    
	    cipher = pk.encrypt(bytes);
	    msg2 = pk.decrypt(cipher);
	    
	    tmp = new String(msg2.toByteArray()); 
	    assertEquals( "Failed decryption 1024bit",content,tmp);
	    
	    //repeat with longer string
	    content ="sdkfjlklsdfjklasdjfklsjdklfjskdfjklsjdklfajlskdjf;klasjdkfljaslkdfjla;ksdjfl;asjd;fklasjdklfjasdklfjas;kldfjksdfjskdljd";
		bytes = content.getBytes();
	    //BigInteger msg = new BigInteger(bytes);
	    
	    cipher = pk.encrypt(bytes);
	    msg2 = pk.decrypt(cipher);
	    
	    tmp = new String(msg2.toByteArray());
	    assertEquals( "Failed decryption long string 1024bit",content,tmp);
	    
	    
	    pk = RSAPrivateKey.generateKey(2048, 99.999);
		content ="Hello World!!";
		bytes = content.getBytes();
	    //BigInteger msg = new BigInteger(bytes);
	    
	    cipher = pk.encrypt(bytes);
	    msg2 = pk.decrypt(cipher);
	    
	    tmp = new String(msg2.toByteArray()); 
	    assertEquals( "Failed decryption 2048bit",content,tmp);
	    
	    //repeat with longer string
	    content ="sdkfjlklsdfjklasdjfklsjdklfjskdfjklsjdklfajlskdjf;klasjdkfljaslkdfjla;ksdjfl;asjd;fklasjdklfjasdklfjas;kldfjksdfjskdljd";
		bytes = content.getBytes();
	    //BigInteger msg = new BigInteger(bytes);
	    
	    cipher = pk.encrypt(bytes);
	    msg2 = pk.decrypt(cipher);
	    
	    tmp = new String(msg2.toByteArray());
	    assertEquals( "Failed decryption long string 2048bit",content,tmp);
	    
	    pk = RSAPrivateKey.generateKey(4096, 99.999);
		content ="Hello World!!";
		bytes = content.getBytes();
	    //BigInteger msg = new BigInteger(bytes);
	    
	    cipher = pk.encrypt(bytes);
	    msg2 = pk.decrypt(cipher);
	    
	    tmp = new String(msg2.toByteArray()); 
	    assertEquals( "Failed decryption 4096bit",content,tmp);
	    
	    //repeat with longer string
	    content ="sdkfjlklsdfjklasdjfklsjdklfjskdfjklsjdklfajlskdjf;klasjdkfljaslkdfjla;ksdjfl;asjd;fklasjdklfjasdklfjas;kldfjksdfjskdljd";
		bytes = content.getBytes();
	    //BigInteger msg = new BigInteger(bytes);
	    
	    cipher = pk.encrypt(bytes);
	    msg2 = pk.decrypt(cipher);
	    
	    tmp = new String(msg2.toByteArray());
	    assertEquals( "Failed decryption long string 4096bit",content,tmp);
		
	}

}
