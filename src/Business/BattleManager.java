package Business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class BattleManager {
    private static final String ALL_ACTIONS_DONE = "over";
    private final ArrayList<Entity> battlefieldCharacters;      //ALl the characters that will play in the battlefield
    private final ArrayList<Integer> monstersTracker;           //Contains the index of the active monsters
    private final ArrayList<Integer> playersTracker;            //Contains the index of the active players
    private String roundResults;
    private final Random random;

    BattleManager() {
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
        return (playersTracker.size() == 0 || monstersTracker.size() == 0);
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
        if (entity.getHp() <= 0) {
            return null;
        }                     //If the entity is dead we don't do anything
        String result = "Turn of: " + entity.getName();             //First thing to add is players turn message
        Entity target;                                              //Target that the entity is attacking to
        //We do remove of both!! if it doesn't exist shouldn't delete anything! check
        int actionCounter = 0;
        while (!result.equals(ALL_ACTIONS_DONE)) {
            if (checkBattleEnded()) {
                return roundResults;
            }
            roundResults = roundResults.concat(result + "\n");
            target = chooseTarget(entity);                //FIX
            result = entity.doAction(target, actionCounter); //FIX!
            actionCounter++;
            if (target.getHp() <= 0) {
                monstersTracker.remove(Integer.valueOf(battlefieldCharacters.indexOf(target)));
                playersTracker.remove(Integer.valueOf(battlefieldCharacters.indexOf(target)));
                roundResults = result.concat("\n" + entity.getName() + " killed " + target.getName());
            }
        }
        entity.classAbility();
        return roundResults;
    }
}
