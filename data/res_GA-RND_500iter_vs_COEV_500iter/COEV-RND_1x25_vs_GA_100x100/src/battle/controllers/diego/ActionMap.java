package battle.controllers.diego;

import asteroids.Action;

/**
 * Created by dperez on 07/07/15.
 */
public class ActionMap
{
    public static Action[] ActionMap = new Action[]{
            new Action(0.0,0.0,false),
            new Action(0.0,-1.0,false),
            new Action(0.0,1.0,false),
            new Action(1.0,0.0,false),
            new Action(1.0,-1.0,false),
            new Action(1.0,1.0,false)
    };

    public static int mutateThrust(int action)
    {
        if (action==0) return 3;
        if (action==1) return 4;
        if (action==2) return 5;
        if (action==3) return 0;
        if (action==4) return 1;
        if (action==5) return 2;
        return -1;
    }

    public static int mutateSteer(int action, boolean rightwise)
    {
        //From a side to center.
        if (action==1) return 0;
        if (action==2) return 0;
        if (action==4) return 3;
        if (action==5) return 3;

        if (action==0)
            if (rightwise) return 2;
            else return 1;

        if (action==3)
            if (rightwise) return 5;
            else return 4;

        return -1;
    }

}
