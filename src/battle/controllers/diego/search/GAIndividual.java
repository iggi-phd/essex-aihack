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
    // each genome is a legal solution/action
    public int[] m_genome;
    // the fitness value
    private double m_fitness;
    // the probability to make a mutation
    public final double MUTATION_PROB = 0.2; //0.834=5/6   //0.2;
    // id of the player (0 or 1)
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

    /**
     * Randomly generate/reset chromosomes
     */
    public void randomize(Random a_rnd, int a_numActions)
    {
        for(int i = 0; i < m_genome.length; ++i)
        {
            m_genome[i] = a_rnd.nextInt(a_numActions);
        }
    }

    /**
     * Evaluate the game, given game state and the opponent
     */
    public double evaluate(SimpleBattle gameState, GAIndividual opponent)
    {
        SimpleBattle thisGameCopy = gameState.clone();
        boolean end = false;
        // Make m_genome.length moves*Search.MACRO_ACTION_LENGTH, update the score
        for(int i = 0; i < m_genome.length; ++i)
        {
            int thisAction = m_genome[i];
            int otherAction = opponent.m_genome[i];
            for(int j =0; !end && j < Search.MACRO_ACTION_LENGTH; ++j)
            {
                if(playerID == 0)
                    thisGameCopy.update(ActionMap.ActionMap[thisAction], ActionMap.ActionMap[otherAction]);
                else
                    thisGameCopy.update(ActionMap.ActionMap[otherAction], ActionMap.ActionMap[thisAction]);
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

    /**
     * Return the mean fitness value
     */
    public double getFitness()
    {
        if(accumFit.n() == 0)
            return m_fitness;

        return accumFit.mean();
    }

    public void print()
    {
        String genome = this.toString();
        genome += "; n: " + accumFit.n() + ", fitness: " + ((accumFit.n() == 0) ? m_fitness : accumFit.mean());
        System.out.println(genome);
    }

}
