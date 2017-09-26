package src;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class Population {
    private ArrayList<Individual> population;
    private int size;



    private double meanFitness;
    private double worstFitness;
    private double bestFitness;
    private double variance;

    public Population(int size, ContestEvaluation evaluation) {
        this.size = size;
        initialize(evaluation);
    }

    public Population(ArrayList<Individual> population) {
        this.size = population.size();
        this.population = population;
    }

    protected void initialize(ContestEvaluation evaluation)
    {
        population = new ArrayList<>();
        for(int i=0; i<size; i++) {
            population.add( new Individual(evaluation));
        }
    }

    public void survive(ASurvival survival)
    {
        population = survival.survival(population);
    }

    public ArrayList<Individual> select(ASelection selection)
    {
        return selection.select(population, size);
    }

    public void reproduce(ArrayList<Individual> parents, ACrossover crossover, AMutation mutation, ContestEvaluation evaluation)
    {

        Collections.shuffle(parents);
        Iterator<Individual> i = parents.iterator();
        Individual parent1 = i.next();
        Individual parent2;

        for (; i.hasNext();)
        {
            parent2 = i.next();
            population.add(crossover.crossover(parent1, parent2, mutation, evaluation));
        }

    }

    public void updateStatistics()
    {
        meanFitness = 0;
        bestFitness = Double.MIN_VALUE;
        worstFitness = Double.MAX_VALUE;

        for (Individual individual: population) {
            double fitness = individual.getFitness();
            meanFitness += fitness;

            if(fitness>bestFitness)
            {
                bestFitness = fitness;
            }

            if(fitness<worstFitness)
            {
                worstFitness = fitness;
            }
        }

        System.out.println();
    }


}
