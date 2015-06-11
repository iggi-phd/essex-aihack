package battle.controllers;

import asteroids.Action;
import asteroids.GameObject;
import asteroids.GameState;
import asteroids.Missile;
import battle.BattleController;
import battle.NeuroShip;
import battle.SimpleBattle;
import math.Vector2d;

import static asteroids.Constants.*;

/**
 * Created by davidgundry on 30/05/15.
 */
public class DaveController implements BattleController {

    Action action;
    int t;
    int lastShot;
    int offset;

    public DaveController()
    {
        t = 0;
        lastShot = 0;
        offset = (int) Math.round(Math.random()*90);
    }

    @Override
    public Action getAction(SimpleBattle s, int playerId) {
        boolean shoot = false;
        NeuroShip enemy = getEnemy(s, playerId);
        Vector2d enemyPos = new Vector2d(enemy.s);
        Vector2d enemyVel = new Vector2d(enemy.v);

        NeuroShip self = getSelf(s, playerId);
        Vector2d selfPos = new Vector2d(self.s);
        Vector2d selfVel = new Vector2d(self.v);

        Vector2d ePredictedPos = enemyPos.add(enemyVel);

        Vector2d relativePos = ePredictedPos.subtract(selfPos);

        double angle = Math.acos(relativePos.scalarProduct(self.d) / (self.d.mag() * relativePos.mag()));

        if ((Math.abs(angle) < 1) && (t - lastShot > 10)) {
            if (relativePos.mag()< missileTTL*self.v.mag())
                shoot = true;
            lastShot = t;
        }

        //angle *= 0.05*Math.sin(t+offset);
        angle = Math.min(angle, 1);
        angle = Math.max(-1, angle);


        t++;
        return new Action(1,Math.random()*Math.sin(t + offset)/10+angle, shoot);
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
