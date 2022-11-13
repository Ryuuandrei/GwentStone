package fileio;

import fileio.Cards.Card;

import java.util.ArrayList;

public class Table{
    private ArrayList<Player> players = new ArrayList<>();
    private int playerTurn;
    private int numberOfTurns = 0;

    private ArrayList<ArrayList<Card>> board = new ArrayList<>();

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<ArrayList<Card>> board) {
        this.board = board;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public Table(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

}
