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
    int counter = 0;
    int maxCounter = 100;

    public MCTSController()
    {
    }

    @Override
    public Action getAction(SimpleBattle gameStateCopy, int playerId)
    {
        if(mctsSearch == null)
        {
            //first run
            mctsSearch = new MCTSRunnable();
            mctsSearch.restart(gameStateCopy);
            t = new Thread(mctsSearch);
            t.start();
        }
        else
        {
            counter++;

            if(counter < maxCounter)
            {
                return currentMacroAction;
            }
            else
            {
                currentMacroAction = mctsSearch.GetBestAction();
                mctsSearch.restart(gameStateCopy);
            }
        }

        return currentMacroAction;
    }
}
