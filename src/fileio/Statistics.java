package fileio;

import lombok.Data;

@Data
public class Statistics {

    private int playerOneWins;
    private int playerTwoWins;
    private static Statistics instance = null;
    private Statistics() {}

    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
            instance.playerOneWins = 0;
            instance.playerTwoWins = 0;
        }
        return instance;
    }

    public void playerOneWon() {
        playerOneWins++;
        return;
    }
    public void playerTwoWon() {
        playerTwoWins++;
        return;
    }

    public int getNumberOfGames() {
        return playerTwoWins + playerOneWins;
    }

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    public void setPlayerOneWins(int playerOneWins) {
        this.playerOneWins = playerOneWins;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public void setPlayerTwoWins(int playerTwoWins) {
        this.playerTwoWins = playerTwoWins;
    }
}
