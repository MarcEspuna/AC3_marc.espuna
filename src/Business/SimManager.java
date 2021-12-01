package Business;

import Persistance.MonsterDAO;
import Persistance.PlayerDAO;

import java.io.FileNotFoundException;
import java.util.*;

public class SimManager {

    private final BattleManager battleManager;
    private ArrayList<Monster> monsters;                        //All monsters loaded from Json
    private ArrayList<Player> players;                          //All players loaded from Json
    private final PlayerDAO playerDAO;
    private final MonsterDAO monsterDAO;
    private final ArrayList<String> roundResults;


    public SimManager(PlayerDAO playerDAO, MonsterDAO monsterDAO)
    {
        this.playerDAO = playerDAO;
        this.monsterDAO = monsterDAO;
        roundResults = new ArrayList<>();
        battleManager = new BattleManager();
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
        battleManager.generateCharacters(selectedMonsters, monsters, players);
        //We sort the list based of the random points that have been assigned
        battleManager.initiativeList();
        //We set the index arrays to track witch characters are monsters and which are players as well as knowing they are alive
        battleManager.setTrackers();

    }


    public boolean nextFight(int charTurn)
    {
        clearResults();
        String battleResults = battleManager.fight(charTurn);
        addResults(battleResults);
        return battleManager.checkBattleEnded();
    }


    public ArrayList<String> getResults() {return roundResults;}
    public void clearResults() {roundResults.clear();}
    private void addResults(String result) {roundResults.add(result);}

    public ArrayList<Entity> getBattlefield() {
        return battleManager.getBattlefield();
    }
}
