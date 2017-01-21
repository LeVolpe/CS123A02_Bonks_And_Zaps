package uk.ac.aber.dcs.dab14.baz.being;

/**
 * @author Christopher William Loftus
 */
public interface Being {
	/**
	 * When called the Being does its stuff, e.g. move. So this represents behaviour.
	 * What this is will vary between different kinds of Being
	 *
	 * @throws CannotActException Thrown if the state of the Being prevents it from acting
	 */
	void act() throws CannotActException;

	/**
	 * Returns the current location of the Being (which Position it's in)
	 *
	 * @return Returns a Position that encapsulates its coordinates in Grid World
	 */
	Position getLocation();

	/**
	 * Allows the relocation of the Being to another Position in Grid World
	 *
	 * @param location x and y values
	 */
	void setLocation(Position location);

	/**
	 * Every inhabitant on GridWorld must have a name given to them
	 * at birth or creation. It is fixed, but can be discovered via this method
	 *
	 * @return The name
	 */
	String getName();
}
