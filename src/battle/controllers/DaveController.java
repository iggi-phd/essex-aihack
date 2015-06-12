package battle.controllers;

import asteroids.Action;
import asteroids.GameObject;
import asteroids.GameState;
import asteroids.Missile;
import battle.BattleController;
import battle.NeuroShip;
import battle.RenderableBattleController;
import battle.SimpleBattle;
import math.Vector2d;

import java.awt.*;

import static asteroids.Constants.*;

/**
 * Created by davidgundry on 30/05/15.
 */
public class DaveController implements RenderableBattleController {

    Action action;
    int t;
    int lastShot;
    int offset;

    Vector2d lastAngle;
    Vector2d lastAlignment;

    double minimumDistance;
    double minThrust = 0.2;

    public DaveController()
    {
        t = 0;
        lastShot = -100000;
        offset = (int) Math.round(Math.random()*90);
        minimumDistance = 30;
        lastAngle = new Vector2d(0,0);
        lastAlignment = new Vector2d(0,0);
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

        Vector2d ePredictedPos = Vector2d.add(enemyPos, enemyVel);
        Vector2d relativePos = Vector2d.subtract(ePredictedPos,selfPos);


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

    private double makeNotZero(double input)
    {
        if (input ==0)
            return 0.000001;
    }

    private Action chaseAndAssault(double maxShootingDistance, Vector2d relativePos, Vector2d selfDir, Vector2d selfVel, Vector2d enemyVel, Vector2d enemyDir)
    {
        boolean shoot = false;
        Vector2d shootingDirection = Vector2d.add(Vector2d.multiply(selfDir, 0.6), Vector2d.multiply(selfVel, 0.4));
        Vector2d weightedEnemyDir = Vector2d.add(Vector2d.multiply(enemyDir, 0.7), Vector2d.multiply(enemyVel, 0.3));

        double angle = Math.acos(relativePos.scalarProduct(shootingDirection) / makeNotZero(shootingDirection.mag() * relativePos.mag()));
        double alignment = 1/makeNotZero(Math.acos(weightedEnemyDir.scalarProduct(selfDir) / makeNotZero(selfDir.mag() * weightedEnemyDir.mag())));

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

    @Override
    public void render(Graphics2D g, NeuroShip s) {
        g.drawLine(0,0,(int)lastAngle.x,(int)lastAngle.y);
        g.drawLine(0,0,(int)lastAlignment.x,(int)lastAlignment.y);
    }
}
