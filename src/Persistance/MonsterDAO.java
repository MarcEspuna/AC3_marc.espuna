package Persistance;

import Business.Monster;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class MonsterDAO {






    public ArrayList<Monster> readMonsters()
            throws FileNotFoundException
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Reader reader = new FileReader("JsonFiles/Monsters.json");

        Monster[] monsters = gson.fromJson(reader, Monster[].class);

        return new ArrayList<Monster>(Arrays.asList(monsters));
    }


}
