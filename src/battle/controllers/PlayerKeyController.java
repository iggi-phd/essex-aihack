package battle.controllers;

import asteroids.Action;
import asteroids.KeyController;
import battle.BattleController;
import battle.NeuroShip;
import battle.JoeCSimpleController;

/**
 * Created by simonlucas on 30/05/15.
 */
public class PlayerKeyController implements BattleController {

    NeuroShip ship;

    Action action;
    KeyController keyHandler;

    public PlayerKeyController() {
        keyHandler = new KeyController();
        action = new Action();
    }
    public KeyController getKeyHandler() {
        return keyHandler;
    }

  /*  public Action action(GameState game) {
        // action.thrust = 2.0;
        action.shoot = true;
        action.turn = 1;

        return action;
    }

    public void setVehicle(NeuroShip ship) {
        // just in case the ship is needed ...
        this.ship = ship;
    }*/

    @Override
    public Action getAction(JoeCSimpleController gameStateCopy, int playerId) {
        return keyHandler.getAction();
        //return new Action(1, 0, true);
    }
}
