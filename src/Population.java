package src;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class Population {
    private ArrayList<Individual> population;
    private int size;

    ICrossover crossover;
    Mutation mutation;
    ISelection selection;
    ContestEvaluation evaluation;
    ISurvival survival;

    private double meanFitness;
    private double worstFitness;
    private double bestFitness;
    private double variance;

    public Population(int size, ICrossover crossover, Mutation mutation, ISelection selection, ContestEvaluation evaluation, ISurvival survival) {
        this.size = size;
        this.crossover = crossover;
        this.mutation = mutation;
        this.selection = selection;
        this.evaluation = evaluation;
        initialize();
    }

    public Population(ArrayList<Individual> population, int size, ICrossover crossover, Mutation mutation, ISelection selection, ContestEvaluation evaluation) {
        this.size = size;
        this.crossover = crossover;
        this.mutation = mutation;
        this.selection = selection;
        this.evaluation = evaluation;
        this.population = population;
    }

    protected void initialize()
    {
        for(int i=0; i<size; i++) {
            population.add( new Individual(evaluation));
        }
    }

    public void survive()
    {

    }

    public ArrayList<Individual> select()
    {
        return selection.select(population, size);
    }

    public void reproduce(ArrayList<Individual> parents)
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
        System.out.print(population.get(0).getFitness());
    }

}
