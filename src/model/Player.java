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
	}

	public Printable getPrintable() {
		return new Printable("player", this.position, this.size, this.alignment);
	}

	public int getPoints() {
		return 0;
	}

	public void move(Dimension resolution, ArrayList<SpaceObject> objects) {
		movePhysicallyCorrect(resolution, objects);
	}

	public void setAlignment(int i) {
		//TODO Maybe inline newAlignment?
		int formerAlignment = this.alignment;
		
		int newAlignment = (i % 360);
		if (newAlignment < 0){
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

}