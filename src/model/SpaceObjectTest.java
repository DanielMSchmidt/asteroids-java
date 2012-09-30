package model;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class SpaceObjectTest {
	private static final Dimension RESOLUTION = new Dimension(640, 320);
	Player player1;

	@Before
	public void setUp() {
		player1 = new Player("Tester", new Point2D.Double(100, 100));
	}

	@Test
	public void test_that_both_players_dont_collide() {
		Player player2 = new Player("Tester2", new Point2D.Double(200, 200));

		assertEquals(false, player1.overlap(player2));
	}

	@Test
	public void test_that_player_collide_with_themself() {
		assertEquals(true, player1.overlap(player1));
	}

	@Test
	public void test_that_a_player_near_another_collides() {
		Player player2 = new Player("Tester2", new Point2D.Double(110, 110));
		assertEquals(true, player1.overlap(player2));
	}

	@Test
	public void test_that_overlapingObjects_works_with_no_overlapping_element() {
		ArrayList<SpaceObject> objects = new ArrayList<SpaceObject>();
		objects.add(new Asteroid(new Point2D.Double(500, 500), 10, new Point2D.Double()));

		assertEquals(new ArrayList<SpaceObject>(), player1.overlapingObjects(objects));
	}

	@Test
	public void test_that_overlapingObjects_works_with_one_overlapping_element() {
		ArrayList<SpaceObject> objects = new ArrayList<SpaceObject>();
		Asteroid asteroid = new Asteroid(new Point2D.Double(100, 100), 10, new Point2D.Double());
		objects.add(asteroid);

		assertTrue(player1.overlapingObjects(objects).contains(asteroid));
	}

	@Test
	public void test_that_overlapingObjects_returns_all_overlapping_element() {
		ArrayList<SpaceObject> objects = new ArrayList<SpaceObject>();
		Asteroid asteroid1 = new Asteroid(new Point2D.Double(110, 110), 100, new Point2D.Double());
		Asteroid asteroid2 = new Asteroid(new Point2D.Double(110, 110), 100, new Point2D.Double());
		objects.add(asteroid1);
		objects.add(asteroid2);

		assertTrue(player1.overlapingObjects(objects).contains(asteroid1));
		assertTrue(player1.overlapingObjects(objects).contains(asteroid2));
	}

	@Test
	public void test_that_overlapingObjects_returns_only_overlapping_element() {
		ArrayList<SpaceObject> objects = new ArrayList<SpaceObject>();
		Asteroid asteroid0 = new Asteroid(new Point2D.Double(110, 110), 100, new Point2D.Double());
		Asteroid asteroid1 = new Asteroid(new Point2D.Double(90, 90), 10, new Point2D.Double());
		Asteroid asteroid2 = new Asteroid(new Point2D.Double(510, 510), 10, new Point2D.Double());
		objects.add(asteroid0);
		objects.add(asteroid1);
		objects.add(asteroid2);

		assertTrue(player1.overlapingObjects(objects).contains(asteroid0));
		assertTrue(player1.overlapingObjects(objects).contains(asteroid1));
	}

	// FIXME Fix Tests
	@Test
	public void test_bounce_with_top_wall() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(5, 10), 5, new Point2D.Double(10, -5));
		ArrayList<Boolean> a = new ArrayList<Boolean>();
		a.add(true);
		a.add(false);
		a.add(false);
		a.add(false);

		asteroid.bounceWithWall(RESOLUTION);

		assertEquals(new Point2D.Double(10, 5), asteroid.getDirection());
	}

	@Test
	public void test_bounce_with_right_wall() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(10, RESOLUTION.getWidth() -5), 5, new Point2D.Double(10, 5));
		ArrayList<Boolean> a = new ArrayList<Boolean>();
		a.add(false);
		a.add(true);
		a.add(false);
		a.add(false);

		asteroid.bounceWithWall(RESOLUTION);

		assertEquals(new Point2D.Double(-10, 5), asteroid.getDirection());
	}

	@Test
	public void test_bounce_with_bottom_wall() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(RESOLUTION.getHeight() -5, 10), 5, new Point2D.Double(10, 5));
		ArrayList<Boolean> a = new ArrayList<Boolean>();
		a.add(false);
		a.add(false);
		a.add(true);
		a.add(false);

		asteroid.bounceWithWall(RESOLUTION);

		assertEquals(new Point2D.Double(10, -5), asteroid.getDirection());
	}

	@Test
	public void test_bounce_with_left_wall() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(10, 5), 5, new Point2D.Double(-10, 5));
		ArrayList<Boolean> a = new ArrayList<Boolean>();
		a.add(false);
		a.add(false);
		a.add(false);
		a.add(true);

		asteroid.bounceWithWall(RESOLUTION);

		assertEquals(new Point2D.Double(10, 5), asteroid.getDirection());
	}

	@Test
	public void test_bounce_with_two_walls() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(10, 10), 5, new Point2D.Double(-10, 5));
		ArrayList<Boolean> a = new ArrayList<Boolean>();
		a.add(false);
		a.add(false);
		a.add(true);
		a.add(true);

		asteroid.bounceWithWall(RESOLUTION);

		assertEquals(new Point2D.Double(10, -5), asteroid.getDirection());
	}

	@Test
	public void test_bounce_with_two_impossible_walls() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(10, 10), 5, new Point2D.Double(10, 5));
		ArrayList<Boolean> a = new ArrayList<Boolean>();
		a.add(false);
		a.add(true);
		a.add(false);
		a.add(true);

		try {
			asteroid.bounceWithWall(RESOLUTION);
			fail("No Exception caught");
		}
		catch (Exception e) {
			assertEquals("Impossible bounce detected", e.getMessage());
		}
	}

	@Test
	public void test_if_non_static_bounce_let_both_change_directions() {
		player1.setDirection(new Point2D.Double(1, 1));
		Player player2 = new Player("player2", new Point2D.Double(0, 0));
		player2.setDirection(new Point2D.Double(-1, -1));

		player1.bounce(player2);

		assertEquals(new Point2D.Double(-1, -1), player1.getDirection());
		assertEquals(new Point2D.Double(1, 1), player2.getDirection());
	}

	@Test
	public void test_if_non_static_bounce_let_both_exchange_speeds() {
		player1.setDirection(new Point2D.Double(10, 1));
		Player player2 = new Player("player2", new Point2D.Double(0, 0));
		player2.setDirection(new Point2D.Double(-1, -5));

		player1.bounce(player2);

		assertEquals(new Point2D.Double(-1, -5), player1.getDirection());
		assertEquals(new Point2D.Double(10, 1), player2.getDirection());
	}

	@Test
	public void test_if_non_static_bounce_and_same_direction_let_the_slower_be_fastened() {
		player1.setDirection(new Point2D.Double(10, 1));
		Player player2 = new Player("player2", new Point2D.Double(0, 0));
		player2.setDirection(new Point2D.Double(1, 5));

		player1.bounce(player2);

		assertEquals(new Point2D.Double(1, 5), player1.getDirection());
		assertEquals(new Point2D.Double(10, 1), player2.getDirection());
	}

	@Test
	public void test_that_move_adds_the_direction_on_object() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(100, 100), 10, new Point2D.Double(10, 1));

		asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

		assertEquals(new Point2D.Double(110, 101), asteroid.getPosition());
	}

	@Test
	public void test_that_move_doesnt_move_over_the_border() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(10, 10), 10, new Point2D.Double(-10, -1));

		asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

		assertTrue(asteroid.getPosition().getX() >= asteroid.size);
		assertTrue(asteroid.getPosition().getY() >= asteroid.size);
		assertTrue(asteroid.getPosition().getX() < 640 - asteroid.size);
		assertTrue(asteroid.getPosition().getY() < 320 - asteroid.size);
	}

	@Test
	public void test_that_move_doesnt_move_over_the_border_on_big_objects() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(110, 110), 100, new Point2D.Double(-30, 0));

		asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

		assertTrue(asteroid.getPosition().getX() >= asteroid.size);
		assertTrue(asteroid.getPosition().getY() >= asteroid.size);
		assertTrue(asteroid.getPosition().getX() < 640 - asteroid.size);
		assertTrue(asteroid.getPosition().getY() < 320 - asteroid.size);
	}

	@Test
	public void test_that_move_bounces_correctly_on_the_top_left_border() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(10, 10), 10, new Point2D.Double(-10, -1));

		asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

		assertEquals(new Point2D.Double(20, 11), asteroid.getPosition());
	}

	@Test
	public void test_that_move_bounces_correctly_on_the_right_border() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(625, 100), 10, new Point2D.Double(10, 0));

		asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

		assertEquals(new Point2D.Double(625, 100), asteroid.getPosition());
	}

	@Test
	public void test_that_move_bounces_correctly_on_the_bottom_border() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(25, 305), 10, new Point2D.Double(10, 10));

		asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

		assertEquals(new Point2D.Double(35, 305), asteroid.getPosition());
	}

	@Test
	public void test_that_move_bounces_correctly_on_the_border_on_big_objects() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(110, 110), 100, new Point2D.Double(-30, 0));

		asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

		assertEquals(new Point2D.Double(120, 110), asteroid.getPosition());
	}

	@Test
	public void test_that_move_changes_the_direction() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(10, 10), 10, new Point2D.Double(-10, -1));

		asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

		assertEquals(new Point2D.Double(10, 1), asteroid.getDirection());
	}

	@Test
	public void test_that_move_changes_the_direction_on_big_objects() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(110, 110), 100, new Point2D.Double(-30, 0));

		asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

		assertEquals(new Point2D.Double(30, 0), asteroid.getDirection());
	}

	@Test
	public void test_that_frequently_calling_move_does_let_the_object_everytime_be_in_space() {
		Asteroid asteroid = new Asteroid(new Point2D.Double(10, 10), 10, new Point2D.Double(-10, -1));
		for (int i = 0; i < 121; i++) {
			asteroid.move(RESOLUTION, new ArrayList<SpaceObject>());

			assertTrue(asteroid.getPosition().getX() >= asteroid.size);
			assertTrue(asteroid.getPosition().getY() >= asteroid.size);
			assertTrue(asteroid.getPosition().getX() <= 640 - asteroid.size);
			assertTrue(asteroid.getPosition().getY() <= 320 - asteroid.size);
		}
	}
}
