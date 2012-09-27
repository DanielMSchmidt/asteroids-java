package model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Shot extends SpaceObject {

	protected Shot(Point2D position, Point2D direction) {
		super(position, 8);
		this.direction = direction;
	}

	public Shot(Point2D position, int shotSpeed, int alignment, Dimension resolution) {
		super(new Point2D.Double(position.getX(), position.getY()), 5);
		this.direction = transformVektorViaAngle(new Point2D.Double(0, -shotSpeed),
				alignment);
		
		this.move(resolution, new ArrayList<SpaceObject>());
		this.move(resolution, new ArrayList<SpaceObject>());
	}

	public Printable getPrintable() {
		return new Printable("shot.png", this.position, this.size, 0);
	}

	public int getPoints() {
		return 0;
	}

	public void move(Dimension resolution, ArrayList<SpaceObject> objects) {
		if (!this.getBorders(resolution).contains(true)) {
			movePhysicallyCorrect(resolution, objects);
		}
	}

}
