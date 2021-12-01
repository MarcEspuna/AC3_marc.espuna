package Business;

import Persistance.MonsterDAO;
import Persistance.PlayerDAO;

import java.io.FileNotFoundException;
import java.util.*;

public class SimManager {

    static private final String ALL_ACTIONS_DONE = "over";
    private ArrayList<Monster> monsters;                        //All monsters loaded from Json
    private ArrayList<Player> players;                          //All players loaded from Json
    private final ArrayList<Entity> battlefieldCharacters;      //ALl the characters that will play in the battlefield
    private final ArrayList<Integer> monstersTracker;           //Contains the index of the active monsters
    private final ArrayList<Integer> playersTracker;            //Contains the index of the active players
    private final PlayerDAO playerDAO;
    private final MonsterDAO monsterDAO;
    private final Random random;
    private final ArrayList<String> roundResults;


    public SimManager(PlayerDAO playerDAO, MonsterDAO monsterDAO)
    {
        this.playerDAO = playerDAO;
        this.monsterDAO = monsterDAO;
        random = new Random();
        battlefieldCharacters = new ArrayList<>();
        roundResults = new ArrayList<>();
        monstersTracker = new ArrayList<>();
        playersTracker = new ArrayList<>();
    }

    //IT loads up the simulation data from the Json files.
    //Returns true if successful or false if the files are not found.
    public boolean loadSimulation() {
        try {
            players = playerDAO.readPlayers();
            monsters = monsterDAO.readMonsters();
        }
        catch (FileNotFoundException io) { return false;}
        return true;
    }


    public void startBattle(HashMap<String, Integer> selectedMonsters) {
        //Generate a list of all the characters that will appear in the battlefield
        generateCharacters(selectedMonsters);
        //We sort the list based of the random points that have been assigned
        initiativeList();
        //We set the index arrays to track witch characters are monsters and which are players as well as knowing they are alive
        setTrackers();

    }

    public boolean nextFight(int charTurn)
    {
        clearResults();
        charTurn = charTurn % battlefieldCharacters.size();
        Entity entity = battlefieldCharacters.get(charTurn);
        if(checkBattleEnded()) { return true;}
        else if (entity.getHp() <= 0) {return false;}
        if (entity instanceof Player)
        {

            String result = "Turn of: " + entity.getName();
            int actionCounter = 0;
            while (!result.equals(ALL_ACTIONS_DONE))
            {
                addResults(result);
                if(checkBattleEnded()) { return true;}
                Monster monster = chooseMonsterTarget();
                result = entity.doAction(monster, actionCounter);
                actionCounter++;
                if (monster.getHp() <= 0) {
                    monstersTracker.remove(Integer.valueOf(battlefieldCharacters.indexOf(monster)));
                    result = result.concat("\nPlayer " + entity.getName() + " killed " + monster.getName());
                }
            }

        } else {

            String result = "Turn of: " + entity.getName();
            int actionCounter = 0;
            while (!result.equals(ALL_ACTIONS_DONE))
            {
                addResults(result);
                if(checkBattleEnded()) { return true;}
                Player player = choosePlayerTarget();
                result = entity.doAction(player, actionCounter);
                actionCounter++;
                if (player.getHp() <= 0) {
                    playersTracker.remove(Integer.valueOf(battlefieldCharacters.indexOf(player)));
                    result = result.concat("\nMonster " + entity.getName() + " killed " + player.getName());
                }
            }
        }
        return false;
    }

    private Player choosePlayerTarget() {
        int playerIndex;
        if(playersTracker.size() > 1) playerIndex = playersTracker.get(random.nextInt(playersTracker.size()-1));
        else { playerIndex = playersTracker.get(0);}
        return (Player) battlefieldCharacters.get(playerIndex);
    }

    private Monster chooseMonsterTarget() {
        int monsterIndex;
        if(monstersTracker.size() > 1) monsterIndex = monstersTracker.get(random.nextInt(monstersTracker.size()-1));
        else { monsterIndex = monstersTracker.get(0);}
        return (Monster)battlefieldCharacters.get(monsterIndex);
    }

    private boolean checkBattleEnded() {
        return (playersTracker.size() == 0 || monstersTracker.size() == 0);
    }

    public ArrayList<String> getResults() {return roundResults;}
    public void clearResults() {roundResults.clear();}

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

    private void generateCharacters(HashMap<String, Integer> selectedMonsters)
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

    private void addMonsters(Monster monster, int numberMonsters)
    {
        for (int i = 0; i < numberMonsters; i++)
        {
            Monster newMonster = new Monster(monster);
            newMonster.addNumberToName(i+1);
            battlefieldCharacters.add(newMonster);
        }
    }
    private void addResults(String result) {roundResults.add(result);}


    public ArrayList<Entity> getBattlefield() {
        return battlefieldCharacters;
    }
}
