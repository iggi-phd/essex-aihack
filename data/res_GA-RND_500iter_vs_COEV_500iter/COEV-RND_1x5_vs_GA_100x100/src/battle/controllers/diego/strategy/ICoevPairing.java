package battle.controllers.diego.strategy;

import battle.SimpleBattle;
import battle.controllers.diego.search.GAIndividual;

/**
 * Created by dperez on 08/07/15.
 */
public interface ICoevPairing
{
    double evaluate(SimpleBattle game, GAIndividual individual, GAIndividual[] otherPop);
}
