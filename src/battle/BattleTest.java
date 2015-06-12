package battle;



import battle.controllers.*;

import javax.swing.*;

import asteroids.Action;

import battle.controllers.EmptyController;
import battle.controllers.FireForwardController;
import battle.controllers.Human.WASDController;
import battle.controllers.webpigeon.StaticEvolver;
import battle.controllers.webpigeon.StupidGAWrapper;


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
        
        BattleController player2 = new FireForwardController();

        BattleController player1 = new WASDController();

        battle.playGame(player1, player2);

    }

}
