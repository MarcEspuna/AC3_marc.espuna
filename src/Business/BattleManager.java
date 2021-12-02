package Business;

import java.lang.annotation.Target;
import java.util.*;

public class BattleManager {
    private static final String ALL_ACTIONS_DONE = "over";
    private final ArrayList<Entity> battlefieldCharacters;      //ALl the characters that will play in the battlefield
    private final ArrayList<Integer> monstersTracker;           //Contains the index of the active monsters
    private final ArrayList<Integer> playersTracker;            //Contains the index of the active players
    private String roundResults;
    private final Random random;



    public BattleManager() {
        battlefieldCharacters = new ArrayList<>();
        monstersTracker = new ArrayList<>();
        playersTracker = new ArrayList<>();
        random = new Random();
    }

    private void swap (ArrayList<Entity> arr, int index1, int index2)
    {
        Entity tmp = arr.get(index1);
        arr.set(index1, arr.get(index2));
        arr.set(index2, arr.get(index1));
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

        for (Entity battlefieldCharacter : battlefieldCharacters) {
            System.out.println("Character: " + battlefieldCharacter.getName() + " Point: " + battlefieldCharacter.randomNumber);
        }

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

        for (Entity battlefieldCharacter : battlefieldCharacters) {
            System.out.println("Character: " + battlefieldCharacter.getName() + " Point: " + battlefieldCharacter.randomNumber);
        }

    }

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

    public void addMonsters(Monster monster, int numberMonsters) {
        for (int i = 0; i < numberMonsters; i++) {
            Monster newMonster = new Monster(monster);
            newMonster.addNumberToName(i + 1);
            battlefieldCharacters.add(newMonster);
        }
    }

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


    private Player choosePlayerTarget() {
        int playerIndex;
        if (playersTracker.size() > 1) playerIndex = playersTracker.get(random.nextInt(playersTracker.size() - 1));
        else {
            playerIndex = playersTracker.get(0);
        }
        return (Player) battlefieldCharacters.get(playerIndex);
    }

    private Monster chooseMonsterTarget() {
        int monsterIndex;
        if (monstersTracker.size() > 1) monsterIndex = monstersTracker.get(random.nextInt(monstersTracker.size() - 1));
        else {
            monsterIndex = monstersTracker.get(0);
        }
        return (Monster) battlefieldCharacters.get(monsterIndex);
    }

    public boolean checkBattleEnded() {
        //Update monsters that have left the battlefield
        for (int i = 0; i < monstersTracker.size(); i++)
        {
            Monster monster = (Monster)battlefieldCharacters.get(monstersTracker.get(i));
            if (monster.hasFled()) {monstersTracker.remove(Integer.valueOf(battlefieldCharacters.indexOf(monster)));}
        }

        return (playersTracker.size() == 0 || monstersTracker.size() == 0);
        //Check
    }

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

    public String fight(int charTurn) {
        roundResults = "";
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
