package battle.controllers;

import asteroids.Action;
import asteroids.GameObject;
import asteroids.GameState;
import asteroids.Missile;
import battle.*;
import math.Vector2d;

import java.awt.*;

import static asteroids.Constants.*;

/**
 * Created by davidgundry on 30/05/15.
 */
public class DaveController extends DebugController {

    Action action;
    int t;
    int lastShot;
    int offset;

    double lastAngle;
    double lastAlignment;
    double lastThrust = 0;
    Vector2d lastRelativePos = new Vector2d(0,0);

    double minimumDistance = 40;
    double minThrust = 0.2;
    int shootingTime = 10;


    public DaveController()
    {
        t = 0;
        lastShot = -100000;
        offset = (int) Math.round(Math.random()*90);
        lastAngle = 0;
        lastAlignment = 0;
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
        double scale = relativePos.mag()/makeNotZero(enemyVel.mag());
        ePredictedPos = Vector2d.add(enemyPos, Vector2d.multiply(enemyVel,scale));
        //ePredictedPos = Vector2d.add(enemyPos, Vector2d.multiply(enemyVel,relativePos.mag()));
        relativePos = Vector2d.subtract(ePredictedPos,selfPos);

        lastRelativePos = relativePos;


        Action action;
        if (relativePos.mag() < minimumDistance)
            action = escape();
        else
            action = chaseAndAssault(maxShootingDistance(selfVel,selfDir),relativePos,selfDir,selfVel,enemyVel,enemyDir);

        t++;
        if (action.shoot)
            lastShot = t;

        lastThrust  =action.thrust;

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
        return input;
    }

    private Action chaseAndAssault(double maxShootingDistance, Vector2d relativePos, Vector2d selfDir, Vector2d selfVel, Vector2d enemyVel, Vector2d enemyDir)
    {
        boolean shoot = false;
        Vector2d shootingDirection = selfDir;
        //Vector2d shootingDirection = Vector2d.add(Vector2d.multiply(selfDir, 0.9), Vector2d.multiply(selfVel, 0.1));
        Vector2d weightedEnemyDir = enemyVel;//Vector2d.add(Vector2d.multiply(enemyDir, 0.3), Vector2d.multiply(enemyVel, 0.7));
        
        double angle = relativePos.theta()-shootingDirection.theta();
        double alignment = weightedEnemyDir.theta()-selfDir.theta();

        if (angle > Math.PI)
            angle = -(angle-Math.PI);

        if (alignment > Math.PI)
            alignment = -(alignment-Math.PI);

        lastAngle = angle;
        lastAlignment = alignment;

        if ((Math.abs(angle) < Math.PI/16) && (t - lastShot > shootingTime)) {
            if (relativePos.mag() < maxShootingDistance)
                shoot = true;
        }

        double randomness = Math.random()*Math.sin(t/100 + offset);
        double thrust = 0;
        // Only take alignment with enemy into account if they're moving?
        if ((enemyVel.mag()>=0.2) && (relativePos.mag() < 100) && (relativePos.mag() > 40)) {
            angle = angle*randomness+alignment*(1-randomness);
        }
        thrust = (relativePos.mag()/6)  +  enemyVel.mag()*(1/makeNotZero(-alignment*2))  +  randomness*30;
        thrust /=50;
        thrust = 1;
        if (Math.abs(thrust)<minThrust)
            thrust = 0;

        return new Action(thrust,angle, shoot);
    }

    private double maxShootingDistance(Vector2d selfVel, Vector2d selfDir)
    {
        return (missileTTL*5);
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
    public void render(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawLine(0, 0, (int)( Math.tan(lastAngle)*lastThrust*100), (int) (-lastThrust*100));
        g.setColor(Color.BLUE);
        g.drawLine(0, 0, (int)( Math.tan(lastAlignment)*lastThrust*100), (int) (-lastThrust*100));
        g.setColor(Color.RED);
        g.drawArc((int)lastRelativePos.x,(int)lastRelativePos.y,10,10,0,360);
    }
}
