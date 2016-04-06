package battle.controllers.diego.strategy;

import battle.controllers.diego.ActionMap;
import battle.controllers.diego.search.GAIndividual;

import java.util.Random;

/**
 * Created by dperez on 08/07/15.
 */
public class PMutation implements IMutation
{
    double mutProb;
    Random m_rnd;

    public PMutation(Random rnd, double mutp)
    {
        mutProb = mutp;
        m_rnd = rnd;
    }

    @Override
    public void mutate(GAIndividual individual) {

        for (int i = 0; i < individual.m_genome.length; i++) {
            if(m_rnd.nextDouble() < mutProb)
            {
                if(m_rnd.nextDouble() < 0.5)  //mutate thrust
                    individual.m_genome[i] = ActionMap.mutateThrust(individual.m_genome[i]);
                else //mutate steering
                    individual.m_genome[i] = ActionMap.mutateSteer(individual.m_genome[i], m_rnd.nextDouble()>0.5);
            }

        }
    }

}
