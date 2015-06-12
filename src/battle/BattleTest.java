package battle;


import battle.controllers.*;

import javax.swing.*;

import asteroids.Action;
import battle.controllers.EmptyController;
import battle.controllers.FireController;
import battle.controllers.FireForwardController;
import battle.controllers.RotateAndShoot;
import math.Vector2d;
import utilities.JEasyFrame;


/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void main(String[] args) {

        SimpleBattle battle = new SimpleBattle();


        BattleController fire1 = new PlayerKeyController();

       battle.addKeyListener(  ((PlayerKeyController) fire1).getKeyHandler());
        BattleController fire2 = new SimpleController();
        battle.playGame(fire1, fire2);

        BattleController player1 = new EmptyController();
        BattleController player2 = new FireForwardController();
        battle.playGame(player1, player2);

    }

}
