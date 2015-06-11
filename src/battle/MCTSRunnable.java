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
    MCTSTreeNode rootNode = null;

    @Override
    public void run()
    {

    }

    public void restart(SimpleBattle state)
    {
        //reset the tree - need to stop the process probably
        rootState = state.clone();
        rootNode = null;
    }

    public Action GetBestAction()
    {
        return currentBestAction;
    }
}
