package battle.controllers.diego.search;

import battle.SimpleBattle;
import battle.controllers.diego.ActionMap;
import battle.controllers.diego.strategy.OpponentGenerator;

import java.util.Random;

public class OneStepLookAhead extends Search {

    /**
     * Current game state
     */
    public static SimpleBattle currentGameState;

    /**
     * Game state used to roll actions and evaluate the current path.
     */
    public static SimpleBattle futureGameState;


    public static int TOURNAMENT_SIZE = 3;
    public final int ELITISM = 2;

    public GAIndividual individual;
    public int m_numGenerations;
    public int numEvals;

    OpponentGenerator opponentGen;

    /**
     * Constructor of the engine.
     */
    public OneStepLookAhead(OpponentGenerator opponentGen, Random rnd)
    {
        super(rnd);
        this.opponentGen = opponentGen;
    }

    /**
     * Initializes the engine.
     * This function is also called to reset it.
     */
    @Override
    public void init(SimpleBattle gameState, int playerId)
    {
        this.numEvals = 0;
        this.playerID = playerId;
        GAIndividual opponent = opponentGen.getOpponent(1);
        
        if(NUM_EVALS>=1) {
            individual = new GAIndividual(Search.NUM_ACTIONS_INDIVIDUAL, playerID);
            individual.randomize(m_rnd, ActionMap.ActionMap.length );
            individual.evaluate(gameState, opponent);
            this.numEvals++;
        } else {
            throw new RuntimeException("The total evaluation number " + NUM_EVALS + " is less or equal to 1.");
        }

        //Resetting the random paths found and best fitness.
        m_bestRandomPath = new int[1];
        m_currentRandomPath = new int[1];
        m_bestFitnessFound = -1;
        m_numGenerations = 0;
    }



    /**
     * Run the engine for one cycle.
     * @param a_gameState Game state where the macro-action to be decided must be executed from.
     * @return  the action decided to be executed.
     */
    @Override
    public int run(SimpleBattle a_gameState)
    {
        m_currentGameState = a_gameState;

        long start_time = System.nanoTime(); 
        boolean stop = false;
        while(!stop) {
            GAIndividual[] nextPop = new GAIndividual[1];
        individual = new GAIndividual(1,playerID);
        
        GAIndividual opponent = opponentGen.getOpponent(1);

        nextPop[i] = breed(); // m_individuals[i-ELITISM].copy();


        mut.mutate(nextPop[i]);

        //System.out.print("c-m: " + nextPop[i].toString());
        nextPop[i].evaluate(a_gameState, opponent);
        //System.out.println(", " + nextPop[i].m_fitness);
        }

        m_individuals = nextPop;
        sortPopulationByFitness(m_individuals);

        /*for(i = 0; i < m_individuals.length; ++i)
            System.out.format("individual i: " + i + ", fitness: %.3f, actions: %s \n", m_individuals[i].m_fitness, m_individuals[i].toString());     */


        m_numGenerations++;
        }

        return individual.m_genome[0];
,   }


    private GAIndividual breed()
    {
        GAIndividual gai1 = sel.getParent(m_individuals, null);        //First parent.
        GAIndividual gai2 = sel.getParent(m_individuals, gai1);        //Second parent.
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



}
