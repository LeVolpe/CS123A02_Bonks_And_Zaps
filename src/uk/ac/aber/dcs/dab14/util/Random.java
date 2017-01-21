package uk.ac.aber.dcs.dab14.util;

/**
 * Used to generate a random number between specified minimum and maximum number.
 *
 * @author Daniel Bursztynski
 */
public class Random {
	private static final java.util.Random random = new java.util.Random();

	/**
	 * Returns a randomly generated Integer between minimum and maximum values.
	 *
	 * @param min value
	 * @param max value
	 * @return a random Integer
	 */
	public static int nextInt(int min, int max) {
		int n = Math.abs(max - min);

		return Math.min(min, max) + (n == 0 ? 0 : random.nextInt(n));
	}
}
