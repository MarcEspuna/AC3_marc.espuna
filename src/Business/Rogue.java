package Business;

import java.util.ArrayList;

public class Rogue extends Player{


    public Rogue(Player player) {
        super(player);
    }

    protected String attack(Entity character, int damage, String actionType) {
        boolean characterKilled = character.damage(damage);
        String result = name + " did " + damage + " of "+ actionType + " damage against " + character.getName();
        if (characterKilled) {
            stealResources(character);
            result = result.concat("\n" + name + " killed " + character.getName());
        }
        return result;
    }

    private String useStrongestAttack(Entity target)
    {
        String[] actionVariables;
        int actionCounter = 0;
        int damage = 0;
        int actionNumber = 0;
        for (String action : actions) {

            actionVariables = actionParser(action);
            int checkDamage = calculateDamage(actionVariables[1]);
            if  (checkDamage > damage) { damage = checkDamage; actionNumber = actionCounter;}
            actionCounter++;
        }

        int randomValue = random.nextInt(20);
        actionVariables = actionParser(this.actions[actionNumber]);
        int hitPoints = Integer.parseInt(actionVariables[0]) + randomValue;
        if (hitPoints < target.getAc()) return entityMissed(hitPoints, target.getAc(), target);
        else return attack(target, damage, actionVariables[2]);

    }

    private int checkWeackestMonster(ArrayList<Entity> battleCharacters, ArrayList<Integer> monstersTracker)
    {
        int targetIndex = 0;
        Entity checkTarget = battleCharacters.get(monstersTracker.get(targetIndex));
        int minimumHealth = checkTarget.currentHp;
        for (int i = 1; i < monstersTracker.size(); i++)
        {
            checkTarget = battleCharacters.get(monstersTracker.get(i));
            if (minimumHealth > checkTarget.currentHp)
            {
                minimumHealth = checkTarget.currentHp;
                targetIndex = monstersTracker.get(i);
            }
        }
        return targetIndex;
    }

    public String classAbility(ArrayList<Entity> battleCharacters, ArrayList<Integer> monstersTracker)
    {
        int weakestMonsterIndex = checkWeackestMonster(battleCharacters, monstersTracker);
        return useStrongestAttack(battleCharacters.get(weakestMonsterIndex)) + "\n";
    }
}



