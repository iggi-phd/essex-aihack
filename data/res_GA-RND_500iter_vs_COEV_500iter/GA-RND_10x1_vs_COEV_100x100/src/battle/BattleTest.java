package battle;

import battle.controllers.Human.ArrowsController;
import battle.controllers.Human.WASDController;
import battle.controllers.diego.BattleEvoController;
import battle.controllers.diego.search.*;
import battle.controllers.diego.strategy.*;
import battle.controllers.nullController.NullController;
import battle.controllers.random.RandomController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

/**
 * Created by simon lucas on 10/06/15.
 */
public class BattleTest {
    BattleView view;

    static final int COEV = 0;
    static final int GA = 1;
    static final int RND = 2;
    static final int NULL = 3;
    static final int WASD = 4;
    static final int ARROWS = 5;
    static final int MAX_TICKS_GAME = 100;
    static final int NUM_GAMES_TO_PLAY = 100;

    public static void main(String[] args) {
        //playOne(BattleTest.WASD, BattleTest.ARROWS);

        //playOne(BattleTest.GA, BattleTest.RND);
        Search.NUM_ACTIONS_INDIVIDUAL = 10;
        Search.MACRO_ACTION_LENGTH = 1;
        Search.NUM_EVAL = 500;
        playN(BattleTest.GA, BattleTest.COEV, "../data/GA-RND_10x1_vs_COEV_100x100.txt");
    }

    public static void playOne(int ply1, int ply2)
    {
        boolean visuals = true;
        SimpleBattle battle = new SimpleBattle(visuals, MAX_TICKS_GAME);
        BattleController p1 = createPlayer(ply1);
        BattleController p2 = createPlayer(ply2);

        double []res = battle.playGame(p1, p2);
    }

    public static void playN(int ply1, int ply2, String filename)
    {
        boolean visuals = false;
        double[][] results = new double[NUM_GAMES_TO_PLAY][MAX_TICKS_GAME];

        for(int i = 0; i < NUM_GAMES_TO_PLAY; ++i) {

            SimpleBattle battle = new SimpleBattle(visuals, MAX_TICKS_GAME);
            BattleController p1 = createPlayer(ply1);
            BattleController p2 = createPlayer(ply2);

            double []res = battle.playGame(p1, p2);
            System.arraycopy(res, 0, results[i], 0, MAX_TICKS_GAME);
        }

        System.out.println("Done.");
        dump(results, filename);
    }

    private static void dump(double[][] results, String filename)
    {
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));

            for (int i = 0; i < results.length; ++i) {
                for (int j = 0; j < results[i].length; ++j) {
                    writer.write(results[i][j] + ",");
                }
                writer.write("\n");
            }

            writer.close();

        }catch(Exception e)
        {
            System.out.println("MEH: " + e.toString());
            e.printStackTrace();
        }
    }


    public static BattleController createPlayer(int ply)
    {
        Random rnd1 = new Random();

        switch (ply)
        {
            case BattleTest.COEV:
                return new BattleEvoController(new CoevSearch(
                        new UniformCrossover(rnd1),
                        new PMutation(rnd1, 0.1),
                        new TournamentSelection(rnd1, 3),
                        new RandomPairing(rnd1, 3),
                        rnd1));

            case BattleTest.GA:
                return new BattleEvoController(new GASearch(
                        new UniformCrossover(rnd1),
                        new PMutation(rnd1, 0.1),
                        new TournamentSelection(rnd1, 3),
                        //new NullOpponentGenerator(Search.NUM_ACTIONS_INDIVIDUAL),
                        new RndOpponentGenerator(rnd1),
                        rnd1));

            case BattleTest.NULL:
                return new NullController();
            case BattleTest.RND:
                return new RandomController(rnd1);
            case BattleTest.WASD:
                return new WASDController();
            case BattleTest.ARROWS:
                return new ArrowsController();
        }

        return new ArrowsController();

    }


}
