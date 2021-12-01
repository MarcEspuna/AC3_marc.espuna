package Business;

import java.util.ArrayList;

public class Cleric extends Player{


    public Cleric(Player player) {
        super(player);
    }



    @Override
    public String classAbility(ArrayList<Entity> battleCharacters, ArrayList<Integer> monstersTracker)
    {
        return new String("");
    }
}

