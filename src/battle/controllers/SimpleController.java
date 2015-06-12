package battle.controllers;

import asteroids.Action;
import battle.BattleController;
import battle.NeuroShip;
import battle.SimpleBattle;

import math.Vector2d;

import static asteroids.Constants.t;

/**
 * Created by davidgundry on 11/06/15.
 */
public class SimpleController implements BattleController {

    // define how quickly the ship will rotate
    static double steerStep = 10 * Math.PI / 180;

    @Override
    public Action getAction(SimpleBattle gameStateCopy, int playerId) {
        NeuroShip me;
        NeuroShip other;
        double turnDirection=0;
        double returnThrust=0;
        boolean doFire=false;
        double turn;
        double thrust;
        double difference;

        if (playerId==0) {
            me = gameStateCopy.s1;
           other = gameStateCopy.s2;
        } else {
           me = gameStateCopy.s2;
            other = gameStateCopy.s1;
        }
        // Attack play

        double myAimDifference = getAimDifference(me.s,me.d,other.s);
        double otherAimDifference = getAimDifference(other.s,other.d,me.s);

        if (myAimDifference<otherAimDifference) {
            // Attack play
            double minDiff = 1000;
            for (turn = -1.0; turn < 2.0; turn++) {
                for (thrust = 0; thrust < 2; thrust++) {
                    difference = getAimDifference(getNextPos(me.s, me.d, turn, thrust), getNextRotate(me.d, turn).mul(-1), other.s);
                    if (difference < minDiff) {
                        minDiff = difference;
                        turnDirection = turn;
                        returnThrust = thrust;
                    }
                }
            }

            doFire = (Math.abs(myAimDifference) < 0.3) && (other.s.dist(me.s) < 100);
        } else {
            // Avoid play
            turnDirection = -1;
            returnThrust = 0;
            double maxDiff = -100;
            for (turn = -1; turn < 2; turn++) {
                for (thrust = 0; thrust < 2; thrust++) {
                    difference = getAimDifference(other.s, other.d, getNextPos(me.s, me.d, turn, thrust));
                    if (difference > maxDiff) {
                        maxDiff = difference;
                        turnDirection = turn;
                        returnThrust = thrust;
                    }
                }
            }
        }



        return new Action(returnThrust,turnDirection,doFire);

    }
    double getAimDifference(Vector2d firerPos,Vector2d firerDir,Vector2d targetPos) {
        Vector2d dV = targetPos.copy();
        dV.subtract(firerPos);
        return Math.atan2(firerDir.x * dV.y - firerDir.y * dV.x, firerDir.x * dV.x + firerDir.y * dV.y);
    }
    Vector2d getNextPos(Vector2d pos,Vector2d dir, double steer,double thrust) {
        Vector2d d = dir.copy();
        d.rotate(steer * steerStep);
        Vector2d p = pos.copy();
        p.add(d, thrust * t * 0.3 / 2);
        return p;
    }
    Vector2d getNextRotate(Vector2d dir,double steer) {
        Vector2d d = dir.copy();
        d.rotate(steer * steerStep);
        return d;
    }


}
