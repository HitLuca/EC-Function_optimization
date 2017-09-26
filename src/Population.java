package src;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;

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
        return selection.select(population);
    }

    public void reproduce(ArrayList<Individual> parents, int parentsNumber, ACrossover crossover, AMutation mutation, ContestEvaluation evaluation)
    {
        ArrayList<Individual> children = crossover.crossover(parentsNumber, parents, mutation, evaluation);
        population.addAll(children);

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
        meanFitness /= population.size();

        System.out.println("Mean Fitness: " + meanFitness + " Best Fitness: " + bestFitness + " Worst Fitness: " + worstFitness + " Individuals: " + Individual.object_count);
    }

    public static void printPopulation(ArrayList<Individual> population)
    {
        for(Individual i: population)
        {
            System.out.println(i);
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getSize() {
        return size;
    }

    public int getCurrentSize() {
        return population.size();
    }
}
