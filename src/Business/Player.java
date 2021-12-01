package Business;

import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class Player extends Entity{

    protected Player(Player player)
    {
        super(player);
        this.playerType = player.playerType;
        this.potionCharges = player.potionCharges;
        this.healed = false;
    }

    @SerializedName(value = "class")
    protected String playerType;
    @SerializedName(value = "Potion Charges")
    protected int potionCharges;
    protected boolean healed;

    public String classType() {
        return playerType;
    }

    public int heal()
    {
        int healingFactor = (int) (((double)hp)/5.0);
        currentHp += healingFactor;
        healed = true;
        return healingFactor;
    }

    @Override
    protected String attack(Entity character, String damage, String actionType) {
        int totalDamage = calculateDamage(damage);
        boolean characterKilled = character.damage(totalDamage);
        if (characterKilled) { stealResources(character);}
        return name + " did " + totalDamage + " of "+ actionType + " damage against " + character.getName();
    }

    @Override
    public String doAction(Entity monster, int actionCounter) {
        if (actionCounter >= actions.length || healed) { healed = false; return "over";}
        int healingFactor;
        if ((double)currentHp/(double)hp < 0.2 && potionCharges>0)
        {
            healingFactor = heal();
            potionCharges--;
            return name + " healed " + healingFactor + " hp with potion";
        }
        int randomValue = random.nextInt(20);
        String[] actionVariables = actionParser(this.actions[actionCounter]);

        int hitPoints = Integer.parseInt(actionVariables[0]) + randomValue;
        if (hitPoints < monster.getAc()) return entityMissed(hitPoints, monster.getAc(), monster);
        else return attack(monster, actionVariables[1], actionVariables[2]);
    }

    public int getPotionCharges()
    {
        return potionCharges;
    }




    private void stealResources(Entity character) {
        this.gold += character.getGold();
        try {
            Number currentXp = NumberFormat.getNumberInstance(java.util.Locale.US).parse(character.getXp());
            Number targetXp = NumberFormat.getNumberInstance(java.util.Locale.US).parse(this.xp);
            int number = targetXp.intValue() + currentXp.intValue();
            this.xp = NumberFormat.getNumberInstance(Locale.US).format(number);
        }

        catch (ParseException ie)
        {
            this.xp = "error parsing";
        }

    }

}

