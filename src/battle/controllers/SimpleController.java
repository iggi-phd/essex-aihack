package battle.controllers;

import asteroids.Action;
import battle.BattleController;
import battle.NeuroShip;
import battle.SimpleBattle;

import math.Vector2d;

/**
 * Created by davidgundry on 11/06/15.
 */
public class SimpleController implements BattleController {
    @Override
    public Action getAction(SimpleBattle gameStateCopy, int playerId) {
        NeuroShip me;
        NeuroShip other;
        double turnDirection;
        boolean doFire;

        if (playerId==0) {
            me = gameStateCopy.s1;
           other = gameStateCopy.s2;
        } else {
           me = gameStateCopy.s2;
            other = gameStateCopy.s1;
        }
        Vector2d dV = other.s.copy();
        dV.subtract(me.s);
        double angleBetweenShips =  Math.atan2(me.d.x * dV.y - me.d.y *dV.x, me.d.x * dV.x + me.d.y * dV.y);
        if (angleBetweenShips<0) {
            turnDirection = -1;
        } else {
            turnDirection=1;
        }
        if ((Math.abs(angleBetweenShips)<0.1) && (other.s.dist(me.s)<100)) {
            doFire=true;
        } else {
            doFire=false;
        }

        return new Action(0,turnDirection,doFire);
        //return new Action(0,0,false);
    }
}
