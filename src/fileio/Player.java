package fileio;

import fileio.Cards.Card;
import fileio.Cards.Hero;

import java.util.ArrayList;

public class Player {
    private int playerIdx;
    private ArrayList<Card> deck;
    private ArrayList<Card> hand = new ArrayList<>();
    private Hero hero;
    private int mana = 1;

    public int getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(int playerIdx) {
        this.playerIdx = playerIdx;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public Player(ArrayList<Card> deck, Hero hero, int playerIdx) {
        this.deck = deck;
        this.hero = hero;
        this.playerIdx = playerIdx;
        hand.add(this.deck.get(0));
        deck.remove(0);
    }
}
