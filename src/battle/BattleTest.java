package battle;

import javax.swing.*;

import asteroids.Action;
import battle.controllers.*;
import math.Vector2d;
import utilities.JEasyFrame;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void main(String[] args) {

        SimpleBattle battle = new SimpleBattle();

        BattleController player1 = new DaveController();
        BattleController player2 = new FireForwardController();
        battle.playGame(player1, player2);
    }

}
