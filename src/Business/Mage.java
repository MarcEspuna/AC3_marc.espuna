package Business;

import java.util.ArrayList;

public class Mage extends Player{


    public Mage(Player player) {
        super(player);

    }


    @Override
    public String classAbility(ArrayList<Entity> battleCharacters, ArrayList<Integer> monstersTracker)
    {
        if (monstersTracker.size() > 3) {return fireball(battleCharacters, monstersTracker); }
        return new String("");
    }

    private String fireball(ArrayList<Entity> battleCharacters, ArrayList<Integer> monstersTracker) {

        for (Integer monsterIndex : monstersTracker) {
            this.attack(battleCharacters.get(monsterIndex), "6d6", "fireball");
        }

        for (Entity battleCharacter : battleCharacters) {
            if (battleCharacter instanceof Player)
            {
                this.attack(battleCharacter, "2d6", "fireball");
            }
        }
        return name + " used FIREBALL!!\n";
    }
}

