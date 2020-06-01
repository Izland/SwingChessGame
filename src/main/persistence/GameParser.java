package persistence;

import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// Reads JSON Files to initialize game state
public class GameParser {

    // REQUIRES: JSONArray should only have two players
    // EFFECTS: Turns JSONArray into ArrayList full of players and returns it
    public static ArrayList<Player> generatePlayerArrayList(Game game, JSONArray players) {
        ArrayList<Player> arrayToReturn = new ArrayList<>();

        for (Object o : players) {
            JSONObject playerObject = (JSONObject) o;
            String name = (String) playerObject.get("name");
            String teamColour = (String) playerObject.get("teamColour");
            boolean isPlayersTurn = (boolean) playerObject.get("isPlayersTurn");
            boolean isAi = (boolean) playerObject.get("isAi");
            Player p;

            if  (isAi) {
                p = new AI(game, teamColour, name, isPlayersTurn);
            } else {
                p = new HumanPlayer(game, teamColour, name, isPlayersTurn);
            }

            arrayToReturn.add(p);
        }
        return arrayToReturn;
    }

    // REQUIRES: pieces must contain JSONObjects with fields "location", "teamColour", "type", and "ID"
    // EFFECTS: Parses JSONArray and creates a new ChessPiece for each object within it
    public static ArrayList<ChessPiece> genChessPieceArrayList(Board board, JSONArray pieces) {
        ArrayList<ChessPiece> arrayToReturn = new ArrayList<>();

        for (Object o : pieces) {
            JSONObject pieceObject = (JSONObject) o;
            String location = (String) pieceObject.get("location");
            String teamColour = (String) pieceObject.get("teamColour");
            String pieceType = (String) pieceObject.get("type");
            String pieceID = (String) pieceObject.get("ID");

            ChessPiece cp = genPiece(board, pieceType, pieceID, location, teamColour);
            arrayToReturn.add(cp);
        }
        return arrayToReturn;
    }

    // REQUIRES: teamColour to be set to "white" or "black"; currentPosition should be unoccupied
    // EFFECTS: Adds a newly created chess piece to the Player's pieces, based on loaded JSON data
    public static ChessPiece genPiece(Board board, String pieceType, String pieceID, String currentPosition, String teamColour) {
        // https://stackoverflow.com/questions/7438612/how-to-remove-the-last-character-from-a-string
        return switch (pieceType) {
            case "pawn" -> new Pawn(board, pieceID, currentPosition, teamColour);
            case "rook" -> new Rook(board, pieceID, currentPosition, teamColour);
            case "bishop" -> new Bishop(board, pieceID, currentPosition, teamColour);
            case "knight" -> new Knight(board, pieceID, currentPosition, teamColour);
            case "king" -> new King(board, currentPosition, teamColour);
            default -> new Queen(board, pieceID, currentPosition, teamColour);
        };
    }

    // REQUIRES: fileLocation should contain json file with game data
    // EFFECTS: Returns Game object containing piece and player data;
    // returns null if an IOException or ParseException occurs
    public static Game parseGameData(String fileLocation) {
        // https://howtodoinjava.com/library/json-simple-read-write-json-examples/#write-json-file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(fileLocation)) {
            Game game = new Game();
            Board board = new Board();
            JSONObject saveDataObject = (JSONObject) jsonParser.parse(reader);
            JSONArray jsonPieces = (JSONArray) saveDataObject.get("pieces");
            JSONArray jsonPlayers = (JSONArray) saveDataObject.get("players");
            ArrayList<Player> players = generatePlayerArrayList(game, jsonPlayers);
            ArrayList<ChessPiece> pieces = genChessPieceArrayList(board, jsonPieces);
            board.loadPieces(pieces);
            game.loadGame(players, board);
            return game;
        } catch (IOException | ParseException e) {
            System.out.println(e);
            return null;
        }
    }
}
