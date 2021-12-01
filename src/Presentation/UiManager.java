package Presentation;

import Business.Entity;
import Business.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class UiManager {

    static private final String MAIN_MENU_MESSAGE =  "1:Simulate\n" + "2:Exit";
    static private final String FILE_NOT_FOUND = "Players Json file not found.";
    static private final String DELIMITER_MONSTERS = ";";
    static private final String DELIMITER_NAME_NUMBER = "-";
    static private final String SIMULATION_SETTINGS =   "Please insert simulation settings: \n" +
                                                        "Example: numMonster1-Nom1;numMonster2-Nom2;...;numMonsterN-NomN";
    static private final String WRONG_INPUT_NUMBER_OF_MONSTERS = "Not valid input in number of monsters.";
    static private final String WRONG_SEPERATOR = "Wrong number to name of monsters seperator.";
    static private final String EXIT_COMMAND = "exit";
    static private final String EXIT_MESSAGE = "Shutting down...";
    private static final String SIMULATION_RESULT = "Simulation result:";


    BufferedReader reader;

    public UiManager()
    {
        reader =  new BufferedReader(new InputStreamReader(System.in));
    }

    public void showMainMenu()
    {
        System.out.println(MAIN_MENU_MESSAGE);
    }

    public int askForInputInt(int maxNumber) {
        // Enter data using BufferReader
        String name;
        while (true) {
            try {
                // Reading data using readLine
                System.out.print("Input: ");
                name = reader.readLine();
                try
                {
                    int value = Integer.parseInt(name);
                    if (value <= maxNumber) return value;
                    else {
                        System.out.println("Input not valid.");
                    }
                }
                catch (NumberFormatException io)
                {
                    System.out.println("This is not an integer.");
                }
            } catch (IOException io) {
                System.out.println("IOException.");
            }
        }
    }

    private boolean parseSimulationInput(String simulationInput, HashMap<String,Integer> selectedMonsters)
    {
        String[] monsters = simulationInput.split(DELIMITER_MONSTERS);
        if (monsters.length>0) {
            for (String monster : monsters) {
                String[] monsterParse = monster.split(DELIMITER_NAME_NUMBER);
                if (monsterParse.length == 2) {
                    int monsterQuantity;
                    try {
                        monsterQuantity = Integer.parseInt(monsterParse[0]);
                    } catch (NumberFormatException io) {
                        System.out.println(WRONG_INPUT_NUMBER_OF_MONSTERS);
                        return false;
                    }
                    selectedMonsters.put(monsterParse[1], monsterQuantity);
                }
                else {
                    System.out.println(WRONG_SEPERATOR);
                    System.out.println(); return false;}
            }
            return true;
        }
        else {
            System.out.println(WRONG_SEPERATOR);
            return false;
        }
    }

    public void fileNotFoundError() {
        System.out.println(FILE_NOT_FOUND);
    }

    public boolean simulationInput(HashMap<String,Integer> selectedMonsters, Boolean[] exitCommand) {
        String simulationInput = null;
        try {
            // Reading data using readLine
            System.out.print("Input: ");
            simulationInput = reader.readLine();

        } catch (IOException io) {
            System.out.println("IOException.");
            return false;
        }
        if (simulationInput.equals(EXIT_COMMAND))
        {
            exitCommand[0] = true;
            return true;
        }
        return parseSimulationInput(simulationInput, selectedMonsters);
    }

    public void askForSimulationInput() {
        System.out.println(SIMULATION_SETTINGS);
    }

    public void exitMessage() {
        System.out.println(EXIT_MESSAGE);
    }

    public void showResults(ArrayList<String> results) {
        if (results.size() == 0) {return;}
        System.out.println();
        for (String result : results) {
            System.out.println(result);
        }
    }

    public void showSimulationResults(ArrayList<Entity> battlefield) {
        ArrayList<String> results = new ArrayList<>();
        for (Entity entity : battlefield) {
            if (entity instanceof Player)
            {
                String result = "Name: " + entity.getName() + " hp: " + entity.getHp() + " xp: " +
                        entity.getXp() + " gold: " + entity.getGold() + " potion charges: " + ((Player)entity).getPotionCharges();
                results.add(result);
            }
        }

        System.out.println();
        System.out.println(SIMULATION_RESULT);
        for (String result : results) {
            System.out.println(result);
        }
    }
}