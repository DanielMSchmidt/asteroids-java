package model;

import java.awt.Dimension;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public abstract class SpaceObject {
	Point2D position;
	float size;
	Point2D direction;

	/**
	 * @param position
	 * @param size
	 * @param imagePath
	 */
	protected SpaceObject(Point2D position, float size) {
		super();
		this.position = position;
		this.size = size;
	}

	public abstract void move(Dimension resolution, ArrayList<SpaceObject> objects);

	protected void movePhysicallyCorrect(Dimension resolution, ArrayList<SpaceObject> objects) {
		ArrayList<Boolean> borders = getBorders(resolution);
		ArrayList<SpaceObject> overlapingObjects = this.overlapingObjects(objects);

		if (borders.contains(true)) {
			this.bounceWithWall(borders, resolution);
		} else if (!overlapingObjects.isEmpty()) {
			bounceWithAsteroids(overlapingObjects);
		} else {
			this.position.setLocation(this.position.getX() + this.direction.getX(), this.position.getY()
			        + this.direction.getY());
		}
	}

	void bounceWithAsteroids(ArrayList<SpaceObject> overlapingObjects) {
		for (SpaceObject overlaper : overlapingObjects) {
			if (overlaper.getClass() == Asteroid.class) {
				this.bounce(overlaper);
			}
		}
		this.position.setLocation(this.position.getX() + this.direction.getX(),
		        this.position.getY() + this.direction.getY());
	}

	ArrayList<Boolean> getBorders(Dimension resolution) {
		ArrayList<Boolean> borders = new ArrayList<Boolean>();
		Point2D nextPosition = new Point2D.Double(this.position.getX() + this.direction.getX(), this.position.getY()
		        + this.direction.getY());
		borders.add((nextPosition.getY() - this.size) < 0); // oben
		borders.add((nextPosition.getX() + this.size) > resolution.width); // rechts
		borders.add((nextPosition.getY() + this.size) > resolution.height); // unten
		borders.add((nextPosition.getX() - this.size) < 0); // links
		return borders;
	}

	public boolean overlap(SpaceObject object) {
		int distance = (int) object.position.distance(this.position);
		if (distance <= (object.size + this.size)) {
			return true;
		}
		return false;
	}

	public ArrayList<SpaceObject> overlapingObjects(ArrayList<SpaceObject> objects) {
		ArrayList<SpaceObject> overlappingObjects = new ArrayList<SpaceObject>();
		for (SpaceObject object : objects) {
			if (object != this) {
				if (this.overlap(object)) {
					overlappingObjects.add(object);
				}
			}
		}
		return overlappingObjects;
	}

	public SpaceObject bounce(SpaceObject object) {
		Point2D a = (Point2D) this.getDirection().clone();
		this.setDirection((Point2D) object.getDirection().clone());
		object.setDirection(a);

		return this;
	}

	public void bounceWithWall(ArrayList<Boolean> borders, Dimension resolution) {
		// FIXME Ugly code, but no other possibility seen
		int leftOrRightSide = 0;
		int topOrBottomSide = 0;
		for (int i = 0; i < borders.size(); i++) {
			if (borders.get(i)) {
				switch (i) {
					case 0:
						this.position.setLocation(this.position.getX(),
						        Math.abs((this.position.getY() - this.size + this.direction.getY())) + this.size);
						this.direction.setLocation(this.direction.getX(), Math.abs(this.direction.getY()));

						topOrBottomSide++;
					break;
					case 1:
						this.position.setLocation(resolution.width
						        - (resolution.width - (this.position.getX() + this.direction.getX() - this.size)),
						        this.position.getY());
						this.direction.setLocation(-1 * Math.abs(this.direction.getX()), this.direction.getY());

						leftOrRightSide++;
					break;
					case 2:
						this.position.setLocation(this.position.getX(), resolution.height
						        - (resolution.height - (this.position.getY() + this.direction.getY() - this.size)));
						this.direction.setLocation(this.direction.getX(), -1 * Math.abs(this.direction.getY()));

						topOrBottomSide++;
					break;
					case 3:
						this.position.setLocation(Math.abs(this.position.getX() - this.size + this.direction.getX())
						        + this.size, this.position.getY());
						this.direction.setLocation(Math.abs(this.direction.getX()), this.direction.getY());

						leftOrRightSide++;
					break;
				}

			}
		}

		if (leftOrRightSide > 1 || topOrBottomSide > 1) {
			throw new RuntimeException("Impossible bounce detected");
		} else if (leftOrRightSide == 1 && topOrBottomSide == 0) {
			this.position.setLocation(this.position.getX(), this.position.getY() + this.direction.getY());
		} else if (leftOrRightSide == 0 && topOrBottomSide == 1) {
			this.position.setLocation(this.position.getX() + this.direction.getX(), this.position.getY());
		}
	}

	protected static Point2D transformVektorViaAngle(Point2D direction, int angle) {
		double theta = Math.toRadians(angle);
		double x = (double) direction.getX() * Math.cos(theta)
		        - (double) direction.getY() * Math.sin(theta);
		double y = (double) direction.getX()
		        * Math.sin(theta) + (double) direction.getY() * Math.cos(theta);
		
		Point2D point = new Point2D.Double(x,y);
		return point;
	}

	public Point2D getPosition() {
		return position;
	}

	public float getSize() {
		return size;
	}

	public Point2D getDirection() {
		return direction;
	}

	public SpaceObject setDirection(Point2D direction) {
		this.direction = direction;
		return this;
	}

	public abstract Printable getPrintable();

	public abstract int getPoints();

	public boolean shouldBeDeletedIfOverlaps(SpaceObject object) {
		return true;
	}
}
