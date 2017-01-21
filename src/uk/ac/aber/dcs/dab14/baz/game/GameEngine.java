package uk.ac.aber.dcs.dab14.baz.game;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.util.Pair;
import uk.ac.aber.dcs.dab14.baz.being.*;
import uk.ac.aber.dcs.dab14.baz.ui.GamePanel;
import uk.ac.aber.dcs.dab14.baz.ui.MainFrame;
import uk.ac.aber.dcs.dab14.util.Random;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is the class that drives the game
 *
 * @author Daniel Bursztynski
 */
public class GameEngine implements Runnable {

	public static final int GRID_SIZE_X = 20; // Determines the number of rooms in the x direction
	public static final int GRID_SIZE_Y = 20; // Determines the number of rooms in the y direction
	public static final int START_BONKS = 20; // Used to specify the number of Bonks that you start off with
	public static final int START_ZAPS = 7; // Used to specify the number of Zaps that you start off with
	public static final int MAX_TURNS = Integer.MAX_VALUE; // Used to specify the number of turns that the game runs for
	public static final List<Monster> monsters = new ArrayList<>(); // This is a list of all of the monsters in the game
	private static final AtomicBoolean running = new AtomicBoolean(); // Used to determine if the game is running
	private static final AtomicBoolean paused = new AtomicBoolean(); // Used to determine if the game is on pause
	private static final Position[][] locations = new Position[GRID_SIZE_X][GRID_SIZE_Y]; // This is a 2D array that represent all of the rooms
	public static int SLEEP = 1000; // Determines how long the game waits before continuing to next turn. It also allows me to speed up the game when testing
	public static int TURN = 0; // Keeps the track of the current turn
	private static GameEngine engine; // The current instance of the GameEngine
	private final Queue<Pair<Monster, Position>> spawnQueue = new LinkedList<>(); // Used to add monsters to rooms/locations
	private final Queue<Monster> despawnQueue = new LinkedList<>(); // Used to remove monsters from rooms/locations

	/**
	 * Monsters are added to despawnQueue to be removed later on
	 *
	 * @param m being Monster
	 */
	public void despawnMonster(Monster m) {
		despawnQueue.offer(m);
	}

	/**
	 * This class is responsible for creating Positions/Rooms
	 * All Positions are stored in a 2D array
	 *
	 */
	private void generatePositions() {
		for (int x = 0; x < GRID_SIZE_X; x++) {
			for (int y = 0; y < GRID_SIZE_Y; y++) {
				locations[x][y] = new Position(x, y);
			}
		}

		double precision = 360D / Position.MAX_NEIGHBOURS;
		int[][] adjPoints = new int[Position.MAX_NEIGHBOURS][2];

		for (int i = 0; i < Position.MAX_NEIGHBOURS; i++) {
			int x = (int) Math.round(Math.sin(Math.toRadians(precision * i)));
			int y = (int) Math.round(Math.cos(Math.toRadians(precision * i)));

			adjPoints[i] = new int[]{x, y};
		}

		for (int x = 0; x < GRID_SIZE_X; x++) {
			for (int y = 0; y < GRID_SIZE_Y; y++) {
				Position r = locations[x][y];

				if (r == null) {
					continue;
				}

				for (int i = 0; i < 8; i++) {
					int roomX = x + adjPoints[i][0];
					int roomY = y + adjPoints[i][1];

					if (Position.isValid(roomX, roomY) && getPositionAt(roomX, roomY) != null) {
						r.addNeighbour(getPositionAt(roomX, roomY));
					}
				}
			}
		}
	}

	/**
	 * Allows su to controll so that only one instance of GameEngine runs at any one time
	 *
	 * @return existing instance, if it doesnt exist, return a new one
	 */
	public static GameEngine getInstance() {
		if (engine != null) return engine;
		else return (engine = new GameEngine());
	}

	/**
	 * Gets all existing and any kind of monsters
	 *
	 * @return List of monsters
	 */
	public static List<Monster> getMonsters() {
		return monsters;
	}

	/**
	 * Gets the Position/Room at specified x and y coordinates
	 *
	 * @param x being x coordinates
	 * @param y being y coordinates
	 * @return Position/Room
	 */
	public static Position getPositionAt(int x, int y) {
		return locations[x][y];
	}

	public Queue<Pair<Monster, Position>> getSpawnQueue() {
		return spawnQueue;
	}

	/**
	 * Basically creates all of the monsters and distributes them randomly around the rooms
	 */
	public void init() {
		generatePositions();

		int bonks = START_BONKS, zaps = START_ZAPS;

		while (bonks > 0 || zaps > 0) {
			int x, y;
			Position r;

			do {
				x = Random.nextInt(0, GRID_SIZE_X);
				y = Random.nextInt(0, GRID_SIZE_Y);
				r = locations[x][y];
			} while (r == null || !r.getMonsters().isEmpty());

			if (bonks > 0) {
				spawnQueue.offer(new Pair<>(new Bonk("SpawnBonk"), r));
				bonks--;
			} else if (zaps > 0) {
				spawnQueue.offer(new Pair<>(new Zap("SpawnBonk"), r));
				zaps--;
			}
		}
	}

	/**
	 * Returns if the game is paused
	 *
	 * @return true if is paused, false otherwise
	 */
	public static boolean isPaused() {
		return paused.get();
	}

	/**
	 * Sets the game to paused depending on the parameter
	 * true if paused, false otherwise
	 *
	 * @param paused
	 */
	public static void setPaused(boolean paused) {
		GameEngine.paused.set(paused);
	}

	/**
	 * Returns if the game is paused
	 *
	 * @return true if is paused, false otherwise
	 */
	public static boolean isRunning() {
		return running.get();
	}

	/**
	 * Sets the game to running depending on the parameter
	 * true if running, false otherwise
	 *
	 * @param running
	 */
	public static void setRunning(boolean running) {
		GameEngine.running.set(running);
	}

	public void processGame() {
		monsters.forEach(m -> {
			try {
				m.act();
			} catch (CannotActException e) {
				e.printStackTrace();
			}
		});

		while (!despawnQueue.isEmpty()) {
			Monster m = despawnQueue.poll();

			m.getLocation().removeMonster(m, true);
		}

		while (!spawnQueue.isEmpty()) {
			Pair<Monster, Position> spawn = spawnQueue.poll();
			Monster m = spawn.getKey();
			Position p = spawn.getValue();

			p.addMonster(m);
		}
	}

	private void repaintGame() {
		GamePanel panel = MainFrame.getInstance().getPanel();

		panel.paintImmediately(0, 0, panel.getWidth(), panel.getHeight());
	}

	/**
	 * This is the main loop of the game
	 */
	@Override
	public void run() {
		setRunning(true);

		while (isRunning() && TURN < MAX_TURNS && monsters.stream().filter(m -> (m instanceof Bonk)).count() < (Position.MAX_MONSTERS * GRID_SIZE_X * GRID_SIZE_Y) - (START_ZAPS * Position.MAX_MONSTERS)) {
			if (isPaused()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException ignored) {
				}

				continue;
			}

			System.out.println("TURN: " + ++TURN);
			processGame();
			repaintGame();
			System.out.println("Bonks Left: " + monsters.stream().filter(m -> (m instanceof Bonk)).count());

			try {
				Thread.sleep(SLEEP);
			} catch (InterruptedException ignored) {
			}
		}

		System.out.println("Game has ended.");

	}
}
