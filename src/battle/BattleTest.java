package battle;

import battle.controllers.diego.BattleEvoController;
import battle.controllers.diego.search.CoevSearch;
import battle.controllers.diego.search.GASearch;
import battle.controllers.diego.search.RandomSearch;
import battle.controllers.diego.strategy.PMutation;
import battle.controllers.diego.strategy.RandomPairing;
import battle.controllers.diego.strategy.TournamentSelection;
import battle.controllers.diego.strategy.UniformCrossover;
import battle.controllers.nullController.NullController;

import java.util.Random;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;


    public static void main(String[] args) {

        int maxTicksGame = 100;
        int numGamesToPlay = 5;
        boolean visible = false;
        double[][] results = new double[numGamesToPlay][maxTicksGame];

        for(int i = 0; i < numGamesToPlay; ++i) {

            SimpleBattle battle = new SimpleBattle(visible, maxTicksGame);
            BattleController p1 = createPlayer1();
            BattleController p2 = createPlayer2();

            double []res = battle.playGame(p1, p2);
            System.arraycopy(res, 0, results[i], 0, maxTicksGame);
        }


        for(int i = 0; i < results.length; ++i)
        {
            for(int j = 0; j < results[i].length; ++j) {
                System.out.printf("%.3f,", results[i][j]);
            }
            System.out.println();
        }
    }



    public static BattleController createPlayer1()
    {
        Random rnd1 = new Random();
        return new BattleEvoController(new CoevSearch(
                new UniformCrossover(rnd1),
                new PMutation(rnd1, 0.1),
                new TournamentSelection(rnd1, 3),
                new RandomPairing(rnd1, 3),
                rnd1));
    }

    public static BattleController createPlayer2()
    {
        Random rnd2 = new Random();
        //BattleEvoController player1 = new BattleEvoController(new RandomSearch(rnd1));
        //battle.BattleController player2 = new BattleEvoController(new RandomSearch(rnd2));

        //battle.BattleController player1 = new BattleEvoController(new GASearch(  new UniformCrossover(rnd1),
        //                                                                new PMutation(rnd1, 0.1),
        //                                                                new TournamentSelection(rnd1, 3),
        //                                                                rnd1));
        return new NullController();
    }

}
