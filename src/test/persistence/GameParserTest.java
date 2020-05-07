package test.persistence;

import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Tests for GameParser class
public class GameParserTest {

    Game testGame;
    JSONArray pieceJsonArray;
    JSONArray playerJsonArray;


    @BeforeEach
     public void setup () {
        pieceJsonArray = new JSONArray();
        playerJsonArray = new JSONArray();

        JSONObject queenObject = new JSONObject();
        queenObject.put("location", "D7");
        queenObject.put("teamColour", "black");
        queenObject.put("ID", "queen1");
        queenObject.put("type", "queen");
        pieceJsonArray.add(queenObject);

        JSONObject bishopObject = new JSONObject();
        bishopObject.put("location", "D6");
        bishopObject.put("teamColour", "white");
        bishopObject.put("ID", "bishop1");
        bishopObject.put("type", "bishop");
        pieceJsonArray.add(bishopObject);

        JSONObject playerOneObject = new JSONObject();
        playerOneObject.put("isPlayersTurn", false);
        playerOneObject.put("name", "abc");
        playerOneObject.put("teamColour", "white");
        playerJsonArray.add(playerOneObject);

        JSONObject playerTwoObject = new JSONObject();
        playerTwoObject.put("isPlayersTurn", true);
        playerTwoObject.put("name", "def");
        playerTwoObject.put("teamColour", "black");
        playerJsonArray.add(playerTwoObject);
    }

    @Test
    public void testParseGameDataValid() {
        String testSaveLocation = "./data/testReadData.json";
        testGame = GameParser.parseGameData(testSaveLocation);
        assertNotNull(testGame);

        assertNotNull(testGame.getPlayers());
        assertNotNull(testGame.getBoard());
        assertNotNull(testGame.getActivePlayer());
        assertEquals(2, testGame.getPlayers().size());
        assertEquals("def", testGame.getActivePlayer().getName());
    }

    @Test
    public void testParseGameDataInvalid() {
        String invalidSaveLocation = "./data/hello.json";
        assertNull(GameParser.parseGameData(invalidSaveLocation));
    }

    @Test
    public void testGeneratePlayerArrayList() {
        ArrayList<Player> players = GameParser.generatePlayerArrayList(playerJsonArray);

        Player playerOne = players.get(0);
        Player playerTwo = players.get(1);

        assertEquals("abc", playerOne.getName());
        assertEquals("def", playerTwo.getName());
        assertEquals("white", playerOne.getTeamColour());
        assertEquals("black", playerTwo.getTeamColour());
        assertFalse(playerOne.getPlayersTurn());
        assertTrue(playerTwo.getPlayersTurn());
    }

    @Test
    public void testGenPiece() {
        ChessPiece testPiece = GameParser.genPiece("bishop", "bishop1", "C1", "white");
        assertEquals(model.Bishop.class, testPiece.getClass());
        assertEquals("bishop", testPiece.getPieceType());
        assertEquals("bishop1", testPiece.getPieceID());
        assertEquals("C1", testPiece.getCurrentPosition());
        assertEquals("white", testPiece.getColour());
    }

    @Test
    public void testGenChessPieceArrayList() {
        ArrayList<ChessPiece> pieceArray = GameParser.genChessPieceArrayList(pieceJsonArray);
        assertEquals(2, pieceArray.size());
    }

//    @Test
//    public void testLoadPlayerPieces() {
//        testPlayer = new Player();
//        testPlayer.setTeamColour("black");
//
//        GameParser.loadPlayerPieces(testPlayer, createTestJsonArray());
//        assertEquals(3, testPlayer.getPieces().size());
//
//        // https://stackoverflow.com/questions/10531513/how-to-identify-object-types-in-java
//        assertEquals(Pawn.class, testPlayer.getPieces().get(0).getClass());
//        assertEquals("pawn1", testPlayer.getPieces().get(0).getPieceID());
//        assertEquals("A2", testPlayer.getPieces().get(0).getCurrentPosition());
//
//        assertEquals(Queen.class, testPlayer.getPieces().get(1).getClass());
//        assertEquals("queen1", testPlayer.getPieces().get(1).getPieceID());
//        assertEquals("E1", testPlayer.getPieces().get(1).getCurrentPosition());
//
//        assertEquals(Bishop.class, testPlayer.getPieces().get(2).getClass());
//        assertEquals("bishop1", testPlayer.getPieces().get(2).getPieceID());
//        assertEquals("C1", testPlayer.getPieces().get(2).getCurrentPosition());
//
//    }
}
