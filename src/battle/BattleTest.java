package battle;

import battle.controllers.*;

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
    }

}
