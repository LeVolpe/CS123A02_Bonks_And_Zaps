package uk.ac.aber.dcs.dab14.baz.being;

import javafx.util.Pair;
import uk.ac.aber.dcs.dab14.baz.game.GameEngine;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/**
 * This class represents a room with x and y coordinates
 * It is used by the Monster class as each Monster is withing a Room/Position
 *
 * @author Daniel Bursztynski
 */
public class Position {

	public static final int MAX_NEIGHBOURS = 8; // Specifies the max amount of rooms/locations around a room/location
	public static final int MAX_MONSTERS = 9; // Specifies the max amount of monsters that can fit in one room at any one time
	public static final double MONSTER_MARGIN = 4; // Used to set a gap/margin around the monsters
	public static final Color ROOM_FILL = Color.BLACK; // Determines the colour of the rooms/locations
	public static final Color ROOM_OUTLINE = Color.DARK_GRAY; // Determines the room/location outline
	public static final double ROOM_PADDING = 4; // Determines the gap between the room/location outline and the monster
	private final int x, y; // Defines the location using x and y axis
	private final List<Monster> monsters = new ArrayList<>(); // Stores all of the monsters within the position/location
	private final List<Position> neighbours = new ArrayList<>(); // Stores all of the rooms/locations attached to this location/room

	/**
	 * Constructs a room/location that is identified by Position
	 *
	 * @param x coordinates
	 * @param y coordinates
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds a monster to the room/location
	 *
	 * @param m being a Monster
	 * @return false if room/location contains the monster otherwise add Monster
	 */
	public boolean addMonster(Monster m) {
		if (monsters.contains(m)) {
			return false;
		}

		m.setLocation(this);

		if (!GameEngine.getMonsters().contains(m)) { // If Monster doesn't exist in GameEngine monsters list then add
			GameEngine.getMonsters().add(m);
		}

		return monsters.add(m); // Add Monster to monsters list
	}

	/**
	 * Allows to Add an adjacent room/location
	 *
	 * @param p being Position/room location
	 */
	public void addNeighbour(Position p) {
		if (neighbours.size() < MAX_NEIGHBOURS && !neighbours.contains(p)) {
			neighbours.add(p);
		}
	}

	/**
	 * Draws a room which then it fills the room with monsters as well as drawing them
	 * monster radius is worked out depending on the number of monsters in such a way that
	 * all monsters can be fitted in the room
	 *
	 * @param g being Graphics
	 * @param x being x coordinates
	 * @param y being y coordinates
	 * @param width being the width of the room
	 * @param height being the height of the room
	 */

	public void draw(Graphics g, int x, int y, int width, int height) {
		int numMonsters = monsters.size();
		Monster m;

		g.setColor(ROOM_FILL); // Colour is set to ROOM_FILL value
		g.fillRect(x, y, width, height); // Room background is drawn
		g.setColor(ROOM_OUTLINE);
		g.drawRect(x, y, width, height); // Room borders are drawn

		if (numMonsters == 0) { // If the number of monsters is 0 return and do not continue
			return;
		}

		double actualRoomWidth = (width - ROOM_PADDING * 2);
		double actualRoomHeight = (height - ROOM_PADDING * 2);

		double monsterMargin = numMonsters == 1 ? 0 : (MONSTER_MARGIN * (1 - (1 / Math.ceil(numMonsters / Math.sqrt(numMonsters)))));
		// if numMonsters == 1 then monsterMargin = 0 else
		// monsterMargin = (MONSTER_MARGIN * ( 1 - ( 1 / RoundUp(numMonsters / SquareRoot(numMonsters)))))


		double totalMonstersX = 0;
		double totalMonstersY = 0;
		double monsterRadius = Math.max(width, height);
		int prevX, prevY;

		while (totalMonstersX < 1 || totalMonstersY < 1 || totalMonstersX * totalMonstersY < numMonsters ||
				(monsterRadius + monsterMargin) * totalMonstersX > actualRoomWidth || (monsterRadius + monsterMargin) * totalMonstersY > actualRoomHeight) {
			monsterRadius--;

			prevX = (int) totalMonstersX;
			totalMonstersX = Math.floor(actualRoomWidth / (monsterRadius + monsterMargin));

			if (totalMonstersY < 1 || (totalMonstersX * totalMonstersY < numMonsters && prevX != (int) totalMonstersX)) {
				prevY = (int) totalMonstersY;
				totalMonstersY = Math.ceil(actualRoomHeight / (monsterRadius + monsterMargin));

				if (prevY != (int) totalMonstersY) {
					totalMonstersY = prevY + 1;
				}
			}
		}

		double monsterOffsetX = (actualRoomWidth - (totalMonstersX * (monsterRadius + monsterMargin))) / 2;
		double monsterOffsetY = (actualRoomHeight - (totalMonstersY * (monsterRadius + monsterMargin))) / 2;

		double monsterRow;
		outer:
		for (monsterRow = 0; monsterRow < totalMonstersY; monsterRow++) {
			double monsterCol;
			for (monsterCol = 0; monsterCol < totalMonstersX; monsterCol++) {
				int i = (int) (monsterCol + totalMonstersX * monsterRow);

				if (i >= monsters.size()) {
					break outer;
				}

				m = monsters.get(i);

				double monsterMarginX = Math.max(0, MONSTER_MARGIN * (monsterCol));
				double monsterMarginY = Math.max(0, MONSTER_MARGIN * (monsterRow));

				double monsterX = x + monsterOffsetX + Math.ceil(ROOM_PADDING + monsterRadius * monsterCol) + monsterMarginX;
				double monsterY = y + monsterOffsetY + Math.ceil(ROOM_PADDING + monsterRadius * monsterRow) + monsterMarginY;

				m.draw(g, (int) monsterX, (int) monsterY, (int) monsterRadius, (int) monsterRadius);
			}
		}
	}

	/**
	 * Compares coordinates of two this.Position to another Objects Position
	 *
	 * @param o being a Position
	 * @return true if object is it self, false if Object == null or if runtime of this class not = to Objects runtime
	 *          and true if coordinates are the same
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Position p = (Position) o;

		return x == p.x && y == p.y;
	}

	/**
	 *  Returns a List of Monsters in this Position
	 *
	 * @return List of Monsters
	 */
	public List<Monster> getMonsters() {
		return Collections.unmodifiableList(monsters);
	}

	/**
	 * Returns a List of adjacent Positions
	 *
	 * @return List of Positions
	 */
	public List<Position> getNeighbours() {
		return Collections.unmodifiableList(neighbours);
	}

	/**
	 * Get the x coordinate of this Position
	 *
	 * @return x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Get the x coordinate of this Position
	 *
	 * @return y coordinate
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	public boolean isFull() {
		Queue<Pair<Monster, Position>> spawn = GameEngine.getInstance().getSpawnQueue();
		int size = monsters.size();
		for (Pair<Monster, Position> pair : spawn) {
			if (pair.getValue().equals(this)) {
				size++;
			}
		}

		return size >= MAX_MONSTERS;
	}

	public static boolean isValid(int x, int y) {
		return x >= 0 && x < GameEngine.GRID_SIZE_X && y >= 0 && y < GameEngine.GRID_SIZE_Y;
	}

	public boolean removeMonster(Being m, boolean global) {
		if (!monsters.contains(m)) {
			return false;
		}

		if (global && GameEngine.getMonsters().contains(m)) {
			GameEngine.getMonsters().remove(m);
		}

		return monsters.remove(m);
	}

	@Override
	public String toString() {
		return "Room{x=" + x + ", y=" + y + '}';
	}
}
