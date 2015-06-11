package battle;

import javax.swing.*;

import asteroids.Action;
import battle.controllers.EmptyController;
<<<<<<< HEAD
import battle.controllers.MCTSController;
=======
import battle.controllers.FireController;
import battle.controllers.FireForwardController;
import battle.controllers.RotateAndShoot;
>>>>>>> origin/master
import math.Vector2d;
import utilities.JEasyFrame;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void main(String[] args) {

        SimpleBattle battle = new SimpleBattle();

        BattleController player1 = new EmptyController();
<<<<<<< HEAD
        BattleController player2 = new MCTSController();
=======
        BattleController player2 = new FireForwardController();
>>>>>>> origin/master
        battle.playGame(player1, player2);
    }

}
