package Business;
import java.util.*;

public class BattleManager {
    private static final String ALL_ACTIONS_DONE = "over";

    private final ArrayList<Entity> battlefieldCharacters;      //ALl the characters that will play in the battlefield
    private final ArrayList<Integer> monstersTracker;           //Contains the index of the active monsters
    private final ArrayList<Integer> playersTracker;            //Contains the index of the active players
    private final Random random;



    public BattleManager() {
        battlefieldCharacters = new ArrayList<>();
        monstersTracker = new ArrayList<>();
        playersTracker = new ArrayList<>();
        random = new Random();
    }

    public void initiativeList() {
        //Generate random numbers for each character in the battlefield
        for (Entity character : battlefieldCharacters) {
            character.setRandomNumber(random.nextInt(19) + 1);
        }
        //Lambda expression to sort based of the random value generated
        battlefieldCharacters.sort(Comparator.comparingInt(x -> x.randomNumber));
        //We make it descending order
        Collections.reverse(battlefieldCharacters);
        //If a player or monster get the same value, the players go first
        resolveCharactersWithSameValue();
    }

    //Solves the case of a player or monster getting the same value. In that case the players go first
    private void resolveCharactersWithSameValue()
    {
        for (int i = 0; i < battlefieldCharacters.size()-1; i++)
        {
            Entity firstCharacter = battlefieldCharacters.get(i);
            Entity secondCharacter = battlefieldCharacters.get(i+1);
            if (firstCharacter.randomNumber == secondCharacter.randomNumber)
            {
                if (firstCharacter instanceof Monster && secondCharacter instanceof Player)
                {
                    Collections.swap(battlefieldCharacters, i, i+1);
                }
            }
        }
    }

    //We will use trackers to track the index of the monsters and the index of the players that are alive, in the main battlefield array
    public void setTrackers() {
        playersTracker.clear();
        monstersTracker.clear();
        int counter = 0;
        for (Entity character : battlefieldCharacters) {
            if (character instanceof Player) {
                playersTracker.add(counter);
            } else {
                monstersTracker.add(counter);
            }
            counter++;
        }
    }


    private void addMonsters(Monster monster, int numberMonsters) {
        for (int i = 0; i < numberMonsters; i++) {
            Monster newMonster = new Monster(monster);
            newMonster.addNumberToName(i + 1);
            battlefieldCharacters.add(newMonster);
        }
    }

    //Generates the array that will be used to track all the characters that will fight in the battlefield
    public void generateCharacters(HashMap<String, Integer> selectedMonsters, ArrayList<Monster> monsters, ArrayList<Player> players) {
        battlefieldCharacters.clear();
        for (Monster monster : monsters) {
            String monsterName = monster.getName();
            if (selectedMonsters.get(monsterName) != null) {
                Integer numMonsters = selectedMonsters.get(monsterName);
                addMonsters(monster, numMonsters);
            }
        }
        battlefieldCharacters.addAll(players);
    }


    //Chose a random player target that is alive
    private Player choosePlayerTarget() {
        int playerIndex;
        if (playersTracker.size() > 1) playerIndex = playersTracker.get(random.nextInt(playersTracker.size() - 1));
        else {
            playerIndex = playersTracker.get(0);
        }
        return (Player) battlefieldCharacters.get(playerIndex);
    }

    //Chose a random monster target that is alive
    private Monster chooseMonsterTarget() {
        int monsterIndex;
        if (monstersTracker.size() > 1) monsterIndex = monstersTracker.get(random.nextInt(monstersTracker.size() - 1));
        else {
            monsterIndex = monstersTracker.get(0);
        }
        return (Monster) battlefieldCharacters.get(monsterIndex);
    }

    public boolean checkBattleEnded() {
        //Update first the monsters that have left the battlefield
        for (int i = 0; i < monstersTracker.size(); i++)
        {
            Monster monster = (Monster)battlefieldCharacters.get(monstersTracker.get(i));
            if (monster.hasFled()) {monstersTracker.remove(Integer.valueOf(battlefieldCharacters.indexOf(monster)));}
        }

        //Check if all the monsters or all the players are defeated
        return (playersTracker.size() == 0 || monstersTracker.size() == 0);
    }

    //Choose the target that the entity will attack to
    private Entity chooseTarget(Entity origin)
    {
        Entity target;
        if (origin instanceof Player)
        {
            target = chooseMonsterTarget();
        }else
        {
            target = choosePlayerTarget();
        }
        return target;
    }

    //Actual fight of each character
    public String fight(int charTurn) {
        String roundResults = "";                                   //Buffer that we store the results of each fight
        charTurn = charTurn % battlefieldCharacters.size();
        Entity entity = battlefieldCharacters.get(charTurn);        //Current entities turn
        if (entity.getHp() <= 0) {return null;}                     //If the entity is dead we don't do anything
        String result = "Turn of: " + entity.getName();             //First thing to add is players turn message
        Entity target;                                              //Target that the entity is attacking to

        int actionCounter = 0;
        while (!result.contains(ALL_ACTIONS_DONE)) {
            roundResults = roundResults.concat(result + "\n");      //We add the fight results to the results buffer
            if (checkBattleEnded()) {return roundResults;}
            target = chooseTarget(entity);                          //Select random target enemy, based of the instance type of entity
            result = entity.doAction(target, actionCounter);
            actionCounter++;
            if (target.getHp() <= 0) {
                //We do remove of both!! if it doesn't exist shouldn't delete anything :D
                monstersTracker.remove(Integer.valueOf(battlefieldCharacters.indexOf(target)));
                playersTracker.remove(Integer.valueOf(battlefieldCharacters.indexOf(target)));
                result = result.concat("\n" + entity.getName() + " killed " + target.getName());
            }
        }
        checkBattleEnded(); //Double check for fled monsters;
        roundResults = roundResults.concat(entity.classAbility(battlefieldCharacters, monstersTracker));
        return roundResults;
    }

    public ArrayList<Entity> getBattlefield()
    {
        return battlefieldCharacters;
    }

}
