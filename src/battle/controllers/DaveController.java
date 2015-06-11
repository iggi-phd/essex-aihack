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
        GameObject enemy = getEnemy(s,playerId);
        Vector2d ePredictedPos = enemy.s.add(enemy.v);
        GameObject self = getSelf(s,playerId);

        Vector2d distance = self.s.subtract(ePredictedPos);
        Vector2d angle = distance.subtract(self.v);
        angle.normalise();

        return new Action(0, angle.x, true);
    }

    private NeuroShip getSelf(SimpleBattle s, int playerId)
    {
        if (playerId==0)
            return s.getS1();
        else
            return s.getS2();
    }

    private NeuroShip getEnemy(SimpleBattle s, int playerId)
    {
        if (playerId==1)
            return s.getS1();
        else
            return s.getS2();
    }
}
