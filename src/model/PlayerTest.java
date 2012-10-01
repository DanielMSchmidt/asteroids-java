package model;

import static org.junit.Assert.*;
import java.awt.geom.Point2D;
import org.junit.*;

public class PlayerTest {
	Player player1;

	@Before
	public void setUp() {
		player1 = new Player("Tester", new Point2D.Double(100, 100));
	}

	@Test
	public void test_that_set_alignment_sets_the_alignment() {
		player1.setAlignment(300);

		assertEquals(300, player1.alignment);
	}

	@Test
	public void test_that_set_alignment_sets_the_alignment_under_360() {
		player1.setAlignment(370);

		assertEquals(10, player1.alignment);
	}

	@Test
	public void test_that_the_alignment_changes_the_direction_after_a_rotation_90_right() {
		Player player2 = new Player("player2", new Point2D.Double(100, 100));
		player2.direction = new Point2D.Double(3, 5);

		player2.setAlignment(90);

		assertEquals(new Point2D.Double(-5, 3), roundPoint(player2.direction));
	}

	@Test
	public void test_that_the_alignment_changes_the_direction_after_a_rotation_180_right() {
		Player player2 = new Player("player2", new Point2D.Double(100, 100));
		player2.direction = new Point2D.Double(3, 5);

		player2.setAlignment(180);

		assertEquals(new Point2D.Double(-3, -5), roundPoint(player2.direction));
	}

	@Test
	public void test_that_the_alignment_changes_the_direction_after_a_rotation_90_left() {
		Player player2 = new Player("player2", new Point2D.Double(100, 100));
		player2.direction = new Point2D.Double(3, 5);

		player2.setAlignment(270);

		assertEquals(new Point2D.Double(5, -3), roundPoint(player2.direction));
	}

	private Point2D roundPoint(Point2D direction) {
		return new Point2D.Double((int) Math.round(direction.getX()), (int) Math.round(direction.getY()));
	}

}
