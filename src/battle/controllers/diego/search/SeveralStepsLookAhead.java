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

    public int[][] allPath = new int[java.lang.Math.pow(ActionMap.length,NUM_ACTIONS_INDIVIDUAL)][NUM_ACTIONS_INDIVIDUAL];
    /**
     * Constructor of the engine.
     */
    public OneStepLookAhead(OpponentGenerator opponentGen, Random rnd)
    {
        super(rnd);
    }

    /**
     * Initializes the engine.
     * This function is also called to reset it.
     */
    @Override
    public void init(SimpleBattle gameState, int playerId)
    {
        this.playerID = playerId;
        //Resetting the random paths found and best fitness.
        m_bestPath = new int[NUM_ACTIONS_INDIVIDUAL];
        m_currentPath = new int[NUM_ACTIONS_INDIVIDUAL];

        m_bestFitnessFound = -1;

        for(int i=0; i<allPath.length; ++i)
        {
            allPath[i] = createPath(i);
    }



    /**
     * Run the engine for one cycle.
     * @param a_gameState Game state where the macro-action to be decided must be executed from.
     * @return  the action decided to be executed.
     */
    @Override
    public int run(SimpleBattle gameState)
    {
        int best_action = -1;
        currentGameState = gameState;
        int num_actions = java.lang.Math.pow(ActionMap.length,NUM_ACTIONS_INDIVIDUAL);
        double[][] all_fitness = new double[num_actions][num_actions];
        for(int i=0; i<num_actions; ++i)
        {
            for(int j=0; j<num_actions; ++j)
            {
                all_fitness[i][j] = evaluate(gameState,i,j);
            }
        }

        return best_action;
   }

   public double evaluate(SimpleBattle gameState,i,j)
   {


   }
   private int[] createPath(int ind) {
        int[] newPath = new int[NUM_ACTIONS_INDIVIDUAL];
        for(int i=NUM_ACTIONS_INDIVIDUAL; i>1; i--) 
        {
            int base = java.lang.Math.pow(ActionMap.length,i-1);
            newPath[NUM_ACTIONS_INDIVIDUAL-i] = java.lang.Math.floor(ind/base);
            ind -= base*newPath[NUM_ACTIONS_INDIVIDUAL-i]; 
        }
        return newPath;
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
