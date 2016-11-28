package cs.fit.edu;

public interface Unmarshall<E> {
	/**
	 * Unmarshalls the content of obj 
	 * @param obj the object to unmarshall from
	 * @return the instance object marshalled, otherwise null 
	 */
	public void unmarshall(E obj);
}
