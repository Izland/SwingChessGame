package test.persistence;

import model.Board;
import model.ChessPiece;
import model.Game;
import model.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Tests for GameWriter class
public class GameWriterTest {

    private ArrayList<Player> playersToWrite;
    private Game gameToWrite;
    private Game testGame;

    @BeforeEach
    public void setup() {
        playersToWrite = new ArrayList<>();
        playersToWrite.add(new Player("white", "Joe", true));
        playersToWrite.add(new Player("black", "Mama", false));
        gameToWrite = new Game();
    }

    @Test
    public void saveGameTestValid() {
        String testSaveLocation = "./data/testWriteData.json";
        assertTrue(GameWriter.saveGame(gameToWrite, testSaveLocation));
        testGame = GameParser.parseGameData(testSaveLocation);
        assertNotNull(testGame);
        assertNotNull(testGame.getPlayers());
        assertNotNull(testGame.getBoard());
        assertNotNull(testGame.getActivePlayer());
    }

    @Test
    public void testSaveGameTestInvalid() {
        String invalidSaveLocation = "./data/testDirectory";
        assertFalse(GameWriter.saveGame(testGame, invalidSaveLocation));
    }

    @Test
    public void testCreatePlayerData() {
        JSONArray playerJsonArray = GameWriter.createPlayerData(playersToWrite);
        JSONObject playerOneJsonObject = (JSONObject) playerJsonArray.get(0);
        JSONObject playerTwoJsonObject = (JSONObject) playerJsonArray.get(1);

        assertEquals("Joe", playerOneJsonObject.get("name"));
        assertEquals("Mama", playerTwoJsonObject.get("name"));
        assertEquals("white", playerOneJsonObject.get("teamColour"));
        assertEquals("black", playerTwoJsonObject.get("teamColour"));
        assertEquals(true, playerOneJsonObject.get("isPlayersTurn"));
        assertEquals(false, playerTwoJsonObject.get("isPlayersTurn"));
    }

    @Test
    public void createPieceDataTest() {
        JSONArray pieceJsonArray = GameWriter.createPieceData(gameToWrite.getBoard());
        assertEquals(32, pieceJsonArray.size());

        // Check for non existent piece
        for (Object o : pieceJsonArray) {
            JSONObject pieceJsonObject =(JSONObject) o;
            if(pieceJsonObject.get("location").equals("D5")) {
                fail("There should not be a piece with that location");
            }
        }

        // Check for existent piece
        boolean pieceNotFound = true;
        for (Object o : pieceJsonArray) {
            JSONObject pieceJsonObject =(JSONObject) o;
            if (pieceJsonObject.get("location").equals("B1")) {
                pieceNotFound = false;
            }
        }
        if (pieceNotFound) {
            fail("Should have been a piece at B1");
        }
    }
}
