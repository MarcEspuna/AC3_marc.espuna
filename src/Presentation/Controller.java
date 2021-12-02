package Presentation;

import Business.SimManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    private static final int START_SIMULATION = 1;
    private static final int EXIT = 2;

    UiManager uiManager;
    SimManager sManager;

    public Controller(UiManager uiManager, SimManager sManager)
    {
        this.uiManager = uiManager;
        this.sManager = sManager;
    }

    public void run()
    {
        //Execute the option util option exit is pressed
        int option;
        do {
            uiManager.showMainMenu();
            option = uiManager.askForInputInt(2);
            runOption(option);
        } while (option != EXIT);
    }

    private void runOption(int option)
    {
        if (option == START_SIMULATION)
        {
            //Load the simulation with the players and monsters json files
            boolean successfulJsonLoad = sManager.loadSimulation();
            if(successfulJsonLoad)
            {
                HashMap<String,Integer> selectedMonsters = new HashMap<>();
                uiManager.askForSimulationInput();
                boolean successfulParse;
                Boolean[] exitCommand = {false};
                do {
                    successfulParse = uiManager.simulationInput(selectedMonsters, exitCommand);
                    if (exitCommand[0]) return;
                }while (!successfulParse);

                boolean fightEnded = false;
                int fightRound = 0;
                sManager.startBattle(selectedMonsters);
                while (!fightEnded)
                {
                    fightEnded = sManager.nextFight(fightRound);
                    uiManager.showResults(sManager.getResults());
                    fightRound++;
                }
                uiManager.showSimulationResults(sManager.getBattlefield());

                if (uiManager.askForWritingResults())
                {
                    String file = uiManager.getStringInput();
                    sManager.writeResultsTo(file);
                }


            }
            else{
                uiManager.fileNotFoundError();
            }
        }
        else if (option == EXIT)
        {
            uiManager.exitMessage();
        }
    }


}
