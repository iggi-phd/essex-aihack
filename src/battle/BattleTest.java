package battle;

import battle.controllers.EmptyController;
import battle.controllers.sarsa.SarsaController;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void main(String[] args) {

        SimpleBattle battle = new SimpleBattle();

        BattleController player1 = new EmptyController();
        BattleController player2 = new SarsaController();
        battle.playGame(player1, player2);
    }

}
