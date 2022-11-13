package fileio.Cards;

import fileio.ActionsInput;
import fileio.CardInput;
import fileio.Table;

public class Environment extends Card{
    public Environment(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
    }

    public Environment(Environment environment) {
        super(environment.getMana(), environment.getDescription(), environment.getColors(), environment.getName());
    }

    /**
     *
     */
    @Override
    public void ability(Table table, ActionsInput action) {
        switch (this.getName()) {
            case "Winterfell": {
                for (var card : table.getBoard().get(action.getAffectedRow()))
                    card.setFrozen(true);
                break;
            }
            case "Firestorm": {
                for (int i = 0; i < table.getBoard().get(action.getAffectedRow()).size(); i++) {
                    var card = table.getBoard().get(action.getAffectedRow()).get(i);
                    ((Minion) card).setHealth(((Minion) card).getHealth() - 1);
                    if (((Minion) card).getHealth() < 1) {
                        table.getBoard().get(action.getAffectedRow()).remove(card);
                        i--;
                    }
                }
                break;
            }
            case "Heart Hound": {
                var card = table.getBoard().get(action.getAffectedRow()).get(0);
                for (int i = 1; i < table.getBoard().get(action.getAffectedRow()).size(); i++) {
                    if (((Minion) card).getHealth() < ((Minion) table.getBoard().get(action.getAffectedRow()).get(i)).getHealth())
                        card = table.getBoard().get(action.getAffectedRow()).get(i);
                }
                if (action.getAffectedRow() == 0) {
                    table.getBoard().get(3).add(card);
                }
                if (action.getAffectedRow() == 1) {
                    table.getBoard().get(2).add(card);
                }
                if (action.getAffectedRow() == 2) {
                    table.getBoard().get(1).add(card);
                }
                if (action.getAffectedRow() == 3) {
                    table.getBoard().get(0).add(card);
                }
                table.getBoard().get(action.getAffectedRow()).remove(card);
                break;
            }
        }
    }
}
