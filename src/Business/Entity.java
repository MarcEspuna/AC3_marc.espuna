package Business;

import com.google.gson.annotations.SerializedName;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Random;

public class Entity {

    protected String name;
    protected int hp;
    transient protected int currentHp;
    protected int ac;
    protected String xp;
    protected int gold;
    protected String[] actions;
    transient protected int randomNumber;
    protected static final Random random = new Random();

    public Entity(Entity entity)
    {
        this.name = entity.name;
        this.hp = entity.hp;
        this.ac = entity.ac;
        this.xp = entity.xp;
        this.gold = entity.gold;
        this.actions = entity.actions.clone();
        this.randomNumber = entity.randomNumber;
        this.currentHp = entity.hp;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getName() {
        return name;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void addNumberToName(int number)
    {
        this.name = this.name + " " + number;
    }

    public int getMaxHp()
    {
        return hp;
    }

    public int getHp()
    {
        return currentHp;
    }

    public boolean damage(int i) {
        currentHp -= i;
        if (currentHp < 0)
        {
            currentHp = 0;
            return true;
        }
        return false;
    }

    protected String[] actionParser(String theAction)
    {
        return theAction.split(",");
    }

    protected int calculateDamage(String damage)
    {
        int totalDamage = 0;
        String[] damageParse = damage.split("d");
        for (int i = 0; i < Integer.parseInt(damageParse[0]); i++)
        {
            totalDamage += random.nextInt(Integer.parseInt(damageParse[1]));
        }
        return totalDamage;
    }

    protected String attack(Entity character, String damage, String actionType) {
        int totalDamage = calculateDamage(damage);
        character.damage(totalDamage);
        return name + " did " + totalDamage + " of "+ actionType + " damage against " + character.getName();
    }


    public String doAction(Entity character, int actionCounter) {
        if (actionCounter >= actions.length) { return "over";}
        int randomValue = random.nextInt(20);
        String[] actionVariables = actionParser(this.actions[actionCounter]);
        int hitPoints = Integer.parseInt(actionVariables[0]) + randomValue;
        if (hitPoints < character.getAc()) return entityMissed(hitPoints, character.getAc(), character);
        else return attack(character, actionVariables[1], actionVariables[2]);
    }

    protected String entityMissed(int hitPoints, int ac, Entity target)
    {
        return name + " missed with: " + hitPoints + " against " + target.getName() + " with ac of " + target.getAc();
    }

    protected int getAc()
    {
        return ac;
    }

    public String getXp(){
        return xp;
    }

    public int getGold()
    {
        return gold;
    }

    public String classAbility(ArrayList<Entity> battleCharacters, ArrayList<Integer> monstersTracker)
    {
        return new String("");
    }

    public void setHp(int currentHp) {
        this.hp = currentHp;
    }
}

