package model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Game {

	protected static final int SHOT_SPEED = 10;
	protected static final int PLAYER_SPEED = 5;
	Player player;
	ArrayList<SpaceObject> objects;
	int level, score;
	Dimension resolution;

	public Game(String playername, Dimension resolution) {
		this.resolution = resolution;
		this.player = new Player(playername, new Point(this.resolution.height * 2 / 3, this.resolution.width / 2));
		this.player.direction = new Point2D.Double(0, -PLAYER_SPEED);
		this.objects = new ArrayList<SpaceObject>();
		this.level = 0;
		this.score = 0;
	}

	// FIXME Fix that the spaceobject doesn't move
	public void run(int deltaAlignment, boolean forward, boolean shoot) {
		handleUserInput(deltaAlignment, forward, shoot);

		moveSpaceObjects();
		if (player.shouldBeDeletedIfOverlaps(player.overlapingObjects(this.objects))) {
			throw new GameOverException(this.score);
		}
		this.objects = handleObjects(this.objects);

		if (getAsteroidCount() == 0) nextLevel();
	}

	private void handleUserInput(int deltaAlignment, boolean forward, boolean shoot) {
		this.player.addAlignment(deltaAlignment);

		if (forward) {
			this.player.move(resolution, objects);
		}

		if (shoot) {
			launchAShot();
		}

	}

	// TODO Getter and setter for objects which clones them allready?
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

	private ArrayList<SpaceObject> getRestOfList(ArrayList<SpaceObject> list) {
		if (list.size() <= 1) return new ArrayList<SpaceObject>();

		ArrayList<SpaceObject> output = new ArrayList<SpaceObject>();
		for (int i = 1; i < list.size(); i++) {
			output.add(list.get(i));
		}
		return output;
	}

	private void moveSpaceObjects() {
		for (int i = 0; i < this.objects.size(); i++) {
			this.objects.get(i).move(resolution, this.objects);
		}
	}

	public void nextLevel() {
		this.level++;
		setLevel(this.level);
	}

	public void setLevel(int level) {
		this.level = level;
		generateAsteroids(level * 2, (int) Math.pow(2, level));
	}

	private void generateAsteroids(int count, int points) {
		Integer availablePoints = points;

		for (int i = 0; i < count; i++) {

			int thisPoints = generateRandomPoints(availablePoints, count - i);
			Asteroid asteroid = generateAsteroidByPoints(thisPoints);

			if (asteroid.overlapingObjects(this.objects).size() >= 1 || this.player.overlap(asteroid)) {
				i--;
			} else {
				availablePoints -= thisPoints;
				this.objects.add(asteroid);
			}
		}
	}

	private int generateRandomPoints(int maxPoints, int times) {
		if (times < 2) return maxPoints;
		return (int) (Math.ceil(Math.random() * ((maxPoints / times) - 1)) + 1);
	}

	private Asteroid generateAsteroidByPoints(int points) {

		int size = 30;
		Point speed = new Point();

		for (int i = 1; i < points; i++) {
			if (Math.round(Math.random()) == 1) {
				if (size != 10) {
					size -= 10;
				} else {
					i--;
				}
			} else {
				int distribution = (int) (Math.random() * 5);
				speed.x += distribution;
				speed.y += 5 - distribution;
			}
		}
		speed.x = (int) (speed.x * getSign());
		speed.y = (int) (speed.y * getSign());

		return new Asteroid(new Point((int) (Math.random() * this.resolution.width),
		        (int) (Math.random() * this.resolution.height)), size, speed);
	}

	private int getSign() {
		long a = Math.round((Math.random() * 2) - 1);
		if (a == 0) return 1;
		return (int) a;
	}

	public ArrayList<Printable> getPrintables() {
		ArrayList<Printable> list = new ArrayList<Printable>();

		for (SpaceObject object : this.objects) {
			list.add(object.getPrintable());
		}
		list.add(player.getPrintable());
		return list;
	}

	public int getTotalPoints() {
		int totalPoints = 0;

		for (SpaceObject object : this.objects) {
			totalPoints += object.getPoints();
		}

		return totalPoints;
	}

	public int getAsteroidCount() {
		int i = 0;
		for (SpaceObject object : this.objects) {
			if (object.getClass() == Asteroid.class) i++;
		}
		return i;
	}

	public void launchAShot() {
		Shot shot = new Shot(this.player.position, SHOT_SPEED, this.player.alignment, this.resolution);
		this.objects.add(shot);
	}

}
