package uk.ac.aber.dcs.dab14.baz.game.input;

import uk.ac.aber.dcs.dab14.baz.game.GameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Listens for key events
 *
 * @author Daniel Bursztynski
 */
public class InputControl implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Runs code when specified key is released.
	 *
	 * @param e key pressed
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				GameEngine.setPaused(!GameEngine.isPaused());
				break;
			case KeyEvent.VK_ESCAPE:
				GameEngine.setRunning(false);
				break;
			case KeyEvent.VK_UP:
				if (GameEngine.SLEEP > 100) {
					GameEngine.SLEEP -= 100;
				}
				break;
			case KeyEvent.VK_DOWN:
				if (GameEngine.SLEEP < 1000) {
					GameEngine.SLEEP += 100;
				}
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}