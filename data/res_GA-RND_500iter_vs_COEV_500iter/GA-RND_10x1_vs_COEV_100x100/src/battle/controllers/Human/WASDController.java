package battle.controllers.Human;

import asteroids.Action;
import battle.BattleController;
import battle.SimpleBattle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by jwalto on 12/06/2015.
 */
public class WASDController implements BattleController, KeyListener {

    public static Action[] ActionMap = new Action[]{
            new Action(0.0,0.0,false),
            new Action(0.0,-1.0,false),
            new Action(0.0,1.0,false),
            new Action(1.0,0.0,false),
            new Action(1.0,-1.0,false),
            new Action(1.0,1.0,false)
    };

    Action curAction = ActionMap[0];

    /**
     * Indicates if the thrust is pressed.
     */
    private boolean m_thrust;

    /**
     * Indicates if the turn must be applied.
     */
    private int m_turn;


    public WASDController()
    {
        m_turn = 0;
        m_thrust = false;
    }

    @Override
    public Action getAction(SimpleBattle gameStateCopy, int playerId) {

        if (curAction == null)
            curAction = ActionMap[0];
        else
            curAction = getCurrentAction();

        return curAction;
    }

    private Action getCurrentAction()
    {
        //Thrust actions.
        if(m_thrust)
        {
            if(m_turn == -1) return ActionMap[4];
            if(m_turn == 1) return ActionMap[5];
            return ActionMap[3];
        }

        //No thrust actions.
        if(m_turn == -1) return ActionMap[1];
        if(m_turn == 1) return ActionMap[2];
        return ActionMap[0];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                m_thrust = true;
                break;

            case KeyEvent.VK_A:
                m_turn = -1;
                break;

            case KeyEvent.VK_D:
                m_turn = 1;
                break;

//            case KeyEvent.VK_SPACE:
//                currentAction = FIRE;
//                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            m_thrust = false;
        }
        if (key == KeyEvent.VK_A) {
            m_turn = 0;
        }
        if (key == KeyEvent.VK_D) {
            m_turn = 0;
        }

    }
}
