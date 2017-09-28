package src;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;

public class Population {
    private ArrayList<Individual> population;
    private int maxSize;

    private double meanFitness = -1;
    private double worstFitness = -1;
    private double bestFitness = -1;
    private double variance = -1;

    private int stagnancyLevel = 0;
    private int stagnancyThreshold = 0;
    private double epurationDegree = 0;

    public Population(int maxSize) {
        this.maxSize = maxSize;
        population = new ArrayList<>();
    }

    public Population(int maxSize, ContestEvaluation evaluation) {
        this.maxSize = maxSize;
        population = new ArrayList<>();
        initialize(evaluation);
    }

    public Population(int maxSize, ContestEvaluation evaluation, int stagnancyThreshold, double epurationDegree) {
        this.maxSize = maxSize;
        population = new ArrayList<Individual>();
        initialize(evaluation);
        this.stagnancyThreshold = stagnancyThreshold;
        this.epurationDegree = epurationDegree;
    }

    public Population(ArrayList<Individual> population) {
        this.maxSize = population.size();
        this.population = population;
    }

    public void renewPopulation(ArrayList<Individual> newPopulation){
        population = newPopulation;
    }

    protected void initialize(ContestEvaluation evaluation) {
        for(int i=0; i<maxSize; i++) {
            population.add(new Individual(evaluation));
        }
    }

    public void updateStatistics() {
        meanFitness = 0;
        bestFitness = Double.MIN_VALUE;
        worstFitness = Double.MAX_VALUE;

        for (Individual individual: population) {
            double fitness = individual.getFitness();
            meanFitness += fitness;

            if(fitness>bestFitness) {
                bestFitness = fitness;
            }

            if(fitness<worstFitness) {
                worstFitness = fitness;
            }
        }
        meanFitness /= population.size();
    }

    public String getStatistics() {
        return "Mean Fitness: " + meanFitness + " Best Fitness: " + bestFitness + " Worst Fitness: " + worstFitness;
    }

    public static void printPopulation(ArrayList<Individual> population) {
        for(Individual i: population) {
            System.out.println(i);
        }
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getCurrentSize() {
        return population.size();
    }

    public ArrayList<Individual> getIndividuals() {
        return population;
    }

    public void addIndividuals(ArrayList<Individual> individuals) {
        population.addAll(individuals);
    }

    public void evaluateFitness(ContestEvaluation evaluation) {
        for(Individual i:population) {
            i.evaluate(evaluation);
        }

        double old_best = bestFitness;
        updateStatistics();
        checkStagnancy(old_best, bestFitness, evaluation);
    }

    public void sortIndividuals() {
        population.sort(new Individual.FitnessComparator().reversed());
    }

    public ArrayList<Individual> getElites(int elitism) {
        return new ArrayList<>(population.subList(0, elitism));
    }

    public void epuration(double epurationDegree){
        sortIndividuals();
        population = new ArrayList<>(population.subList(0, maxSize - (int)(maxSize * epurationDegree)));
        System.out.println("Epuration enacted, purged individuals: " + (int)(maxSize * epurationDegree));
    }

    public void checkStagnancy(double oldBest, double newBest, ContestEvaluation evaluation){

        if (stagnancyThreshold == 0) return;

        if (newBest > oldBest) stagnancyLevel = 0;
        else stagnancyLevel++;

        if (stagnancyLevel > stagnancyThreshold){
            epuration(epurationDegree);
            stagnancyLevel = 0;
            while(population.size()<maxSize){
                population.add(new Individual(evaluation));
            }
        }
    }
}
