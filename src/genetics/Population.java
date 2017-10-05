package src.genetics;

import org.vu.contest.ContestEvaluation;
import src.genetics.components.FitnessSharing;
import src.genetics.components.Stagnancy;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

public class Population {
    public static final int BASE_GENOME_SIZE = 10;
    public static int FULL_GENOME_SIZE = BASE_GENOME_SIZE;

    private ArrayList<Individual> population;
    private int maxSize;

    private double meanFitness = -1;
    private double worstFitness = -1;
    private double bestFitness = -1;
    private double variance = -1;

    private int stagnancyLevel = 0;
    private int stagnancyThreshold = 0;
    private double epurationDegree = 0;

    private Random rng;

    private Stagnancy stagnancy;

    //region Constructors
    public Population(Random rng, int maxSize, Stagnancy stagnancy) {
        this.rng = rng;
        this.maxSize = maxSize;
        this.stagnancy = stagnancy;
        population = new ArrayList<>();
    }

    public Population(Random rng, int maxSize, int genomeSize, Stagnancy stagnancy) {
        this(rng, maxSize, stagnancy);

        if (genomeSize > BASE_GENOME_SIZE)
            FULL_GENOME_SIZE = genomeSize;
    }

    public Population(Random rng, int maxSize, ContestEvaluation evaluation, Stagnancy stagnancy) {
        this(rng, maxSize, stagnancy);
        initialize(evaluation);
    }

    public Population(Random rng, int maxSize, ContestEvaluation evaluation, int stagnancyThreshold, double epurationDegree, Stagnancy stagnancy) {
        this(rng, maxSize, stagnancy);
        initialize(evaluation);
        this.stagnancyThreshold = stagnancyThreshold;
        this.epurationDegree = epurationDegree;
    }

    public Population(Random rng, int maxSize, ContestEvaluation evaluation, int genomeSize, Stagnancy stagnancy) {
        this(rng, maxSize, stagnancy);
        if (genomeSize > BASE_GENOME_SIZE)
            FULL_GENOME_SIZE = genomeSize;

        initialize(evaluation);
    }

    public Population(Random rng, ArrayList<Individual> population) {
        this.rng = rng;
        this.maxSize = population.size();
        this.population = population;
    }

    public Population(Random rng, ArrayList<Individual> population, int genomeSize) {
        this.rng = rng;
        if (genomeSize > BASE_GENOME_SIZE)
            FULL_GENOME_SIZE = genomeSize;

        this.maxSize = population.size();
        this.population = population;
    }

    public static void printPopulation(ArrayList<Individual> population) {
        for (Individual i : population) {
            System.out.println(i);
        }
    }

    public void renewPopulation(ArrayList<Individual> newPopulation) {
        population = newPopulation;
    }

    //endregion

    protected void initialize(ContestEvaluation evaluation) {
        for (int i = 0; i < maxSize; i++) {
            population.add(new Individual(rng, evaluation));
        }
    }

    public void updateStatistics() {
        meanFitness = 0;
        bestFitness = Double.MIN_VALUE;
        worstFitness = Double.MAX_VALUE;

        variance = 0;
        for (Individual individual : population) {
            double fitness = individual.getFitness();
            meanFitness += fitness;

            if (fitness > bestFitness) {
                bestFitness = fitness;
            }

            if (fitness < worstFitness) {
                worstFitness = fitness;
            }
        }
        meanFitness /= population.size();

        for (Individual individual : population) {
            variance += Math.pow(individual.getFitness() - meanFitness, 2);
        }
        variance /= (population.size()-1);
    }

    public String getStatistics() {
        return meanFitness
                + ", " + bestFitness
                + ", " + worstFitness
                + ", " + variance;
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
        for (Individual i : population) {
            i.evaluate(evaluation);
        }

        double old_best = bestFitness;
        population = stagnancy.checkStagnancy(old_best, bestFitness, evaluation, population);
    }

    public void sortIndividuals() {
        population.sort(new Individual.FitnessComparator().reversed());
    }

    public ArrayList<Individual> getElites(int elitism) {
        return new ArrayList<>(population.subList(0, elitism));
    }

    public void calculateSharedFitness() {
        for(Individual ind: population) {
            ind.setFitness(FitnessSharing.calculateSharedFitness(ind, population));
        }
        updateStatistics();
    }

    public double getMeanFitness() {
        return meanFitness;
    }

    public double getWorstFitness() {
        return worstFitness;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public double getVariance() {
        return variance;
    }
}
