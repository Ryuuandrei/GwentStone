package fileio;

import fileio.Cards.Card;
import fileio.Cards.Environment;
import fileio.Cards.Hero;
import fileio.Cards.Minion;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionHandler implements iActionHandler {

    String[] cardsOnTheFrontRow = {"Miraj", "Goliath", "Warden", "The Ripper"};
    String[] cardsOnTheBackRow = {"Sentinel", "Berserker", "The Cursed One", "Disciple"};
    public String placeCard(Table table, int handIdx) {

        var player = table.getPlayers().get(table.getPlayerTurn() - 1);
        
        //if (handIdx < player.getHand().size()) return;
        var card = player.getHand().get(handIdx);
        
        if (card.isEnvironment()) {
            return "Cannot place environment card on table.";
        }
        
        if (player.getMana() < card.getMana()) {
            return "Not enough mana to place card on table.";
        }
        ArrayList<String> cardSpecifier = new ArrayList<>(Arrays.asList(cardsOnTheFrontRow));

        final String s1 = "Cannot place card on table since row is full.";
        if (table.getPlayerTurn() == 1) {
            if (cardSpecifier.contains(card.getName())) {
                if (table.getBoard().get(2).size() >= 5) {
                    return s1;
                }
                table.getBoard().get(2).add(card);
            } else {
                if (table.getBoard().get(3).size() >= 5) {
                    return s1;
                }
                table.getBoard().get(3).add(card);
            }
        } else {
            if (cardSpecifier.contains(card.getName())) {
                if (table.getBoard().get(1).size() >= 5) {
                    return s1;
                }
                table.getBoard().get(1).add(card);
            } else {
                if (table.getBoard().get(0).size() >= 5) {
                    return s1;
                }
                table.getBoard().get(0).add(card);
            }
        }
        player.setMana(player.getMana() - card.getMana());
        player.getHand().remove(handIdx);

        return "success";
    }
    public int getPlayerMana(Player player) {
        return player.getMana();
    }

    public ArrayList<ArrayList<Card>> getCardsOnTable(Table table) {
        return table.getBoard();
    }

    public String cardUsesAttack(Table table, ActionsInput input) {

        if (cardsAreOnTheSameTeam(input.getCardAttacked().getX(), input.getCardAttacker().getX())) {
            return "Attacked card does not belong to the enemy.";
        }

        Minion attacker = (Minion) table.getBoard().get(input.getCardAttacker().getX()).get(input.getCardAttacker().getY());

        if (attacker.isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (attacker.isHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        Minion attacked = (Minion) table.getBoard().get(input.getCardAttacked().getX()).get(input.getCardAttacked().getY());

        if (containsTank(table, input.getCardAttacker().getX()) &&
                !(attacked.getName().compareTo("Goliath") == 0 || attacked.getName().compareTo("Warden") == 0)) {
            return "Attacked card is not of type 'Tank'.";
        }

        attacked.setHealth(attacked.getHealth() - attacker.getAttackDamage());
        if (attacked.getHealth() < 1) table.getBoard().get(input.getCardAttacked().getX()).remove(attacked);
        attacker.setHasAttacked(true);

        return "attackSuccess";
    }

    private boolean cardsAreOnTheSameTeam(int x1, int x2) {
        return (x1 == 2 && x2 == 3) ||
                (x1 == 3 && x2 == 2) ||
                (x1 == x2) ||
                (x1 == 1 && x2 == 0) ||
                (x1 == 0 && x2 == 1);
    }

    private boolean containsTank(Table table, int attackerX) {
        if (attackerX == 0 || attackerX == 1) {
            for (var card : table.getBoard().get(2)) {
                if (card.getName().compareTo("Goliath") == 0 ||
                        card.getName().compareTo("Warden") == 0) {
                    return true;
                }
            }
        }
        if (attackerX == 2 || attackerX == 3) {
            for (var card : table.getBoard().get(1)) {
                if (card.getName().compareTo("Goliath") == 0 ||
                        card.getName().compareTo("Warden") == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Environment> getEnvironmentCardsInHand(Player player) {
        ArrayList<Environment> environmentsCards = new ArrayList<>();
        for (var card : player.getHand()) {
            if (card.getName().compareTo("Firestorm") == 0 ||
            card.getName().compareTo("Winterfell") == 0 ||
            card.getName().compareTo("Heart Hound") == 0) {
                environmentsCards.add((Environment) card);
            }
        }
        return environmentsCards;
    }

    public Card getCardAtPosition(Table table, int x, int y) {
        if (y > table.getBoard().get(x).size() - 1) return null;
        return table.getBoard().get(x).get(y);
    }

    public String useEnvironmentCard(Table table, ActionsInput action) {
        var player = table.getPlayers().get(table.getPlayerTurn() - 1);
        var envi = player.getHand().get(action.getHandIdx());

        if (!envi.isEnvironment()) {
            return "Chosen card is not of type environment.";
        }
        if (envi.getMana() > table.getPlayers().get(table.getPlayerTurn() - 1).getMana()) {
            return "Not enough mana to use environment card.";
        }
        final String s = "Chosen row does not belong to the enemy.";
        if (table.getPlayerTurn() == 1) {
            if (cardsAreOnTheSameTeam(action.getAffectedRow(), 2)) return s;
            if (cardsAreOnTheSameTeam(action.getAffectedRow(), 3)) return s;
        }
        if (table.getPlayerTurn() == 2) {
            if (cardsAreOnTheSameTeam(action.getAffectedRow(), 0)) return s;
            if (cardsAreOnTheSameTeam(action.getAffectedRow(), 1)) return s;
        }

        if (envi.getName().compareTo("Heart Hound") == 0) {
            final String s1 = "Cannot steal enemy card since the player's row is full.";
            if (action.getAffectedRow() == 0 && table.getBoard().get(3).size() > 4) {
                return s1;
            }
            if (action.getAffectedRow() == 1 && table.getBoard().get(2).size() > 4) {
                return s1;
            }
            if (action.getAffectedRow() == 2 && table.getBoard().get(1).size() > 4) {
                return s1;
            }
            if (action.getAffectedRow() == 3 && table.getBoard().get(0).size() > 4) {
                return s1;
            }
        }

        envi.ability(table, action);
        player.setMana(player.getMana() - envi.getMana());
        player.getHand().remove(envi);

        return "envi";
    }

    public ArrayList<Card> getFrozenCardsOnTable(Table table) {
        ArrayList<Card> frozenCards = new ArrayList<>();

        for (int i = 0 ; i < 4; i++) {
            for (var card : table.getBoard().get(i)) {
                if (card.isFrozen()) frozenCards.add(card);
            }
        }
        return frozenCards;
    }

    public String cardUsesAbility(Table table, ActionsInput input) {

        Minion attacker = (Minion) table.getBoard().get(input.getCardAttacker().getX()).get(input.getCardAttacker().getY());

        if (attacker.getName().compareTo("Disciple") == 0 &&
        !(cardsAreOnTheSameTeam(input.getCardAttacked().getX(), input.getCardAttacker().getX()))) {
            return "Attacked card does not belong to the current player.";
        }

        if (cardsAreOnTheSameTeam(input.getCardAttacked().getX(), input.getCardAttacker().getX())
        && attacker.getName().compareTo("Disciple") != 0) {
            return "Attacked card does not belong to the enemy.";
        }

        if (attacker.isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (attacker.isHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        Minion attacked = (Minion) table.getBoard().get(input.getCardAttacked().getX()).get(input.getCardAttacked().getY());

        if (containsTank(table, input.getCardAttacker().getX()) &&
                !(attacked.getName().compareTo("Goliath") == 0 || attacked.getName().compareTo("Warden") == 0)) {
            return "Attacked card is not of type 'Tank'.";
        }

        attacker.ability(attacked);
//        attacked.setHealth(attacked.getHealth() - attacker.getAttackDamage());
        if (attacked.getHealth() < 1) table.getBoard().get(input.getCardAttacked().getX()).remove(attacked);
        attacker.setHasAttacked(true);

        return "abilitySuccess";
    }

    public String useAttackHero(Table table, ActionsInput input) {

        Minion attacker = (Minion) table.getBoard().get(input.getCardAttacker().getX()).get(input.getCardAttacker().getY());

        if (attacker.isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (attacker.isHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        if (containsTank(table, input.getCardAttacker().getX())) {
            return "Attacked card is not of type 'Tank'.";
        }

        Hero hero;
        if (table.getPlayerTurn() == 1) {
            hero = table.getPlayers().get(1).getHero();
            hero.setHealth(hero.getHealth() - attacker.getAttackDamage());
        } else {
            hero = table.getPlayers().get(0).getHero();
            hero.setHealth(hero.getHealth() - attacker.getAttackDamage());
        }

        //if (hero.getHealth() < 1) return "Player one killed the hero";
        attacker.setHasAttacked(true);

        return "attackHeroSuccess";

    }

    public String useHeroAbility(Table table, ActionsInput action) {

        Player player = table.getPlayers().get(table.getPlayerTurn() - 1);
        System.out.println(player.getMana() + " " + player.getHero().getMana());

        Hero hero = player.getHero();

        if (hero.isHasAttacked()) return "Hero has already attacked this turn.";

        final String s = "Selected row does not belong to the enemy.";
        if (hero.getName().compareTo("Lord Royce") == 0 ||
                hero.getName().compareTo("Empress Thorina") == 0) {
            if (table.getPlayerTurn() == 1) {
                if (cardsAreOnTheSameTeam(action.getAffectedRow(), 2)) return s;
                if (cardsAreOnTheSameTeam(action.getAffectedRow(), 3)) return s;
            }
            if (table.getPlayerTurn() == 2) {
                if (cardsAreOnTheSameTeam(action.getAffectedRow(), 0)) return s;
                if (cardsAreOnTheSameTeam(action.getAffectedRow(), 1)) return s;
            }
        }

        final String s1 = "Selected row does not belong to the current player.";
        if (hero.getName().compareTo("King Mudface") == 0 ||
                hero.getName().compareTo("General Kocioraw") == 0) {
            if (table.getPlayerTurn() == 1) {
                if (!cardsAreOnTheSameTeam(action.getAffectedRow(), 2)) return s1;
                if (!cardsAreOnTheSameTeam(action.getAffectedRow(), 3)) return s1;
            }
            if (table.getPlayerTurn() == 2) {
                if (!cardsAreOnTheSameTeam(action.getAffectedRow(), 0)) return s1;
                if (!cardsAreOnTheSameTeam(action.getAffectedRow(), 1)) return s1;
            }
        }

        if (player.getMana() < player.getHero().getMana()) {
            return "Not enough mana to use hero's ability.";
        }

        hero.ability(table, action);
        player.setMana(player.getMana() - hero.getMana());
        hero.setHasAttacked(true);

        return "heroAbilitySuccess";

    }

    public int getPlayerOneWins(Statistics statistics) {
        return statistics.getPlayerOneWins();
    }

    public int getPlayerTwoWins(Statistics statistics) {
        return statistics.getPlayerTwoWins();
    }

    public int getTotalGamesPlayed(Statistics statistics) {
        return statistics.getNumberOfGames();
    }
}

