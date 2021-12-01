package Business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class BattleManager {
    private final ArrayList<Entity> battlefieldCharacters;      //ALl the characters that will play in the battlefield
    private final ArrayList<Integer> monstersTracker;           //Contains the index of the active monsters
    private final ArrayList<Integer> playersTracker;            //Contains the index of the active players
    private final ArrayList<String> roundResults;
    private final Random random;

    BattleManager()
    {
        battlefieldCharacters = new ArrayList<>();
        roundResults = new ArrayList<>();
        monstersTracker = new ArrayList<>();
        playersTracker = new ArrayList<>();
        random = new Random();
    }


    private void initiativeList()
    {
        //Generate random numbers for each character in the battlefield
        for (Entity character : battlefieldCharacters) {
            character.setRandomNumber(random.nextInt(19)+1);
        }

        //Lambda expression to sort based of the random value generated
        battlefieldCharacters.sort(Comparator.comparingInt(x -> x.randomNumber));

    }

    private void setTrackers() {
        playersTracker.clear();
        monstersTracker.clear();
        int counter = 0;
        for (Entity character : battlefieldCharacters) {
            if (character instanceof Player)
            {
                playersTracker.add(counter);
            }else
            {
                monstersTracker.add(counter);
            }
            counter++;
        }
    }

    public void addMonsters(Monster monster, int numberMonsters)
    {
        for (int i = 0; i < numberMonsters; i++)
        {
            Monster newMonster = new Monster(monster);
            newMonster.addNumberToName(i+1);
            battlefieldCharacters.add(newMonster);
        }
    }

    public void generateCharacters(HashMap<String, Integer> selectedMonsters, ArrayList<Monster> monsters, ArrayList<Player> players)
    {
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



    public Player choosePlayerTarget() {
        int playerIndex;
        if(playersTracker.size() > 1) playerIndex = playersTracker.get(random.nextInt(playersTracker.size()-1));
        else { playerIndex = playersTracker.get(0);}
        return (Player) battlefieldCharacters.get(playerIndex);
    }

    public Monster chooseMonsterTarget() {
        int monsterIndex;
        if(monstersTracker.size() > 1) monsterIndex = monstersTracker.get(random.nextInt(monstersTracker.size()-1));
        else { monsterIndex = monstersTracker.get(0);}
        return (Monster)battlefieldCharacters.get(monsterIndex);
    }

    public boolean checkBattleEnded() {
        return (playersTracker.size() == 0 || monstersTracker.size() == 0);
    }

}
