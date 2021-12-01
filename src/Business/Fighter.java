package Business;

import java.util.ArrayList;

public class Fighter extends Player{
    public Fighter(Player player) {
        super(player);
    }


    @Override
    public String classAbility(ArrayList<Entity> battleCharacters, ArrayList<Integer> monstersTracker)
    {

        return new String("");
    }

    @Override
    public String doAction(Entity monster, int actionCounter) {
        if (actionCounter >= actions.length*2 || healed) { healed = false; return "over";}
        int healingFactor;
        if ((double)currentHp/(double)hp < 0.2 && potionCharges>0)
        {
            healingFactor = heal();
            potionCharges--;
            return name + " healed " + healingFactor + " hp with potion";
        }
        int randomValue = random.nextInt(20);
        String[] actionVariables = actionParser(this.actions[actionCounter % actions.length]);
        int hitPoints = Integer.parseInt(actionVariables[0]) + randomValue;
        if (hitPoints < monster.getAc()) return entityMissed(hitPoints, monster.getAc(), monster);
        else return attack(monster, actionVariables[1], actionVariables[2]);
    }





}



