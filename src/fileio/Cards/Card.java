package fileio.Cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fileio.ActionsInput;
import fileio.Table;

import java.util.ArrayList;
@JsonIgnoreProperties({ "frozen", "hasAttacked", "environment" })
public abstract class Card {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean isFrozen = false;
    private boolean hasAttacked = false;

    public boolean isHasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card(int mana, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    public void ability(Table table, ActionsInput action) {}
    public void ability(Minion minion) {}

    @Override
    public String toString() {
        return "Card{" +
                "mana=" + mana +
                ", description='" + description + '\'' +
                ", colors=" + colors +
                ", name='" + name + '\'' +
                '}';
    }
    public boolean isEnvironment() {
        if (name.compareTo("Firestorm") == 0 ||
        name.compareTo("Winterfell") == 0 ||
        name.compareTo("Heart Hound") == 0) return true;
        return false;
    }
}
