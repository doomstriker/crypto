package cs.fit.edu;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

public class RSAKeyTest {

	@Test
	public final void testSetModulus() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSetExponent() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	@Test
	public final void testSavePublic() {
		fail("Not yet implemented");
	}

	@Test
	public final void testRSAEncrypt() {
		fail("Not yet implemented");
	}

}
