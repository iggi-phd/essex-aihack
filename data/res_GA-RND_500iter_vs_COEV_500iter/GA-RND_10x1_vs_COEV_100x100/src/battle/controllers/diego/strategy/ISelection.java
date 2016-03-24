package battle.controllers.diego.strategy;

import battle.controllers.diego.search.GAIndividual;

/**
 * Created by dperez on 08/07/15.
 */
public interface ISelection
{
    GAIndividual getParent(GAIndividual[] pop, GAIndividual first);
}
