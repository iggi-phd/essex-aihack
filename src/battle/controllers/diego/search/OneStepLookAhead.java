
package battle.controllers.diego.search;

import battle.SimpleBattle;
import battle.controllers.diego.ActionMap;
import math.Matrix;
import battle.BattleController;
import asteroids.Action;

public class OneStepLookAhead implements BattleController {
    public Matrix my_fitness;
    
    public Matrix opponent_fitness;
    
    public int playerID;

    public static final int RECOMMEND_POLICY = 1;
    /**
     * Constructor of the engine.
     */
    public OneStepLookAhead()
    {
        my_fitness = new Matrix(ActionMap.ActionMap.length);
        opponent_fitness = new Matrix(ActionMap.ActionMap.length);
    }

    /**
     * Initializes the engine.
     * This function is also called to reset it.
     */
    public void init(SimpleBattle gameState, int playerId)
    {
        this.playerID = playerId;
    }
    
    @Override
    public Action getAction(SimpleBattle gameState, int playerId)
    {
        int best_action = -1;
        //SimpleBattle thisGameCopy = gameState.clone();
        for(int i=0; i<ActionMap.ActionMap.length; ++i)
        {
            for(int j=0; j<ActionMap.ActionMap.length; ++j)
            {
                SimpleBattle thisGameCopy = gameState.clone();
                thisGameCopy.update(ActionMap.ActionMap[i], ActionMap.ActionMap[j]);
                my_fitness.fill(i,j,thisGameCopy.score(playerID));
                opponent_fitness.fill(i,j,thisGameCopy.score(1-playerID));
            }
        }
        best_action = recommend();
        //System.out.println("action : " + best_action);
        return ActionMap.ActionMap[best_action];
   }

    public int recommend()
    {
        int best_action = -1;
        switch(RECOMMEND_POLICY) {
            case 0:
                best_action = my_fitness.MaxSum();
                break;
            case 1:
                best_action = opponent_fitness.MinSum();
                break;
            case 10:
                best_action = my_fitness.MaxMin();
                break;
            case 11:
                best_action = opponent_fitness.MinMax();
                break;
            default:
                throw new RuntimeException("Recommendation policy not defined.");
       }
        return best_action;
    }

}
