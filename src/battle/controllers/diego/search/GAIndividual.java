package battle.controllers.diego.search;

import battle.SimpleBattle;
import battle.controllers.diego.ActionMap;
import utilities.StatSummary;

import java.util.Random;

/**
 * PTSP-Competition
 * Created by Diego Perez, University of Essex.
 * Date: 17/10/12
 */
public class GAIndividual
{
    public int[] m_genome;
    private double m_fitness;
    public final double MUTATION_PROB = 0.2; //0.834=5/6   //0.2;

    public int playerID;

    private StatSummary accumFit;

    public GAIndividual(int a_genomeLength, int playerID)
    {
        m_genome = new int[a_genomeLength];
        m_fitness = 0;
        accumFit = new StatSummary();
        this.playerID = playerID;
    }


    public GAIndividual(int genome[], int playerID)
    {
        m_genome = genome;
        m_fitness = 0;
        accumFit = new StatSummary();
        this.playerID = playerID;
    }

    public void randomize(Random a_rnd, int a_numActions)
    {
        for(int i = 0; i < m_genome.length; ++i)
        {
            m_genome[i] = a_rnd.nextInt(a_numActions);
        }
    }

    public double evaluate(SimpleBattle gameState, GAIndividual opponent)
    {
        SimpleBattle thisGameCopy = gameState.clone();
        boolean end = false;
        for(int i = 0; i < m_genome.length; ++i)
        {
            int thisAction = m_genome[i];
            int otherAction = opponent.m_genome[i];
            for(int j =0; !end && j < Search.MACRO_ACTION_LENGTH; ++j)
            {
                thisGameCopy.update(ActionMap.ActionMap[thisAction], ActionMap.ActionMap[otherAction]);
                end = thisGameCopy.isGameOver();
            }
        }
        m_fitness = thisGameCopy.score(playerID);
        accumFit = new StatSummary(); //We need to void this.
        return m_fitness;
    }

    public void setFitness(double fit)
    {
        m_fitness = fit;
    }


    public void accumFitness(double fit)
    {
        accumFit.add(fit);
    }


    public GAIndividual copy()
    {
        GAIndividual gai = new GAIndividual(this.m_genome.length, this.playerID);
        for(int i = 0; i < this.m_genome.length; ++i)
        {
            gai.m_genome[i] = this.m_genome[i];
        }
        return gai;
    }

    public String toString()
    {
        String st = new String();
        for(int i = 0; i < m_genome.length; ++i)
            st += m_genome[i];
        return st;
    }

    public double getFitness()
    {
        if(accumFit.n() == 0)
            return m_fitness;

        return accumFit.mean();
    }


}
