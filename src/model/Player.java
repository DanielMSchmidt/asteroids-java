package model;

import java.awt.Dimension;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Player extends SpaceObject {
	String name;
	int score;
	int alignment;

	/**
	 * @param name
	 * @param score
	 */
	protected Player(String name, Point2D position) {
		super(position, 20);
		this.name = name;
		this.score = 0;
		this.direction = new Point2D.Double();
		this.direction.setLocation(0, -5);
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

	public void setAlignment(int i) {
		int formerAlignment = this.alignment;

		int newAlignment = (i % 360);
		if (newAlignment < 0) {
			newAlignment += 360;
		}

		this.alignment = newAlignment;
		transformDirectionDueToAlignmentChanges(formerAlignment, newAlignment);
	}

	private void transformDirectionDueToAlignmentChanges(int from, int to) {
		this.direction = transformVektorViaAngle(this.direction, to - from);
	}

	public void addAlignment(int deltaAlignment) {
		setAlignment(this.alignment + deltaAlignment);
	}

	@Override
	public boolean shouldBeDeletedIfOverlaps(ArrayList<SpaceObject> overlappingObjects) {
		return classIsInList("model.Asteroid", overlappingObjects) || classIsInList("model.Shot", overlappingObjects);
	}

	@Override
	public boolean shouldBeDeletedAsItCrashsWithWall(Dimension resolution) {
		return false;
	}

}