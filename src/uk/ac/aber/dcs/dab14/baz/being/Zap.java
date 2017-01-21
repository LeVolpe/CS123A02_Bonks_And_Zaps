package uk.ac.aber.dcs.dab14.baz.being;

import uk.ac.aber.dcs.dab14.baz.game.GameEngine;

import java.awt.*;
import java.util.List;

/**
 * Type of a Monster
 * Kills all of the Bonks when in the same room
 * Cannot die
 * Cannot breed
 *
 * @author Daniel Bursztynski
 */
public class Zap extends Monster {

	private static int zapId; // Used to keeps track of the Zap ID

	/**
	 * Constructs a Zap
	 *
	 * @param name being the name of the Zap
	 */
	public Zap(String name) {
		super(name + "@" + ++zapId);
	}

	/**
	 * For each Bonk found in the room, kill and add to despawn que
	 *
	 * @throws CannotActException
	 */
	@Override
	public void act() throws CannotActException {
		super.act();

		List<Monster> monsters = getLocation().getMonsters();

		if (monsters.stream().filter(m -> !equals(m)).count() > 0) {
			monsters.stream().filter(m -> !(m instanceof Zap)).forEach(m -> {
				System.err.println(m.getName() + " got zapped!");
				GameEngine.getInstance().despawnMonster(m);
			});
		}
	}

	/**
	 * Checks if in the next room there is a zap if so return false otherwise return true
	 *
	 * @param p being Position
	 * @return true if Position does not contain Zaps, false otherwise
	 */
	public boolean canMove(Position p) {
		return p.getMonsters().stream().filter(m -> m instanceof Zap).count() == 0;
	}

	/**
	 * Gets the colour of the Zap specified by the user
	 *
	 * @return Color of the Zap
	 */
	public Color getFill() {
		return Color.BLACK;
	}

	/**
	 * Gets the colour of the Zaps outline specified by the user
	 *
	 * @return Color of the Zaps outline
	 */
	public Color getOutline() {
		return Color.LIGHT_GRAY;
	}
}
