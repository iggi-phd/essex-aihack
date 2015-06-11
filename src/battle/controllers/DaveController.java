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
        NeuroShip enemy = getEnemy(s,playerId);
        Vector2d enemyPos = new Vector2d(enemy.s);
        Vector2d enemyVel = new Vector2d(enemy.v);

        NeuroShip self = getSelf(s,playerId);
        Vector2d selfPos = new Vector2d(self.s);
        Vector2d selfVel = new Vector2d(self.v);
        System.out.println(selfPos.x + "," + selfPos.y);

        Vector2d ePredictedPos = enemyPos.add(enemyVel);

        Vector2d relativePos = ePredictedPos.subtract(selfPos);

        double angle = Math.acos(relativePos.scalarProduct(self.d)/(self.d.mag()*relativePos.mag()));
        if (angle > 0)
            angle = Math.min(angle,1);
        if (angle < 0)
            angle = Math.max(-1,angle);

        boolean shoot = false;
        System.out.println(angle);
        if (Math.abs(angle) < 1)
            shoot = true;

        return new Action(1, angle, shoot);
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
