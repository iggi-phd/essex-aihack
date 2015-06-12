package battle.controllers;

import asteroids.Action;
import battle.BattleController;
import battle.JoeCSimpleController;

/**
 * Created by davidgundry on 11/06/15.
 */
public class FireController implements BattleController {
    @Override
    public Action getAction(JoeCSimpleController gameStateCopy, int playerId) {
        return new Action(0,0,true);
    }
}
