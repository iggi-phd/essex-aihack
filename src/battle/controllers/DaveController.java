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
        Vector2d selfDir = new Vector2d(self.d);

        Vector2d ePredictedPos = enemyPos.add(enemyVel);

        Vector2d relativePos = ePredictedPos.subtract(selfPos);

        Vector2d shootingDirection = selfDir;//.add(selfDir.mul(relativePos.mag()*missileMinVelocity*2));

        double angle = Math.acos(relativePos.scalarProduct(shootingDirection) / (shootingDirection.mag() * relativePos.mag()));
        double alignment = -Math.acos(enemyVel.scalarProduct(selfDir) / selfDir.mag() * enemyVel.mag());

        if (!(angle > 0) && !(angle < 0))
            angle = 0;
        if (!(alignment > 0) && !(alignment < 0))
            alignment = 0;


        if ((Math.abs(angle) < 1) && (t - lastShot > 10)) {
            if (relativePos.mag()< missileTTL*10+self.v.mag())
                shoot = true;
            lastShot = t;
        }

        //angle *= 0.05*Math.sin(t+offset);
        angle = Math.min(angle, 1);
        angle = Math.max(-1, angle);

        double randomness = Math.random()*Math.sin(t + offset)/1;

        double thrust = 1/alignment*enemyVel.mag()+randomness;
        if (Math.abs(thrust)<0.2)
            thrust = 0;

        t++;
        return new Action(thrust,angle, shoot);
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
