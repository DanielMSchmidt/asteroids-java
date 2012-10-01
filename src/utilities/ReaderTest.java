package utilities;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

import model.Game;

import org.junit.Before;
import org.junit.Test;

public class ReaderTest {
	String OPTIONSLOCATION;
	String HIGHSCORELOCATION;
	String GAMELOCATION;
	ArrayList<String> arraylist;
	ArrayList<String> optionlist;
	Reader reader;
	OptionsReader optionsReader;
	HighscoreReader highscoreReader;
	ArrayList<String> highscorelist;
	GameReader gameReader;

	@Before
	public void setUp() throws Exception {
		initializeLists();
		OPTIONSLOCATION = "Test";
		HIGHSCORELOCATION = "TestHighscore";
		GAMELOCATION = "Testsavegame";
		reader = new Reader(OPTIONSLOCATION);
		optionsReader = new OptionsReader(OPTIONSLOCATION);
		highscoreReader = new HighscoreReader(HIGHSCORELOCATION);
		gameReader = new GameReader(GAMELOCATION);
	}

	private void initializeLists() {
		arraylist = new ArrayList<String>();
		arraylist.add("abc");
		arraylist.add("cde");
		arraylist.add("efg");
		optionlist = new ArrayList<String>();
		optionlist.add("playername");
		optionlist.add("123");
		optionlist.add("456");
		highscorelist = new ArrayList<String>();
		highscorelist.add("player1");
		highscorelist.add("500");
		highscorelist.add("player2");
		highscorelist.add("400");
		highscorelist.add("player3");
		highscorelist.add("300");
		highscorelist.add("player4");
		highscorelist.add("200");
	}

	@Test
	public void test_flat_with_empty_list() {
		assertEquals("", reader.flat(new ArrayList<String>()));
	}

	@Test
	public void test_flat_with_one_element_list() {
		ArrayList<String> a = new ArrayList<String>();

		a.add("test");

		assertEquals("test", reader.flat(a));
	}

	@Test
	public void test_flat_with_multiple_element_list() {
		assertEquals("abc,cde,efg", reader.flat(arraylist));
	}

	@Test
	public void test_data_raw_read_and_write() throws IOException {
		reader.writeData(arraylist, OPTIONSLOCATION);

		String a = reader.readRawData(OPTIONSLOCATION);

		assertEquals("abc,cde,efg", a);
	}

	@Test
	public void test_data_raw_read_with_empty_data() {
		String a = reader.readRawData("NotTest");

		assertEquals("", a);
	}

	@Test
	public void test_data_read_and_write() throws IOException {
		reader.writeData(arraylist, OPTIONSLOCATION);

		ArrayList<String> a = reader.readData(OPTIONSLOCATION);

		assertEquals(arraylist, a);
	}

	@Test
	public void test_raw_data_deletion() throws IOException {
		reader.writeData(arraylist, OPTIONSLOCATION);

		ArrayList<String> a = reader.readData(OPTIONSLOCATION);
		assertEquals(arraylist, a);
		reader.deleteFile(OPTIONSLOCATION);
		String b = reader.readRawData(OPTIONSLOCATION);

		assertEquals("", b);
	}

	@Test
	public void test_data_deletion() throws IOException {
		reader.writeData(arraylist, OPTIONSLOCATION);

		ArrayList<String> a = reader.readData(OPTIONSLOCATION);
		assertEquals(arraylist, a);
		reader.deleteFile(OPTIONSLOCATION);
		ArrayList<String> b = reader.readData(OPTIONSLOCATION);
		ArrayList<String> c = new ArrayList<String>();

		assertEquals(c, b);
	}

	@Test
	public void test_add_data() throws IOException {
		reader.writeData(arraylist, OPTIONSLOCATION);
		ArrayList<String> a = arraylist;
		ArrayList<String> addition = new ArrayList<String>();
		addition.add("test");
		addition.add("string");

		a.addAll(addition);
		reader.addData(addition, OPTIONSLOCATION);
		ArrayList<String> b = reader.readData(OPTIONSLOCATION);

		assertEquals(a, b);
	}

	@Test
	public void test_add_data_to_empty_data() throws IOException {
		ArrayList<String> a = arraylist;
		reader.deleteFile(OPTIONSLOCATION);

		reader.addData(arraylist, OPTIONSLOCATION);
		ArrayList<String> b = reader.readData(OPTIONSLOCATION);

		assertEquals(a, b);
	}

	public void test_add_empty_data() throws IOException {
		int oldSize = reader.readData(OPTIONSLOCATION).size();
		reader.addData(arraylist, OPTIONSLOCATION);
		int newSize = reader.readData(OPTIONSLOCATION).size();

		assertTrue(newSize == oldSize);
	}

	@Test
	public void test_non_param_functions() throws IOException {
		reader.writeData(arraylist);
		ArrayList<String> a = reader.readData();
		assertEquals(arraylist, a);
		reader.deleteFile();
		reader.addData(arraylist);
		assertEquals(arraylist, a);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_validate_options() throws IOException {
		ArrayList<String> tooLongOptionsList = (ArrayList<String>) optionlist.clone();
		tooLongOptionsList.add("senseless String");
		ArrayList<String> wrongTypeOptionsList = (ArrayList<String>) optionlist.clone();
		wrongTypeOptionsList.set(2, "not an integer");

		assertTrue(optionsReader.validate(optionlist));
		assertFalse(optionsReader.validate(wrongTypeOptionsList));
		assertFalse(optionsReader.validate(tooLongOptionsList));

	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_save_with_validatopm() throws IOException {
		ArrayList<String> tooLongOptionsList = (ArrayList<String>) optionlist.clone();
		tooLongOptionsList.add("senseless String");
		ArrayList<String> wrongTypeOptionsList = (ArrayList<String>) optionlist.clone();
		wrongTypeOptionsList.set(2, "not an integer");

		optionsReader.deleteFile();

		try {
			optionsReader.save(optionlist);
			assertEquals(optionsReader.readData(), optionlist);

			optionsReader.save(wrongTypeOptionsList);
			assertEquals(optionsReader.readData(), optionlist);

			optionsReader.save(tooLongOptionsList);
			assertEquals(optionsReader.readData(), optionlist);
		}
		catch (Exception e) {
		}

	}

	@Test
	public void test_if_options_are_initialized() {
		OptionsReader secondReader = new OptionsReader("second_Location");
		assertTrue(secondReader.validate(secondReader.readData()));
		secondReader.deleteFile();
		assertFalse(secondReader.validate(secondReader.readData()));
	}

	@Test
	public void test_get_Highscore() throws IOException {
		highscoreReader.deleteFile();
		assertEquals(highscoreReader.getHighscore(), new ArrayList<String>());

		highscoreReader.writeData(highscorelist);
		assertEquals(highscoreReader.getHighscore(), highscorelist);

		highscoreReader.deleteFile();
		highscoreReader.writeData(arraylist);

		assertEquals(highscoreReader.getHighscore(), new ArrayList<Integer>());
	}

	@Test
	public void test_highscore_validation() throws IOException {
		ArrayList<String> a = new ArrayList<String>();
		a.add("Right");
		a.add("Wrong");

		ArrayList<String> b = new ArrayList<String>();
		b.add("Wrong");
		b.add("Number");
		b.add("of Elements");

		assertFalse(highscoreReader.validate(a));
		assertFalse(highscoreReader.validate(b));
		assertTrue(highscoreReader.validate(highscorelist));

	}

	@Test
	public void test_add_score() {
		highscoreReader.deleteFile();
		highscoreReader.addScore(highscorelist);
		highscoreReader.addScore(highscorelist);

		assertEquals(highscoreReader.readData().size(), highscorelist.size() * 2);

		highscoreReader.addScore(arraylist);
		assertEquals(highscoreReader.readData().size(), highscorelist.size() * 2);
	}

	@Test
	public void test_merge_scores() {
		@SuppressWarnings("unchecked")
		ArrayList<String> newHighscores = (ArrayList<String>) highscorelist.clone();
		ArrayList<String> mixed = new ArrayList<String>();

		for (int i = 1; i < highscorelist.size(); i = i + 2) {
			newHighscores.set(i, String.valueOf(Integer.valueOf(newHighscores.get(i)) + 1));
			mixed.add(newHighscores.get(i - 1));
			mixed.add(newHighscores.get(i));
			mixed.add(highscorelist.get(i - 1));
			mixed.add(highscorelist.get(i));
		}

		assertEquals(mixed, highscoreReader.mergeScores(newHighscores, highscorelist));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_merge_scores_for_equal_lists() {
		ArrayList<String> newHighscores = (ArrayList<String>) highscorelist.clone();
		ArrayList<String> mixed = new ArrayList<String>();

		for (int i = 1; i < highscorelist.size(); i = i + 2) {
			mixed.add(newHighscores.get(i - 1));
			mixed.add(newHighscores.get(i));
			mixed.add(highscorelist.get(i - 1));
			mixed.add(highscorelist.get(i));
		}

		assertEquals(mixed, highscoreReader.mergeScores(newHighscores, highscorelist));
	}

	@Test
	public void test_get_short_highscore() {
		highscoreReader.deleteFile();
		for (int i = 0; i < HighscoreReader.MAXVALUES * 2; i++) {
			highscoreReader.addData(highscorelist);
			ArrayList<String> highscore = highscoreReader.getShortHighscore();

			assertTrue(highscore.size() <= highscoreReader.getHighscore().size());
			assertTrue(highscore.size() <= HighscoreReader.MAXVALUES);
		}
	}
	
	@Test
	public void test_that_game_reader_saves_empty_game(){
		Game game = new Game("test", new Dimension(100,100), 10, 1);
		
		gameReader.saveGame(game);
        Game loadedGame;
        try {
	        loadedGame = gameReader.loadGame();
	        assertEquals(loadedGame.getPlayername(), game.getPlayername());
	        assertEquals(loadedGame.getAsteroidCount(), game.getAsteroidCount());
        }
        catch (IOException e) {
	        e.printStackTrace();
	        fail("error");
        }
        
		
	}
}
