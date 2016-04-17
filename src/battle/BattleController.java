package battle;

import asteroids.Action;

import java.awt.*;

/**
 * Created by simon lucas on 10/06/15.
 */

public interface BattleController {

    Action getAction(SimpleBattle gameStateCopy, int playerId);
    void draw(Graphics2D g);
}
