package Business;

import java.util.ArrayList;

public class Cleric extends Player{


    public Cleric(Player player) {
        super(player);
    }


    private boolean healPlayer(Entity player)
    {
        double currentHp = player.currentHp;
        double maxHp = player.hp;
        if (currentHp/maxHp < 0.5 && currentHp != 0) {
            currentHp = maxHp*0.2;
            player.currentHp = (int)currentHp;
            return true;
        }
        return false;
    }

    @Override
    public String classAbility(ArrayList<Entity> battleCharacters, ArrayList<Integer> monstersTracker)
    {
        for (Entity battleCharacter : battleCharacters) {
            if (battleCharacter instanceof Player)
            {
                if (healPlayer(battleCharacter)) return name + " healed " + battleCharacter.getName() + " for 11 hp\n";
            }
        }
        return new String("");
    }
}

