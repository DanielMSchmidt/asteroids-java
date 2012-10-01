package model;

import java.awt.Dimension;

import java.awt.geom.Point2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Player extends SpaceObject {
	String name;
	int score;
	int alignment;

	protected Player(String name, Point2D position) {
		super(position, new Point2D.Double(0, -5), 20);
		this.name = name;
		this.score = 0;
		this.alignment = 0;
		this.shouldBounceWithWall = false;
	}

	public Printable getPrintable() {
		return new Printable("player", this.position, this.size, this.alignment);
	}

	public int getPoints() {
		return 0;
	}

	public void move(Dimension resolution, ArrayList<SpaceObject> objects) {
		movePhysicallyCorrect(resolution, objects);
		jumpIfOnEdge(resolution);
	}

	/**
	 * moves the player on the other side of the map
	 * 
	 * @param resolution
	 *            resolution of the map
	 */
	private void jumpIfOnEdge(Dimension resolution) {
		ArrayList<Boolean> borders = getBorders(resolution);
		if (borders.contains(true)) {
			if (borders.get(3)) {
				position.setLocation(resolution.getWidth() - (size + 1), position.getY());
			}
			if (borders.get(2)) {
				position.setLocation(position.getX(), (size + 1));
			}
			if (borders.get(0)) {
				position.setLocation(position.getX(), resolution.getHeight() - (size + 1));
			}
			if (borders.get(1)) {
				position.setLocation((size + 1), position.getY());
			}
		}
	}

	/**
     * adds new alignment
     * 
     * @param deltaAlignment
     *            alignment which should be added
     */
    public void addAlignment(int deltaAlignment) {
    	setAlignment(this.alignment + deltaAlignment);
    }

	/**
	 * sets the player alignment and transforms the direction
	 * 
	 * @param i
	 *            new angle
	 */
	public void setAlignment(int i) {
		int formerAlignment = this.alignment;

		int newAlignment = (i % 360);
		if (newAlignment < 0) {
			newAlignment += 360;
		}

		this.alignment = newAlignment;
		transformDirectionDueToAlignmentChanges(formerAlignment, newAlignment);
	}

	/**
	 * transforms the direction
	 * 
	 * @param from
	 *            old alignment
	 * @param to
	 *            new alignment
	 */
	private void transformDirectionDueToAlignmentChanges(int from, int to) {
		this.direction = transformVektorViaAngle(this.direction, to - from);
	}

	@Override
	public boolean shouldBeDeletedIfOverlaps(ArrayList<SpaceObject> overlappingObjects) {
		return classIsInList("model.Asteroid", overlappingObjects) || classIsInList("model.Shot", overlappingObjects);
	}

	@Override
	public boolean shouldBeDeletedAsItCrashsWithWall(Dimension resolution) {
		return false;
	}

	/**
	 * checks if this object is within the range
	 * 
	 * @param object
	 *            object to be checked
	 * @param range
	 *            the radius in which it should be
	 * @return <code>true</code> if the object is in the radius
	 */
	public boolean isWithinRange(SpaceObject object, float range) {
		float originalSize = this.size;
		this.size = range;

		boolean isInRadius = this.overlap(object);

		this.size = originalSize;
		return isInRadius;
	}

}