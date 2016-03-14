package battle.controllers.diego.search;

import battle.SimpleBattle;
import battle.controllers.diego.ActionMap;
import battle.controllers.diego.strategy.ICoevPairing;
import battle.controllers.diego.strategy.ICrossover;
import battle.controllers.diego.strategy.IMutation;
import battle.controllers.diego.strategy.ISelection;

import java.util.Random;

/**
 * PTSP-Competition
 * Random Search engine. Creates random paths and determines which one is the best to execute according to an heuristic.
 * It keeps looking during MACRO_ACTION_LENGTH time steps. After that point, the search is reset.
 * Created by Diego Perez, University of Essex.
 * Date: 17/10/12
 */
public class CoevSearch extends Search {

    /**
     * Current game state
     */
    public static SimpleBattle m_currentGameState;

    /**
     * Game state used to roll actions and evaluate  the current path.
     */
    public static SimpleBattle m_futureGameState;


    public static int TOURNAMENT_SIZE = 3;
    public final int ELITISM = 2;
    public final int NUM_INDIVIDUALS = 10;


    public GAIndividual[] m_individuals;

    public GAIndividual[] m_individualsOpp;


    ICrossover cross;
    IMutation mut;
    ISelection sel;
    ICoevPairing pair;
    public int m_numGenerations;


    /**
     * Constructor of the random search engine.
     */
    public CoevSearch(ICrossover ic, IMutation im, ISelection is, ICoevPairing icp, Random rnd)
    {
        super(rnd);
        cross = ic;
        mut = im;
        sel = is;
        pair = icp;
    }

    /**
     * Initializes the random search engine. This function is also called to reset it.
     */
    @Override
    public void init(SimpleBattle gameState, int playerId)
    {
        m_individuals = new GAIndividual[NUM_INDIVIDUALS];
        m_individualsOpp = new GAIndividual[NUM_INDIVIDUALS];
        this.playerID = playerId;

        for(int i = 0; i < NUM_INDIVIDUALS; ++i) {
            m_individuals[i] = new GAIndividual(Search.NUM_ACTIONS_INDIVIDUAL, playerID);
            m_individuals[i].randomize(m_rnd, ActionMap.ActionMap.length);

            m_individualsOpp[i] = new GAIndividual(Search.NUM_ACTIONS_INDIVIDUAL, playerID);
            m_individualsOpp[i].randomize(m_rnd, ActionMap.ActionMap.length);
        }

        //We need to evaluate once the opp population is complete.
        for(int i = 0; i < NUM_INDIVIDUALS; ++i)
        {
            pair.evaluate(gameState, m_individuals[i], m_individualsOpp);
        }

        sortPopulationByFitness(m_individuals);
        sortPopulationByFitness(m_individualsOpp);

        //Resetting the random paths found and best fitness.
        m_bestRandomPath = new int[NUM_ACTIONS_INDIVIDUAL];
        m_currentRandomPath = new int[NUM_ACTIONS_INDIVIDUAL];
        m_bestFitnessFound = -1;
        m_numGenerations = 0;
    }



    /**
     * Runs the Random Search engine for one cycle.
     * @param a_gameState Game state where the macro-action to be decided must be executed from.
     * @return  the action decided to be executed.
     */
    @Override
    public int run(SimpleBattle a_gameState)
    {
        m_currentGameState = a_gameState;
        int numIters = 0;

        //check that we don't overspend
        while(numIters < 500)
        {
            //OPPONENT POPULATION: prepare the next generation (no evaluation nor sorting yet!).
            GAIndividual[] nextOppPop = new GAIndividual[m_individualsOpp.length];

            int i;
            for(i = 0; i < ELITISM; ++i)  nextOppPop[i] = m_individualsOpp[i];

            for(;i<m_individualsOpp.length;++i)
            {
                nextOppPop[i] = breed(m_individualsOpp);
                mut.mutate(nextOppPop[i]);
            }
            m_individualsOpp = nextOppPop;

            //SOURCE POPULATION: create the next generation and evaluate against new individuals from
            // the opponent population. This also evaluates those.
            GAIndividual[] nextPop = new GAIndividual[m_individuals.length];

            for(i = 0; i < ELITISM; ++i)  nextPop[i] = m_individuals[i];

            for(;i<m_individuals.length;++i)
            {
                nextPop[i] = breed(m_individuals);
                mut.mutate(nextPop[i]);
                pair.evaluate(a_gameState, nextPop[i], m_individualsOpp);
            }
            m_individuals = nextPop;

            //and sort both populations by fitness for the next iteration
            sortPopulationByFitness(m_individuals);
            sortPopulationByFitness(m_individualsOpp);

            m_numGenerations++;
            numIters++;
        }

        return m_individuals[0].m_genome[0];
    }





    private GAIndividual breed(GAIndividual[] pop)
    {
        GAIndividual gai1 = sel.getParent(pop, null);        //First parent.
        GAIndividual gai2 = sel.getParent(pop, gai1);        //Second parent.
        return cross.uniformCross(gai1, gai2);
    }

    /**
     * Provides an heuristic score of the game state m_futureGameState.
     * @return the score.
     */
    @Override
    public double scoreGame(SimpleBattle game)
    {
        return game.score(playerID); //game.getPoints(playerID);
    }

    /**
     * Prints a population, including fitness.
     */
    private void printPopulation(GAIndividual[] pop)
    {
        for(int i=0;i<m_individuals.length;++i)
        {
            pop[i].print();
        }
    }




}
