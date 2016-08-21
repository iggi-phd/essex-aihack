package battle.controllers.diego.strategy;

import battle.SimpleBattle;
import battle.controllers.diego.search.GAIndividual;

/**
 * Created by dperez on 08/07/15.
 */
public abstract class ICoevPairing
{
    public int groupSize;

    public abstract double evaluate(SimpleBattle game, GAIndividual individual, GAIndividual[] otherPop);
    public int getGroupSize() {
        return groupSize;
    }
}
