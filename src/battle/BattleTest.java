package battle;



import battle.controllers.*;


/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void main(String[] args) {

        JoeCSimpleController battle = new JoeCSimpleController();

       BattleController player1 = new PlayerKeyController();

       battle.addKeyListener(  ((PlayerKeyController) player1).getKeyHandler());
       /* BattleController fire2 = new SimpleController();
        battle.playGame(fire1, fire2);*/

        //BattleController player1 = new WASDController();
        BattleController player2 = new SimpleController();



        battle.playGame(player1, player2);

    }

}
