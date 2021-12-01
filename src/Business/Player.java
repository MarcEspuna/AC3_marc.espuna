package Business;

import com.google.gson.annotations.SerializedName;

public class Player extends Entity{

    protected Player(Player player)
    {
        super(player);
        this.playerType = player.playerType;
        this.potionCharges = player.potionCharges;
    }

    @SerializedName(value = "class")
    protected String playerType;
    @SerializedName(value = "Potion Charges")
    protected int potionCharges;

    public String classType() {
        return playerType;
    }

    public int heal()
    {
        int healingFactor = (int) (((double)hp)/5.0);
        currentHp += healingFactor;
        return healingFactor;
    }

    @Override
    public void classAbility()
    {

    }

    @Override
    public String doAction(Entity monster, int actionCounter) {
        if (actionCounter >= actions.length) { return "over";}
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

}
