package fileio;

import fileio.Cards.Card;
import fileio.Cards.Hero;

import java.util.ArrayList;

public interface iActionHandler {
    default void endPlayerTurn(Table table) {
        if (table.getPlayerTurn() == 1) {
            for (var card : table.getBoard().get(3)) {
                card.setFrozen(false);
                card.setHasAttacked(false);
            }
            for (var card : table.getBoard().get(2)) {
                card.setFrozen(false);
                card.setHasAttacked(false);
            }
            table.getPlayers().get(0).getHero().setHasAttacked(false);
        } else {
            for (var card : table.getBoard().get(0)) {
                card.setFrozen(false);
                card.setHasAttacked(false);
            }
            for (var card : table.getBoard().get(1)) {
                card.setFrozen(false);
                card.setHasAttacked(false);
            }
            table.getPlayers().get(1).getHero().setHasAttacked(false);
        }

        if (table.getPlayerTurn() == 1) {
            table.setPlayerTurn(2);
        } else {
            table.setPlayerTurn(1);
        }

        table.setNumberOfTurns(table.getNumberOfTurns() + 1);

        if (table.getNumberOfTurns() % 2 == 0) {
            table.getPlayers().get(0).setMana(table.getPlayers().get(0).getMana() + table.getNumberOfTurns()/2 + 1);
            table.getPlayers().get(1).setMana(table.getPlayers().get(1).getMana() + table.getNumberOfTurns()/2 + 1);

            var players = table.getPlayers();
            if (players.get(0).getDeck().size() > 0) {
                players.get(0).getHand().add(players.get(0).getDeck().get(0));
                players.get(0).getDeck().remove(0);
            }
            if (players.get(1).getDeck().size() > 0) {
                players.get(1).getHand().add(players.get(1).getDeck().get(0));
                players.get(1).getDeck().remove(0);
            }
        }

    }

    default ArrayList<Card> getPlayerDeck(Player player) {
        return player.getDeck();
    }
    default Hero getPlayerHero(Player player) {
        return player.getHero();
    };
    default int getPlayerTurn(Table table) {
        return table.getPlayerTurn();
    }
    default ArrayList<Card> getCardsInHand(Player player) {
        return player.getHand();
    }
}
