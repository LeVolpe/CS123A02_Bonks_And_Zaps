package uk.ac.aber.dcs.dab14.baz.being;

/**
 * This exception is thrown when Being can move
 *
 * @author Daniel Bursztynski
 */
public class CannotActException extends Exception {

	public CannotActException(String message) {
		super(message);
	}
}
