package battle.controllers.diego.strategy;

import battle.controllers.diego.search.GAIndividual;
import battle.controllers.diego.search.Search;

/**
 * Created by dperez on 19/01/16.
 */
public class NullOpponentGenerator implements OpponentGenerator {

    GAIndividual nullOpponent;

    public NullOpponentGenerator(int numActions)
    {
        nullOpponent = new GAIndividual(numActions, -1, null);
    }


    @Override
    public GAIndividual getOpponent(int numActions) {
        return nullOpponent;
    }
}
