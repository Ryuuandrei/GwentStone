package main;

import checker.Checker;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import fileio.*;
import fileio.Cards.Card;
import fileio.Cards.Environment;
import fileio.Cards.Hero;
import fileio.Cards.Minion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static java.util.Collections.shuffle;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();
        //TODO add here the entry point to your implementation

        int a = 0;
        int b = 0;

        GameInput gameInput;
        DecksInput player1decks;
        DecksInput player2decks;

        // the input is distributed
        player1decks = inputData.getPlayerOneDecks();
        player2decks = inputData.getPlayerTwoDecks();

        Statistics statistics = Statistics.getInstance();

        for (int k = 0; k < inputData.getGames().size(); k++) {

            gameInput = inputData.getGames().get(k);
            ArrayList<CardInput> player1deck = new ArrayList<>(player1decks.getDecks().get(gameInput.getStartGame().getPlayerOneDeckIdx()));
            ArrayList<CardInput> player2deck = new ArrayList<>(player2decks.getDecks().get(gameInput.getStartGame().getPlayerTwoDeckIdx()));
            shuffle(player1deck, new Random(gameInput.getStartGame().getShuffleSeed()));
            shuffle(player2deck, new Random(gameInput.getStartGame().getShuffleSeed()));

            String[] MinionList = {"Miraj", "Sentinel", "Berserker", "Goliath", "Warden", "The Cursed One", "Disciple", "The Ripper"};
            String[] EnvironmentList = {"Firestorm", "Winterfell", "Heart Hound"};
            ArrayList<String> minionList = new ArrayList<>(Arrays.asList(MinionList));
            ArrayList<String> environmentList = new ArrayList<>(Arrays.asList(EnvironmentList));

            ArrayList<Card> player1deck_notInput = new ArrayList<>();
            ArrayList<Card> player2deck_notInput = new ArrayList<>();

            for (var cardInput : player1deck) {
                if (environmentList.contains(cardInput.getName())) {
                    var card = new Environment(cardInput);
                    player1deck_notInput.add(card);
                }
                if (minionList.contains(cardInput.getName())) {
                    var card = new Minion(cardInput);
                    player1deck_notInput.add(card);
                }
            }

            for (var cardInput : player2deck) {
                if (environmentList.contains(cardInput.getName())) {
                    var card = new Environment(cardInput);
                    player2deck_notInput.add(card);
                }
                if (minionList.contains(cardInput.getName())) {
                    var card = new Minion(cardInput);
                    player2deck_notInput.add(card);
                }
            }

            Player player_1 = new Player(player1deck_notInput, new Hero(gameInput.getStartGame().getPlayerOneHero()), 1);
            Player player_2 = new Player(player2deck_notInput, new Hero(gameInput.getStartGame().getPlayerTwoHero()), 2);

            Table table = new Table(gameInput.getStartGame().getStartingPlayer());
            for (int i = 0; i < 4; i++) {
                table.getBoard().add(new ArrayList<>());
            }
            table.getPlayers().add(player_1);
            table.getPlayers().add(player_2);

            ActionHandler actionHandler = new ActionHandler();

            for (var action : gameInput.getActions()) {

                switch (action.getCommand()) {
                    case "getPlayerDeck": {
                        var deck = actionHandler.getPlayerDeck(table.getPlayers().get(action.getPlayerIdx() - 1));
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", deck);
                        break;
                    }
                    case "getPlayerHero": {
                        var hero = actionHandler.getPlayerHero(table.getPlayers().get(action.getPlayerIdx() - 1));
                        var heroCopy = new Hero(hero);
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", heroCopy);
                        break;
                    }
                    case "getCardsInHand": {
                        var cardsInHand = actionHandler.getCardsInHand(table.getPlayers().get(action.getPlayerIdx() - 1));
                        ArrayList<Card> cardsCopy = new ArrayList<>();
                        for (var card : cardsInHand) {
                            if (minionList.contains(card.getName())) cardsCopy.add(new Minion((Minion) card));
                            else cardsCopy.add(new Environment((Environment) card));
                        }
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", cardsCopy);
                        break;
                    }
                    case "getPlayerTurn": {
                        var turn = actionHandler.getPlayerTurn(table);
                        output.addObject().put("command", action.getCommand()).put("output", turn);
                        break;
                    }
                    case "endPlayerTurn": {
                        System.out.println(table.getNumberOfTurns());
                        actionHandler.endPlayerTurn(table);
                        System.out.println("Carti dupa ce a tras" + table.getPlayers().get(table.getPlayerTurn() - 1).getHand());
                        break;
                    }
                    case "placeCard": {
                        if (table.getPlayers().get(table.getPlayerTurn() - 1).getHand().size() - 1 < action.getHandIdx())
                            break;
                        var success = actionHandler.placeCard(table, action.getHandIdx());
                        if (success.compareTo("success") != 0)
                            output.addObject().put("command", "placeCard").put("handIdx", action.getHandIdx()).put("error", success);
                        break;
                    }
                    case "getPlayerMana": {
                        var mana = actionHandler.getPlayerMana(table.getPlayers().get(action.getPlayerIdx() - 1));
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).put("output", mana);
                        break;
                    }
                    case "getCardsOnTable": {
                        var allCards = actionHandler.getCardsOnTable(table);
                        output.addObject().put("command", action.getCommand()).putPOJO("output", allCards);
                        break;
                    }
                    case "cardUsesAttack": {
                        if (action.getCardAttacker().getY() > table.getBoard().get(action.getCardAttacker().getX()).size() - 1
                                || action.getCardAttacked().getY() > table.getBoard().get(action.getCardAttacked().getX()).size() - 1) {
                            break;
                        }
                        var attackSuccess = actionHandler.cardUsesAttack(table, action);
                        if (attackSuccess.compareTo("attackSuccess") != 0) {
                            output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker",
                                    action.getCardAttacker()).putPOJO("cardAttacked",
                                    action.getCardAttacked()).put("error", attackSuccess);
                        }
                        break;
                    }
                    case "getEnvironmentCardsInHand": {
                        var enviCards = actionHandler.getEnvironmentCardsInHand(table.getPlayers().get(action.getPlayerIdx() - 1));
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", enviCards);
                        break;
                    }
                    case "getCardAtPosition": {
                        var card = actionHandler.getCardAtPosition(table, action.getX(), action.getY());
                        if (card == null) output.addObject().put("command", action.getCommand()).put("output",
                                "No card available at that position.").put("x", action.getX()).put("y", action.getY());
                        else {
                            var cardCopy = new Minion(((Minion) card));
                            output.addObject().put("command", action.getCommand()).put("x", action.getX()).put("y", action.getY()).putPOJO("output", cardCopy);
                        }
                        break;
                    }
                    case "useEnvironmentCard": {
                        //if (table.getBoard().get(action.getAffectedRow()).size() == 0) break;
                        var envi = actionHandler.useEnvironmentCard(table, action);
                        if (envi.compareTo("envi") != 0)
                            output.addObject().put("command", action.getCommand()).put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                                    .put("error", envi);
                        break;
                    }
                    case "getFrozenCardsOnTable": {
                        var frozenCards = actionHandler.getFrozenCardsOnTable(table);
                        output.addObject().put("command", action.getCommand()).putPOJO("output", frozenCards);
                        break;
                    }
                    case "cardUsesAbility": {
                        if (action.getCardAttacker().getY() > table.getBoard().get(action.getCardAttacker().getX()).size() - 1
                                || action.getCardAttacked().getY() > table.getBoard().get(action.getCardAttacked().getX()).size() - 1) {
                            break;
                        }
                        var abilitySuccess = actionHandler.cardUsesAbility(table, action);
                        if (abilitySuccess.compareTo("abilitySuccess") != 0) {
                            output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker",
                                    action.getCardAttacker()).putPOJO("cardAttacked",
                                    action.getCardAttacked()).put("error", abilitySuccess);
                        }
                        break;
                    }
                    case "useAttackHero": {
                        if (action.getCardAttacker().getY() > table.getBoard().get(action.getCardAttacker().getX()).size() - 1) {
                            break;
                        }
                        var attackHeroSuccess = actionHandler.useAttackHero(table, action);
                        if (attackHeroSuccess.compareTo("attackHeroSuccess") != 0) {
                            output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker",
                                    action.getCardAttacker()).put("error", attackHeroSuccess);
                        }
                        if (table.getPlayerTurn() == 1) {
                            if (table.getPlayers().get(1).getHero().getHealth() < 1) {
                                a++;
                                statistics.playerOneWon();
                                output.addObject().put("gameEnded", "Player one killed the enemy hero.");
                                System.out.println("ceva " + statistics);
                                break;
                            }
                        }
                        if (table.getPlayerTurn() == 2) {
                            if (table.getPlayers().get(0).getHero().getHealth() < 1) {
                                b++;
                                statistics.playerTwoWon();
                                output.addObject().put("gameEnded", "Player two killed the enemy hero.");
                            }
                        }
                        break;
                    }
                    case "useHeroAbility": {
                        if (table.getBoard().get(action.getAffectedRow()).size() == 0) {
                            if (table.getPlayers().get(table.getPlayerTurn() - 1).getMana() <
                                    table.getPlayers().get(table.getPlayerTurn() - 1).getHero().getMana()) {
                                output.addObject().put("command", action.getCommand()).put("affectedRow",
                                        action.getAffectedRow()).put("error", "Not enough mana to use hero's ability.");
                                break;
                            }
                        }
                        var heroAbilitySuccess = actionHandler.useHeroAbility(table, action);
                        if (heroAbilitySuccess.compareTo("heroAbilitySuccess") != 0) {
                            output.addObject().put("command", action.getCommand()).put("affectedRow",
                                    action.getAffectedRow()).put("error", heroAbilitySuccess);
                        }
                        break;
                    }
                    case "getPlayerOneWins": {
                        output.addObject().put("command", action.getCommand()).put("output",
                                actionHandler.getPlayerOneWins(statistics));
                        break;
                    }
                    case "getPlayerTwoWins": {
                        output.addObject().put("command", action.getCommand()).put("output",
                                actionHandler.getPlayerTwoWins(statistics));
                        break;
                    }
                    case "getTotalGamesPlayed": {
                        output.addObject().put("command", action.getCommand()).put("output",
                                actionHandler.getTotalGamesPlayed(statistics));
                        break;
                    }
                    default:
                        break;
                }

            }
        }
        System.out.println(a + " " + b);

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
