package model;

import java.awt.Dimension;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Shot extends SpaceObject {
	int alignment;
	
	protected Shot(Point2D position, Point2D direction) {
		super(position, 8);
		this.direction = direction;
	}

	public Shot(Point2D position, int shotSpeed, int alignment, Dimension resolution) {
		super(new Point2D.Double(position.getX(), position.getY()), 5);
		this.direction = transformVektorViaAngle(new Point2D.Double(0, -shotSpeed),
				alignment);
		this.alignment = alignment;
		this.move(resolution, new ArrayList<SpaceObject>());
		this.move(resolution, new ArrayList<SpaceObject>());
	}

	public Printable getPrintable() {
		return new Printable("shot.png", this.position, this.size, this.alignment);
	}

	public int getPoints() {
		return 0;
	}

	public void move(Dimension resolution, ArrayList<SpaceObject> objects) {
		if (!this.getBorders(resolution).contains(true)) {
			movePhysicallyCorrect(resolution, objects);
		}
	}

	@Override
    public boolean shouldBeDeletedIfOverlaps(ArrayList<SpaceObject> overlappingObjects) {
		return classIsInList("model.Asteroid", overlappingObjects) || classIsInList("model.Shot", overlappingObjects);
    }

	@Override
    public boolean shouldBeDeletedAsItCrashsWithWall(Dimension resolution) {
		return this.getBorders(resolution).contains(true);
    }

}
