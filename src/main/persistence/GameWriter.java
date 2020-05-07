package persistence;

import model.Game;
import model.Board;
import model.ChessPiece;
import model.Tile;
import model.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Class that assists in saving game state to a JSON file that is able to be loaded on run
public class GameWriter {

    // MODIFIES: File with the value of targetFileLocation as a name
    // EFFECTS: Saves the state of the game by converting player data to json and writing it to a file, returns true
    // if successful, false if an IOException occurs and prints error
    public static boolean saveGame(Game game, String targetFileLocation) {
        // Source: https://www.codevscolor.com/java-write-json-to-file/
        // https://howtodoinjava.com/library/json-simple-read-write-json-examples/#write-json-file
        // https://mkyong.com/java/json-simple-example-read-and-write-json/
        try (FileWriter file = new FileWriter(targetFileLocation)) {
            Board board = game.getBoard();
            JSONObject saveObject = new JSONObject();
            saveObject.put("pieces", createPieceData(board));
            saveObject.put("players", createPlayerData(game.getPlayers()));
            file.write(saveObject.toJSONString());
            file.close();
            return true;
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    // REQUIRES: Player pieces must not be 0
    // EFFECTS: Returns a JSONArray that contains ChessPiece field data of all of the player's chess pieces
    public static JSONArray createPieceData(Board b) {
        JSONArray pieceArray = new JSONArray();
        for (Tile t : b.getBoardTiles()) {
            if (t.isOccupied()) {
                ChessPiece cp = t.getOccupyingPiece();
                JSONObject pieceObject = new JSONObject();
                pieceObject.put("ID", cp.getPieceID());
                pieceObject.put("location", cp.getCurrentPosition());
                pieceObject.put("teamColour", cp.getColour());
                pieceObject.put("type", cp.getPieceType());
                pieceArray.add(pieceObject);
            }
        }
        return pieceArray;
    }

    // REQUIRES: Player objects within array should have all their fields without null values
    // EFFECTS: Creates and returns a JSONArray that contains JSONObjects with all of the player's fields within.
    public static JSONArray createPlayerData(ArrayList<Player> players) {
        JSONArray toSave = new JSONArray();
        for (Player p : players) {
            JSONObject playerDataObject = new JSONObject();
            playerDataObject.put("name", p.getName());
            playerDataObject.put("isPlayersTurn", p.getPlayersTurn());
            playerDataObject.put("teamColour", p.getTeamColour());
            toSave.add(playerDataObject);
        }
        return toSave;
    }

}
