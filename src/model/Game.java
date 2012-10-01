package model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Game Model, which handles the whole game
 * 
 * @author danielschmidt
 * 
 */
@SuppressWarnings("serial")
public class Game implements Serializable {

	private static final float GENERATIONRADIUS = 100;
	protected int SHOT_SPEED;
	protected int PLAYER_SPEED;
	Player player;
	ArrayList<SpaceObject> objects;
	int level, score;
	Dimension resolution;

	/**
	 * Simple Gameconstructor, which sets default values for SHOT, PLAYERSPEED
	 * and level
	 * 
	 * @param playername
	 *            name of the player
	 * @param resolution
	 *            resolution of the enviroment
	 */
	public Game(String playername, Dimension resolution) {
		this.SHOT_SPEED = 10;
		this.PLAYER_SPEED = 5;
		this.resolution = resolution;
		this.player = new Player(playername, new Point(this.resolution.height * 2 / 3, this.resolution.width / 2));
		this.player.direction = new Point2D.Double(0, -PLAYER_SPEED);
		this.objects = new ArrayList<SpaceObject>();
		this.level = 0;
		this.score = 0;
	}

	/**
	 * Enhanced constructor, which also sets the speed and the startlevel
	 * 
	 * @param playername
	 *            name of the player
	 * @param resolution
	 *            resolution of the enviroment
	 * @param speed
	 *            playerspeed & half of shotspeed
	 * @param startLevel
	 *            level on which the game starts
	 */
	public Game(String playername, Dimension resolution, int speed, int startLevel) {
		this.SHOT_SPEED = 2 * speed;
		this.PLAYER_SPEED = speed;
		this.resolution = resolution;
		this.player = new Player(playername, new Point(this.resolution.height * 2 / 3, this.resolution.width / 2));
		this.player.direction = new Point2D.Double(0, -PLAYER_SPEED);
		this.objects = new ArrayList<SpaceObject>();
		this.level = startLevel - 1;
		this.score = 0;
	}

	/**
	 * moves the game one step forward, handles gameover and userinput
	 * 
	 * @param deltaAlignment
	 *            alignmentchange of player (negative is turn to left, positive
	 *            is right)
	 * @param forward
	 *            <code>true</code> if the player should move forward
	 * @param shoot
	 *            <code>true</code> if the player shoots
	 * @return <code>true</code> if the game is over
	 */
	public boolean run(int deltaAlignment, boolean forward, boolean shoot) {
		handleUserInput(deltaAlignment, forward, shoot);

		moveSpaceObjects();
		if (player.shouldBeDeletedIfOverlaps(player.overlapingObjects(this.objects))) {
			return true;
		}
		this.objects = handleObjects(this.objects);

		if (getAsteroidCount() == 0) nextLevel();
		return false;
	}

	/**
	 * handles the userinput
	 * 
	 * @param deltaAlignment
	 *            alignmentchange of player (negative is turn to left, positive
	 *            is right)
	 * @param forward
	 *            <code>true</code> if the player should move forward
	 * @param shoot
	 *            <code>true</code> if the player shoots
	 */
	private void handleUserInput(int deltaAlignment, boolean forward, boolean shoot) {
		this.player.addAlignment(deltaAlignment);

		if (forward) {
			this.player.move(resolution, objects);
		}

		if (shoot) {
			launchAShot();
		}

	}

	/**
	 * moves each SpaceObject
	 */
	private void moveSpaceObjects() {
		for (int i = 0; i < this.objects.size(); i++) {
			this.objects.get(i).move(resolution, this.objects);
		}
	}

	/**
	 * handles objects, so deletes them if they should be deleted, all changes
	 * are made recursively
	 * 
	 * @param objects
	 *            the objects which should be handled
	 * @return the rest of the objects which shouldn't be deleted
	 */
	public ArrayList<SpaceObject> handleObjects(ArrayList<SpaceObject> objects) {
		if (objects.size() <= 0) return objects;

		SpaceObject thisObject = objects.get(0);
		ArrayList<SpaceObject> remainingObjects = getRestOfList(objects);
		ArrayList<SpaceObject> thisOverlappingObjects = thisObject.overlapingObjects(objects);

		if (thisObject.shouldBeDeletedIfOverlaps(thisOverlappingObjects)) {

			for (SpaceObject overlappingObject : thisObject.shouldBeBothDeletedIfOverlaps(thisOverlappingObjects)) {
				this.score += overlappingObject.getPoints();
				remainingObjects.remove(overlappingObject);
			}

			this.score += thisObject.getPoints();
			return handleObjects(remainingObjects);

		} else if (thisObject.shouldBeDeletedAsItCrashsWithWall(this.resolution)) {
			return handleObjects(remainingObjects);
		} else {
			remainingObjects = handleObjects(remainingObjects);
			remainingObjects.add(thisObject);
			return remainingObjects;
		}
	}

	/**
	 * returns the list except the first element
	 * 
	 * @param list
	 *            list to be changed
	 * @return the list except the first element
	 */
	private ArrayList<SpaceObject> getRestOfList(ArrayList<SpaceObject> list) {
		if (list.size() <= 1) return new ArrayList<SpaceObject>();

		ArrayList<SpaceObject> output = new ArrayList<SpaceObject>();
		for (int i = 1; i < list.size(); i++) {
			output.add(list.get(i));
		}
		return output;
	}

	/**
	 * levels the game up
	 */
	public void nextLevel() {
		setLevel(this.level + 1);
	}

	/**
	 * sets the level, generates the corresponding asteroids and adds them to
	 * the objects list
	 * 
	 * @param level
	 *            the level which should be set
	 */
	public void setLevel(int level) {
		this.level = level;
		generateAsteroids(level, level * 2);
	}

	/**
	 * generates and adds asteroids
	 * 
	 * @param count
	 *            count of asteroids which should be generated
	 * @param points
	 *            the total amount of points which should be generated
	 */
	private void generateAsteroids(int count, int points) {
		Integer availablePoints = points;

		for (int i = 0; i < count; i++) {

			int thisPoints = generateRandomPoints(availablePoints, count - i);
			Asteroid asteroid = generateAsteroidByPoints(thisPoints);

			if (asteroid.overlapingObjects(this.objects).size() >= 1
			        || this.player.isWithinRange(asteroid, GENERATIONRADIUS)) {
				i--;
			} else {
				availablePoints -= thisPoints;
				this.objects.add(asteroid);
			}
		}
	}

	/**
	 * generates a random number which should spread the points between the
	 * times
	 * 
	 * @param remainingPoints
	 *            the remaining points
	 * @param times
	 *            the parts on which the points should be spread
	 * @return a random number which should spread the points between the times
	 */
	private int generateRandomPoints(int remainingPoints, int times) {
		if (times < 2) return remainingPoints;
		return (int) (Math.ceil(Math.random() * ((remainingPoints / times) - 1)) + 1);
	}

	/**
	 * generates an Asteroids which has the value of points
	 * 
	 * @param points
	 *            the value of the asteroid
	 * @return an Asteroids which has the value of points
	 */
	private Asteroid generateAsteroidByPoints(int points) {

		int size = 30;
		Point speed = new Point();

		for (int i = 1; i < points; i++) {
			if (Math.round(Math.random()) == 1 && size != 10) {
				size -= 10;
			} else {
				int distribution = (int) (Math.random() * 5);
				speed.x += distribution;
				speed.y += 5 - distribution;
			}
		}
		speed.x = (int) (speed.x * generateRandomSign());
		speed.y = (int) (speed.y * generateRandomSign());

		return new Asteroid(new Point((int) (Math.random() * this.resolution.width),
		        (int) (Math.random() * this.resolution.height)), size, speed);
	}

	/**
	 * returns random 1 or -1
	 * 
	 * @return random 1 or -1
	 */
	private int generateRandomSign() {
		if (Math.round((Math.random() * 2)) == 1) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Generates the printable vision of each object in the game
	 * 
	 * @return each object in the game as printable in an arraylist
	 */
	public ArrayList<Printable> getPrintables() {
		ArrayList<Printable> list = new ArrayList<Printable>();

		for (SpaceObject object : this.objects) {
			list.add(object.getPrintable());
		}
		list.add(player.getPrintable());
		return list;
	}

	/**
	 * getter for score
	 * 
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * returns the points which can be earned if all asteroids were shot down
	 * 
	 * @return the points which can be earned if all asteroids were shot down
	 */
	public int getTotalPoints() {
		int totalPoints = 0;

		for (SpaceObject object : this.objects) {
			totalPoints += object.getPoints();
		}

		return totalPoints;
	}

	/**
	 * returns the amount of asteroids in the game
	 * 
	 * @return the amount of asteroids in the game
	 */
	public int getAsteroidCount() {
		int i = 0;
		for (SpaceObject object : this.objects) {
			if (object.getClass() == Asteroid.class) i++;
		}
		return i;
	}

	/**
	 * getter for resolution
	 * 
	 * @return the resolution
	 */
	public Dimension getResolution() {
		return this.resolution;
	}

	/**
	 * launches a shot
	 */
	public void launchAShot() {
		this.objects.add(new Shot(this.player.position, SHOT_SPEED, this.player.alignment, this.resolution));
	}

	/**
	 * gets the Playernae
	 * 
	 * @return the playername
	 */
	public String getPlayername() {
		return this.player.name;
	}

}
