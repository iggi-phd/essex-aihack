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

    double minimumDistance;
    double minThrust = 0.2;

    public DaveController()
    {
        t = 0;
        lastShot = -100000;
        offset = (int) Math.round(Math.random()*90);
        minimumDistance = 30;
    }

    @Override
    public Action getAction(SimpleBattle s, int playerId) {
        NeuroShip enemy = getEnemy(s, playerId);
        Vector2d enemyPos = new Vector2d(enemy.s);
        Vector2d enemyVel = new Vector2d(enemy.v);
        Vector2d enemyDir = new Vector2d(enemy.d);

        NeuroShip self = getSelf(s, playerId);
        Vector2d selfPos = new Vector2d(self.s);
        Vector2d selfVel = new Vector2d(self.v);
        Vector2d selfDir = new Vector2d(self.d);

        Vector2d ePredictedPos = enemyPos.add(enemyVel);
        Vector2d relativePos = ePredictedPos.subtract(selfPos);


        Action action;
        if (relativePos.mag() < minimumDistance)
            action = escape();
        else
            action = chaseAndAssault(maxShootingDistance(selfVel,selfDir),relativePos,selfDir,selfVel,enemyVel,enemyDir);

        t++;
        if (action.shoot)
            lastShot = t;
        return action;
    }

    private Action escape()
    {
        double randomness = Math.random()*Math.sin(t + offset);

        double thrust = 1;
        boolean shoot = false;
        double angle = randomness;
        return new Action(thrust,angle,shoot);
    }

    private Action chaseAndAssault(double maxShootingDistance, Vector2d relativePos, Vector2d selfDir, Vector2d selfVel, Vector2d enemyVel, Vector2d enemyDir)
    {
        boolean shoot = false;
        Vector2d shootingDirection = selfDir.mul(0.6).add(selfVel.mul(0.4));//.add(selfDir.mul(relativePos.mag()*missileMinVelocity*2));
        Vector2d weightedEnemyDir = enemyDir.mul(0.7).add(enemyVel.mul(0.3));

        double den = shootingDirection.mag() * relativePos.mag();
        if (den==0)
            den=0.0001;
        double angle = Math.acos(relativePos.scalarProduct(shootingDirection) / den);

        den = selfDir.mag() * weightedEnemyDir.mag();
        if (den==0)
            den=0.0001;
        double alignment = Math.acos(weightedEnemyDir.scalarProduct(selfDir) / den);
        if (alignment == 0)
            alignment = 0.0001;
        alignment = 1/alignment;

        if (angle > Math.PI/2)
            angle = -(angle-Math.PI/2);

        System.out.println(angle);

        //if (!(angle > 0) && !(angle < 0))
        //   angle = 0;
        //if (!(alignment > 0) && !(alignment < 0))
        //    alignment = 0;

        if ((Math.abs(angle) < 1) && (t - lastShot > 10)) {
            if (relativePos.mag() < maxShootingDistance)
                shoot = true;
        }

        //angle = Math.min(angle, 1);
        //angle = Math.max(-1, angle);

        double randomness = Math.random()*Math.sin(t + offset);
        double thrust = 0;
        // Only take alignment with enemy into account if they're moving
        if (enemyVel.mag()>5) {
            thrust = alignment * enemyVel.mag() + randomness;
            angle -= alignment / 3;
        }
        else
            thrust = enemyVel.mag()+randomness;

        if (Math.abs(thrust)<minThrust)
            thrust = 0;

        return new Action(thrust,angle, shoot);
    }

    private double maxShootingDistance(Vector2d selfVel, Vector2d selfDir)
    {
        return missileTTL*2*Math.max(selfVel.mag(), 1);
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
