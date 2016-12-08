package cs.fit.edu.rsa.util;

import java.math.BigInteger;

public class EratosthenesSieve {

	private BigInteger suggestedPrime;
	private boolean isPossiblyPrime;

	/**
	 * Set the suggested prime value
	 * @param suggPrime the suggested prime 
	 */
	public void setSuggestedPrime(BigInteger suggPrime) {
		this.suggestedPrime = suggPrime;
	}

	/**
	 * Returns the suggestedPrime 
	 * @return the suggested prime if set, null otherwise
	 */
	public BigInteger isSuggestedPrime() {
		return this.suggestedPrime;
	}

	/**
	 * Clear/Sets the isPossiblyPrime flag
	 * @param flag true to set the flag, false otherwise
	 */
	public void setIsPossiblyPrime(boolean flag) {
		this.isPossiblyPrime = flag;
	}

	/**
	 * Returns the value of the isPossiblyPrime flag
	 * @return true if the value is set, false otherwise
	 */
	public boolean isPossiblyPrime() {
		return this.isPossiblyPrime;
	}

}
