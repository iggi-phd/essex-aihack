package battle;

import asteroids.Action;

/**
 * Created by simon lucas on 10/06/15.
 */

public interface BattleController {

    Action getAction(JoeCSimpleController gameStateCopy, int playerId);

}
