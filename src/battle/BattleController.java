package battle;

import asteroids.Action;
import utilities.ElapsedCpuTimer;

/**
 * Created by simon lucas on 10/06/15.
 */

public interface BattleController {

    Action getAction(SimpleBattle gameStateCopy, int playerId, ElapsedCpuTimer elapsedTimer);

}
