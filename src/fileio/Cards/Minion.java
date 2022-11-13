package fileio.Cards;

import fileio.ActionsInput;
import fileio.CardInput;
import fileio.Table;

public class Minion extends Card{
    private int health;
    private int attackDamage;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public Minion(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
        this.health = cardInput.getHealth();
        this.attackDamage = cardInput.getAttackDamage();
    }

    public Minion(Minion minion) {
        super(minion.getMana(), minion.getDescription(), minion.getColors(), minion.getName());
        this.health = minion.health;
        this.attackDamage = minion.attackDamage;
    }

    @Override
    public String toString() {
        return super.toString() +
                "health=" + health +
                ", attackDamage=" + attackDamage +
                '}';
    }

    @Override
    public void ability(Minion attacked) {
        switch (this.getName()) {
            case "The Ripper": {
                attacked.setAttackDamage(attacked.getAttackDamage() - 2);
                if (attacked.getAttackDamage() < 0) attacked.setAttackDamage(0);
                break;
            }
            case "Miraj": {
                int swapHealth = this.getHealth();
                this.setHealth(attacked.getHealth());
                attacked.setHealth(swapHealth);
                break;
            }
            case "The Cursed One": {
                int newHealth = attacked.getAttackDamage();
                attacked.setAttackDamage(attacked.getHealth());
                attacked.setHealth(newHealth);
                break;
            }
            case "Disciple": {
                attacked.setHealth(attacked.getHealth() + 2);
                break;
            }
        }
    }
}
