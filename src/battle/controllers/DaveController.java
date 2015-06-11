package battle.controllers;

import asteroids.Action;
import asteroids.GameObject;
import asteroids.GameState;
import battle.BattleController;
import battle.NeuroShip;
import battle.SimpleBattle;
import math.Vector2d;

/**
 * Created by davidgundry on 30/05/15.
 */
public class DaveController implements BattleController {

    Action action;

    public DaveController()
    {
        action = new Action();
    }

    @Override
    public Action getAction(SimpleBattle s, int playerId)
    {
        for (int i=0;i<s.getObjects().size();i++)
        {
            GameObject o = s.getObjects().get(i);
            Vector2d predictedPosition = o.s.add(o.v);

        }
        return new Action(0, 1, true);
    }
}
