package battle.controllers.diego.search;

import battle.SimpleBattle;

import java.util.Random;

/**
 * Created by dperez on 08/07/15.
 */
public abstract class Search {

    /**
     * Number of macro-actions that form the random path.
     */
    public static int NUM_ACTIONS_INDIVIDUAL = 5; //8
    /**
     * Number of single actions that form a macro action.
     */
    public static int MACRO_ACTION_LENGTH = 1; //15
    /**
     * Total number of evaluaitons can be used
     */
    public static int NUM_EVAL = 1500;

    /**
     * ID of ths player.
     */
    public int playerID;
    /**
     * Best individual (route) found in the current search step.
     */
    public int[] m_bestRandomPath;
    /**
     * Best heuristic cost of the best individual
     */
    public double m_bestFitnessFound;
    /**
     * Next generated individual to be evaluated
     */
    public int[] m_currentRandomPath;
    /**
     * Random number generator
     */
    public Random m_rnd;

    public Search(Random rnd) {
        m_rnd = rnd;
    }

    abstract public void init(SimpleBattle gameState, int playerID);

    abstract public int run(SimpleBattle a_gameState);

    abstract public double scoreGame(SimpleBattle game);


    private void printGenome(int []path)
    {
        for(int p : path)
        {
            System.out.print(p);
        }
        System.out.println();
    }


    protected void sortPopulationByFitness(GAIndividual[] population) {
        for (int i = 0; i < population.length; i++) {
            for (int j = i + 1; j < population.length; j++) {
                if (population[i].getFitness() < population[j].getFitness()) {
                    GAIndividual gcache = population[i];
                    population[i] = population[j];
                    population[j] = gcache;
                }
            }
        }
    }
}
