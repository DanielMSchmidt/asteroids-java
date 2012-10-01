package model;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Asteroid extends SpaceObject {

	/**
	 * Constructor for Asteroid
	 * 
	 * @see SpaceObject
	 */
	protected Asteroid(Point2D position, float size, Point2D direction) {
		super(position, direction, size);
	}

	public int getPoints() {
		int normalSize = 30;
		int speedPoints = (int) Math.round((Math.abs(this.direction.getX()) + Math.abs(this.direction.getY())) / 5);
		int sizePoints = Math.round(Math.abs(normalSize - this.size) / 10);
		return speedPoints + sizePoints + 1;
	}

	public Printable getPrintable() {
		return new Printable("asteroid", this.position, this.size, 0);
	}

	public void move(Dimension resolution, ArrayList<SpaceObject> objects) {
		movePhysicallyCorrect(resolution, objects);
	}

	public boolean shouldBeDeletedIfOverlaps(ArrayList<SpaceObject> overlappingObjects) {
		return classIsInList("model.Shot", overlappingObjects);
	}

	public boolean shouldBeDeletedAsItCrashsWithWall(Dimension resolution) {
		return false;
	}

}
