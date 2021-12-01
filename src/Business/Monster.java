package Business;

import java.util.Random;

public class Monster extends Entity{

    protected String fleeChance;
    protected boolean fleed;

    public Monster(Monster monster) {
        super((Entity)monster);
        this.fleeChance = monster.fleeChance;
        this.fleed = false;
    }

    public String getFleeChance() {
        return fleeChance;
    }

    private void checkFlee()
    {
        StringBuilder sb = new StringBuilder(fleeChance);
        sb.deleteCharAt(sb.indexOf("%"));
        int fleeChance = Integer.parseInt(sb.toString());
        if (fleeChance >= currentHp)
        {
            fleed = true;
        }
    }

    public boolean hasFled()
    {
        return fleed;
    }

    @Override
    public String doAction(Entity character, int actionCounter) {
        if (fleed) {currentHp = 0;return "over";}
        if (actionCounter == 0) checkFlee();
        if (fleed) { return  name + " has left the encounter";}
        return super.doAction(character,actionCounter);
    }


}
