package battle.controllers;

import asteroids.Action;
import battle.BattleController;
import battle.MCTSRunnable;
import battle.SimpleBattle;

/**
 * Created by Nick on 11/06/2015.
 */
public class MCTSController implements BattleController
{
    //Thrust/turn/shoot
    Action currentMacroAction = new Action(0, 0, false);
    MCTSRunnable mctsSearch = null;
    Thread t = null;

    public MCTSController()
    {
    }

    private void StartMCTS(SimpleBattle gameState, int playerId)
    {
        mctsSearch = new MCTSRunnable();
        mctsSearch.Init(gameState, playerId);
        t = new Thread(mctsSearch);
        t.start();
    }

    @Override
    public Action getAction(SimpleBattle gameStateCopy, int playerId)
    {
        if(mctsSearch == null)
        {
            //first run
            StartMCTS(gameStateCopy, playerId);
        }
        else if(mctsSearch.IsComplete())
        {
            currentMacroAction = mctsSearch.GetBestAction();
            StartMCTS(gameStateCopy, playerId);
        }
        else
        {
            currentMacroAction = mctsSearch.GetBestAction();
        }

        return currentMacroAction;
    }
}
