package battle.controllers.nullController;

import asteroids.Action;
import battle.BattleController;
import battle.SimpleBattle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by jwalto on 12/06/2015.
 */
public class NullController implements BattleController {

    @Override
    public Action getAction(SimpleBattle gameStateCopy, int playerId) {
        return new Action(0, 0, false);
    }
}
