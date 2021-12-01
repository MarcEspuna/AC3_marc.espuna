package Business;

public class Monster extends Entity{

    protected String fleeChance;

    public Monster(Monster monster) {
        super((Entity)monster);
        this.fleeChance = monster.fleeChance;
    }

    public String getFleeChance() {
        return fleeChance;
    }


}
