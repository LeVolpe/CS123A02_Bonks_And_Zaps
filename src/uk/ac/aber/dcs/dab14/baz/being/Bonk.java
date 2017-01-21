package uk.ac.aber.dcs.dab14.baz.being;

import javafx.util.Pair;
import uk.ac.aber.dcs.dab14.baz.game.GameEngine;
import uk.ac.aber.dcs.dab14.util.Random;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Type of a Monster
 * Bonk is able to breed so has children
 * Has Gender
 * Is a mortal
 *
 * @author Daniel Bursztynski
 */
public class Bonk extends Monster {

	private static int bonkId; // Used to keep the track of the Bonk ID
	private final List<Bonk> children = new ArrayList<>(); // Stores Bonks children
	private final byte gender; // Used to determine gender
	private boolean bred = false; // Used to prevent Bonks from breeding more than once each turn

	public Bonk(String name) {
		super("Bonk@" + ++bonkId);
		this.gender = (byte) Random.nextInt(0, 2);
	}

	/**
	 * Checks if the Bonk has not already bred
	 * For each Monster in the same room it will check if it can breed with it
	 * If both above are true two monsters will breed and create a child
	 *
	 * @throws CannotActException
	 */
	@Override
	public void act() throws CannotActException {
		super.act();

		if (bred) {
			bred = false;
			return;
		}

		List<Monster> monsters = getLocation().getMonsters();
		Stream<Bonk> mates = monsters.stream().filter(m -> m instanceof Bonk && isBreedable((Bonk) m)).map(m -> (Bonk) m);

		mates.findAny().ifPresent(mate -> {
			if (getLocation().isFull()) {
				return;
			}

			Bonk child = new Bonk("Bonk");

			GameEngine.getInstance().getSpawnQueue().offer(new Pair<>(child, getLocation()));
			children.add(child);
			mate.children.add(child);

			if (!isMale()) {
				System.out.println(getName() + "[" + isMale() + "]" + " gave birth to: " + child.getName() + "[" + child.isMale() + "]" + " with: " + mate.getName() + "[" + mate.isMale() + "]");
				//System.out.println(getName() + " gave birth to: " + child.getName() + " with: " + mate.getName());
			} else {
				System.out.println(mate.getName() + "[" + mate.isMale() + "]" + " gave birth to: " + child.getName() + "[" + child.isMale() + "]" + " with: " + getName() + "[" + isMale() + "]");
				//System.out.println(mate.getName() + " gave birth to: " + child.getName() + " with: " + getName());
			}

			bred = true;
			mate.bred = true;
		});

	}

	/**
	 * Determines if the Bonk can make the move
	 *
	 * @param p being the Position
	 * @return true if Position/room is not full && Position/room does not contain Zaps otherwise false
	 */
	@Override
	public boolean canMove(Position p) {
		return !p.isFull() && (p.getMonsters().stream().filter(m -> m instanceof Zap).count() == 0);
	}

	/**
	 * Gets all of the Bonks children
	 *
	 * @return List of Bonks
	 */
	public List<Bonk> getChildren() {
		return children;
	}

	/**
	 * Gets the Color of the Bonk, Blue if mala and magenta if female
	 *
	 * @return Color
	 */
	public Color getFill() {
		return isMale() ? Color.BLUE : Color.MAGENTA;
	}

	/**
	 * Gets the gender of the Monster
	 *
	 * @return byte representing gender
	 */
	public byte getGender() {
		return gender;
	}

	/**
	 * Checks if the Bonk can breed
	 * Checks if the Bonk is opposite gender
	 * Checks if the Bonk is not related
	 * Checks if it has not bred already
	 *
	 * @param b being a Bonk
	 * @return true if all statements above are true, false otherwise
	 */
	public boolean isBreedable(Bonk b) {
		return getGender() != b.getGender() && !isRelated(b) && !b.isRelated(this) && !bred && !b.bred;
	}

	/**
	 * Checks if the Bonk is a Male
	 *
	 * @return true if getGender == 1, false otherwise
	 */
	public boolean isMale() {
		return getGender() != 0;
	}

	/**
	 * Loops through all children checking if it is related
	 *
	 * @param b
	 * @return true when related, false otherwise
	 */
	public boolean isRelated(Bonk b) {
		for (Bonk child : getChildren()) {
			if (b.equals(child)) {
				return true;
			}
		}

		return false;
	}
}
