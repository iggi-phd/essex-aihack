package battle.controllers.diego.strategy;

import battle.controllers.diego.search.GAIndividual;

/**
 * Created by dperez on 19/01/16.
 */
public interface OpponentGenerator
{
    public GAIndividual getOpponent(int numActions);

}
