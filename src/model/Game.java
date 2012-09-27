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

	//FIXME: Shots which overlapp should only delete the latest shot (with highest distance from player)
	
	public void run(int deltaAlignment, boolean forward, boolean shoot) {
		handleUserInput(deltaAlignment, forward, shoot);

		moveSpaceObjects();
		handleOverlappingSpaceObjects();

		if (getAsteroidCount() == 0) nextLevel();
		if (player.overlapingObjects(this.objects).size() > 0) throw new GameOverException(this.score);
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

	private void handleOverlappingSpaceObjects() {
		for (SpaceObject object : this.objects) {
			handleOverlapingObjects(object);
		}
	}

	private void handleOverlapingObjects(SpaceObject object) {
		ArrayList<SpaceObject> overlapingObjects = object.overlapingObjects(this.objects);

		if (overlapingObjects.size() > 0) {
			deleteOverlappingObjects(object, overlapingObjects);
		}

		if (object.getClass() == Shot.class && object.getBorders(resolution).contains(true)) {
			this.objects = getRemainingObjects(object);
		}
	}

	private void deleteOverlappingObjects(SpaceObject object, ArrayList<SpaceObject> overlapingObjects) {
		ArrayList<SpaceObject> toBeDeleted = new ArrayList<SpaceObject>();

		for (SpaceObject overlapingObject : overlapingObjects) {
			toBeDeleted.addAll(handleDeletion(object, overlapingObject));
		}

		if (!toBeDeleted.isEmpty()) this.objects = getRemainingObjects(toBeDeleted);
	}

	private ArrayList<SpaceObject> handleDeletion(SpaceObject object, SpaceObject overlapingObject) {

		if (object.shouldBeDeletedIfOverlaps(overlapingObject)) {
			ArrayList<SpaceObject> delete = new ArrayList<SpaceObject>();

			this.score += object.getPoints() + overlapingObject.getPoints();
			delete.add(object);
			delete.add(overlapingObject);

			return delete;
		} else {
			return new ArrayList<SpaceObject>();
		}
	}

	private ArrayList<SpaceObject> getRemainingObjects(SpaceObject deleteIt) {
		ArrayList<SpaceObject> list = new ArrayList<SpaceObject>();

		for (SpaceObject object : this.objects) {
			if (deleteIt != object) list.add(object);
		}

		return list;
	}

	private ArrayList<SpaceObject> getRemainingObjects(ArrayList<SpaceObject> toBeDeleted) {
		ArrayList<SpaceObject> remainingObjects = new ArrayList<SpaceObject>();
		for (SpaceObject object : this.objects) {
			if (shouldNotBeDeleted(object, toBeDeleted)) {
				remainingObjects.add(object);
			}
		}
		return remainingObjects;
	}

	private boolean shouldNotBeDeleted(SpaceObject object, ArrayList<SpaceObject> toBeDeleted) {

		boolean deleteIt = true;

		for (SpaceObject toDelete : toBeDeleted) {
			if (object == toDelete) {
				deleteIt = false;
			}
		}

		return deleteIt;
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
