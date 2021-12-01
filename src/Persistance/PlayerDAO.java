package Persistance;

import Business.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class PlayerDAO {


    public ArrayList<Player> readPlayers()
    throws FileNotFoundException
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Reader reader = new FileReader("JsonFiles/Players.json");

        Player[] players = gson.fromJson(reader, Player[].class);

        return organizePlayers(players);
    }

    private ArrayList<Player> organizePlayers(Player[] players)
    {
        ArrayList<Player> allPlayers = new ArrayList<>();
        for (Player player : players) {
            switch (player.classType()) {
                case "Fighter" -> {
                    Fighter fighter = new Fighter(player);
                    allPlayers.add(fighter);
                }
                case "Rogue" -> {
                    Rogue rogue = new Rogue(player);
                    allPlayers.add(rogue);
                }
                case "Cleric" -> {
                    Cleric cleric = new Cleric(player);
                    allPlayers.add(cleric);
                }
                case "Mage" -> {
                    Mage mage = new Mage(player);
                    allPlayers.add(mage);
                }
                default -> {
                    allPlayers.add(player);
                }
            }
        }
    return allPlayers;
    }



}
