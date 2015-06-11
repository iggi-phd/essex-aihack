package battle;

import asteroids.Action;
import battle.controllers.MCTSTreeNode;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 * Created by Nick on 11/06/2015.
 */
public class MCTSRunnable implements Runnable {

    Action currentBestAction = null;
    SimpleBattle rootState = null;
    int actingPlayerId = 0;
    MCTSTreeNode rootNode = null;
    boolean complete = false;
    int iterBudget = 1000;

    public void Init(SimpleBattle rootState, int playerId)
    {
        this.rootState = rootState;
        actingPlayerId = playerId;
    }

    @Override
    public void run()
    {
        //reset the tree - need to stop the process probably
        rootState = rootState.clone();
        rootNode = new MCTSTreeNode(rootState, actingPlayerId);

        for(int i = 0; i < iterBudget; i++)
        {
            rootNode.selectAction();

            currentBestAction = rootNode.GetBestAction();
        }

        complete = true;
    }

    public boolean IsComplete()
    {
        return complete;
    }

    public Action GetBestAction()
    {
        return currentBestAction;
    }
}
