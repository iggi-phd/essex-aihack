package battle;

import battle.controllers.*;
import battle.controllers.FireForwardController;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void main(String[] args) {

        SimpleBattle battle = new SimpleBattle();

        BattleController player1 = new DaveController();
        BattleController player2 = new FireForwardController();

        //BattleController player1 = new WASDController();
        //BattleController player2 = new StupidGAWrapper(new double[]{2.7631328506251744, 0.746687716615824, 0.11574670823251669});

        battle.playGame(player1, player2);
    }

}
