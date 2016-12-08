/**
 * 
 */
package cs.fit.edu.rsa.output;

/**
 * @author wburgos
 *
 */
public interface Marshall<E> {
	/**
	 * marshalls the content of obj 
	 * @return the instance object marshalled
	 */
	E marshall();
}
