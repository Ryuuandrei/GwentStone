package fileio.Cards;

import fileio.ActionsInput;
import fileio.CardInput;
import fileio.Table;

public class Hero extends Card{
    private int health = 30;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public Hero(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
    }

    public Hero(Hero otherHero) {
        super(otherHero.getMana(), otherHero.getDescription(), otherHero.getColors(), otherHero.getName());
        health = otherHero.health;
    }

    /**
     * @param table
     * @param action
     */
    @Override
    public void ability(Table table, ActionsInput action) {
        switch (this.getName()) {
            case "Lord Royce": {
                var card = table.getBoard().get(action.getAffectedRow()).get(0);
                for (int i = 1; i < table.getBoard().get(action.getAffectedRow()).size(); i++) {
                    if (((Minion) card).getAttackDamage() < ((Minion) table.getBoard().get(action.getAffectedRow()).get(i)).getAttackDamage())
                        card = table.getBoard().get(action.getAffectedRow()).get(i);
                }
                card.setFrozen(true);
                break;
            }
            case "Empress Thorina": {
                var card = table.getBoard().get(action.getAffectedRow()).get(0);
                for (int i = 1; i < table.getBoard().get(action.getAffectedRow()).size(); i++) {
                    if (((Minion) card).getHealth() < ((Minion) table.getBoard().get(action.getAffectedRow()).get(i)).getHealth())
                        card = table.getBoard().get(action.getAffectedRow()).get(i);
                }
                table.getBoard().get(action.getAffectedRow()).remove(card);
                break;
            }
            case "King Mudface": {
                for (var card : table.getBoard().get(action.getAffectedRow())) {
                    ((Minion) card).setHealth(((Minion) card).getHealth() + 1);
                }
                break;
            }
            case "General Kocioraw": {
                for (var card : table.getBoard().get(action.getAffectedRow())) {
                    ((Minion) card).setAttackDamage(((Minion) card).getAttackDamage() + 1);
                }
                break;
            }
        }
    }
}
