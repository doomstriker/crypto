/**
 * 
 */
package cs.fit.edu;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

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
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#unmarshall(java.lang.String)}.
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
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#marshall()}.
	 */
	@Test
	public final void testMarshall() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEqualsObject() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#setPrivateExponent(java.math.BigInteger)}.
	 */
	@Test
	public final void testSetPrivateExponent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#setP(java.math.BigInteger)}.
	 */
	@Test
	public final void testSetP() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#setdP(java.math.BigInteger)}.
	 */
	@Test
	public final void testSetdP() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#setdQ(java.math.BigInteger)}.
	 */
	@Test
	public final void testSetdQ() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#setqInv(java.math.BigInteger)}.
	 */
	@Test
	public final void testSetqInv() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#setQ(java.math.BigInteger)}.
	 */
	@Test
	public final void testSetQ() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#generateKey(int, double)}.
	 */
	@Test
	public final void testGenerateKey() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#saveKeyPair(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testSaveKeyPair() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#savePrivateKey(java.lang.String)}.
	 */
	@Test
	public final void testSavePrivateKey() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cs.fit.edu.RSAPrivateKey#totient(java.math.BigInteger, java.math.BigInteger)}.
	 */
	@Test
	public final void testTotient() {
		fail("Not yet implemented");
	}

}
