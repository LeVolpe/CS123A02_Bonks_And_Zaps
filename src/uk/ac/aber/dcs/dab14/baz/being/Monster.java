package uk.ac.aber.dcs.dab14.baz.being;

import uk.ac.aber.dcs.dab14.util.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Monster is extended by Bonk and Zap which allows me to get rid of repeating code
 * It contains methods that ar used by both
 *
 * @author Daniel Bursztynski
 */
public abstract class Monster implements Being {

	private final String name; // Stores the name of the Monster
	private Position location; // Stores the location of the Monster

	/**
	 * Constructs a Monster
	 *
	 * @param name being the name of the Monster
	 */
	public Monster(String name) {
		this.name = name;
	}

	@Override
	public void act() throws CannotActException {
		List<Position> neighbours = location.getNeighbours();
		boolean freeNeighour = neighbours.stream().filter(this::canMove).count() > 0;
		boolean currentRoomSafe = location.getMonsters().stream().filter(m -> m instanceof Zap).count() == 0;
		int roomIdx;
		Position newRoom;

		do {
			roomIdx = uk.ac.aber.dcs.dab14.util.Random.nextInt(0, neighbours.size() + 1);
			newRoom = roomIdx < neighbours.size() ? neighbours.get(roomIdx) : null;

			if (newRoom != null && canMove(newRoom)) {
				break;
			} else if (currentRoomSafe && newRoom == null) {
				break;
			} else if (!freeNeighour) {
				newRoom = null;
				break;
			}
		} while (true);

		if (newRoom != null && canMove(newRoom)) {
			location.removeMonster(this, false);
			(location = newRoom).addMonster(this);
		}
	}

	/**
	 * Checks if specified Monster can move to another room
	 *
	 * @param l being Position
	 * @return true if it can move, otherwise false
	 */
	public abstract boolean canMove(Position l);

	/**
	 * Draws the monster at specified coordinates and specified size
	 *
	 * @param g being Graphics
	 * @param x being x coordinates
	 * @param y being y coordinates
	 * @param width being monster width
	 * @param height being monster height
	 */
	public void draw(Graphics g, int x, int y, int width, int height) {
		g.setColor(getFill());
		g.fillRect(x, y, width, height);
		g.setColor(getOutline());
		g.drawRect(x, y, width, height);
	}

	public Color getFill() {
		return Color.WHITE;
	}

	/**
	 * Gets the location of the monster
	 *
	 * @return Position of the Monster
	 */
	@Override
	public Position getLocation() {
		return location;
	}


	/**
	 * Sets the location of the Monster
	 *
	 * @param location x and y values
	 */
	@Override
	public void setLocation(Position location) {
		this.location = location;
	}

	/**
	 * Gets the name of the Monster
	 *
	 * @return name of the Monster
	 */
	@Override
	public String getName() {
		return name;
	}

	public Color getOutline() {
		return getFill();
	}
}
