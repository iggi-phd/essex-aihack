package battle;

import battle.controllers.Human.ArrowsController;
import battle.controllers.Human.WASDController;
import battle.controllers.diego.BattleEvoController;
import battle.controllers.diego.search.CoevSearch;
import battle.controllers.diego.search.GASearch;
import battle.controllers.diego.search.RandomSearch;
import battle.controllers.diego.strategy.PMutation;
import battle.controllers.diego.strategy.RandomPairing;
import battle.controllers.diego.strategy.TournamentSelection;
import battle.controllers.diego.strategy.UniformCrossover;
import battle.controllers.nullController.NullController;
import battle.controllers.random.RandomController;

import java.util.Random;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    public static void playN()
    {
        int maxTicksGame = 100;
        int numGamesToPlay = 100;
        boolean visuals = false;
        double[][] results = new double[numGamesToPlay][maxTicksGame];

        for(int i = 0; i < numGamesToPlay; ++i) {

            SimpleBattle battle = new SimpleBattle(visuals, maxTicksGame);
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


    public static void main(String[] args) {
        playOne();
        //playN();
    }


    public static void playOne()
    {
        int maxTicksGame = 1000;
        boolean visuals = true;
        SimpleBattle battle = new SimpleBattle(visuals, maxTicksGame);
        BattleController p1 = createPlayer1();
        BattleController p2 = createPlayer2();

        double []res = battle.playGame(p1, p2);
    }

    public static BattleController createPlayer1()
    {
        Random rnd1 = new Random();

//        return new BattleEvoController(new CoevSearch(
//                new UniformCrossover(rnd1),
//                new PMutation(rnd1, 0.1),
//                new TournamentSelection(rnd1, 3),
//                new RandomPairing(rnd1, 3),
//                rnd1));

//        return new BattleEvoController(new GASearch(
//                new UniformCrossover(rnd1),
//                new PMutation(rnd1, 0.1),
//                new TournamentSelection(rnd1, 3),
//                rnd1));

//        return new NullController();
          return new WASDController();
    }

    public static BattleController createPlayer2()
    {
        Random rnd2 = new Random();

//        return new BattleEvoController(new CoevSearch(
//                new UniformCrossover(rnd2),
//                new PMutation(rnd2, 0.1),
//                new TournamentSelection(rnd2, 3),
//                new RandomPairing(rnd2, 3),
//                rnd2));

//        return new BattleEvoController(new GASearch(
//                new UniformCrossover(rnd2),
//                new PMutation(rnd2, 0.1),
//                new TournamentSelection(rnd2, 3),
//                rnd2));

        //return new RandomController(rnd2);

//        return new NullController();
         return new ArrowsController();

//        return new WASDController();
    }

}
