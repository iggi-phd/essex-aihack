package battle.controllers.diego.strategy;

import battle.controllers.diego.search.GAIndividual;

import java.util.Random;

/**
 * Created by dperez on 08/07/15.
 */
public interface IMutation
{
    void mutate(GAIndividual individual);
}
