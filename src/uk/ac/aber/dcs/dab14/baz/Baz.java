package uk.ac.aber.dcs.dab14.baz;

import uk.ac.aber.dcs.dab14.baz.game.GameEngine;
import uk.ac.aber.dcs.dab14.baz.ui.MainFrame;

/**
 * This is the entry point to the program.
 * Initializes a new MainFrame and starts the GameEngine,
 * both in a separate Thread.
 *
 * @author Daniel Bursztynski
 */
public class Baz {

	public static final String APP_NAME = "Bonks & Zaps"; // Used for the application name

	/**
	 * Initializes a new MainFrame and starts the GameEngine in a separate Thread.
	 * Both are Singletons (because only one object is needed)
	 *
	 * @param args No Arguments
	 */
	public static void main(String[] args) {
		MainFrame.getInstance();

		new Thread(() -> {
			GameEngine.getInstance().init();
			GameEngine.getInstance().run();
		}).start();
	}
}