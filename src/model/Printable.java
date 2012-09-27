package model;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Printable {
	String type;
	Point2D position;
	float size;
	int alignment;
	
	protected Printable(String type, Point2D position, float size, int alignment) {
		this.type = type;
		this.position = position;
		this.size = size;
		this.alignment = alignment;
	}
	
	public String getType(){
		return type;
	}
	
	public Point2D getPosition(){
		return position;
	}
	
	public float getSize(){
		return size;
	}
	
	public int getAlignment(){
		return alignment;
	}
}