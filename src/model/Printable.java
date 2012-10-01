package model;

import java.awt.geom.Point2D;

/**
 * Printable is an Exchangetype for printing SpaceObjects
 * 
 * @author danielschmidt
 * 
 */
public class Printable {
	String type;
	Point2D position;
	float size;
	int alignment;

	/**
	 * constructor for printable
	 * 
	 * @param type
	 *            type of object, which should be printed
	 * @param position
	 *            position of the object, which should be printed
	 * @param size
	 *            the size of the object, which should be printed
	 * @param alignment
	 *            alignment of the object, which should be printed
	 */
	protected Printable(String type, Point2D position, float size, int alignment) {
		this.type = type;
		this.position = position;
		this.size = size;
		this.alignment = alignment;
	}

	/**
	 * getter for the type of the object
	 * 
	 * @return the type of the object
	 */
	public String getType() {
		return type;
	}

	/**
	 * getter for the position of the object
	 * 
	 * @return the position of the object
	 */
	public Point2D getPosition() {
		return position;
	}

	/**
	 * getter for the size of the object
	 * 
	 * @return the size of the object
	 */
	public float getSize() {
		return size;
	}

	/**
	 * getter for the alignment of the object
	 * 
	 * @return the alignment of the object
	 */
	public int getAlignment() {
		return alignment;
	}
}