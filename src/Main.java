import Business.SimManager;
import Persistance.MonsterDAO;
import Persistance.PlayerDAO;
import Presentation.Controller;
import Presentation.UiManager;

public class Main {


    public static void main(String[] args) {

        MonsterDAO monsterDAO = new MonsterDAO();
        PlayerDAO playerDAO = new PlayerDAO();
        UiManager uiManager = new UiManager();
        SimManager simManager = new SimManager(playerDAO, monsterDAO);
        Controller controller = new Controller(uiManager, simManager);
        controller.run();

    }







}
