package src.genetics;

import org.vu.contest.ContestEvaluation;

import java.io.IOException;
import java.util.*;

public class DifferentialEvolution extends AEA {
    private int epochs;
    private int popSize;

    private double f;
    private double cr;
    private char base;
    private int diffN;

    private DiffPopulation population;

    private Random rng;
    private ContestEvaluation evaluation;

    public DifferentialEvolution(int epochs, int popSize, double f, double cr, char base,
                                 int diffN, Random rng, ContestEvaluation evaluation) {
        this.popSize = popSize;
        this.f = f;
        this.cr = cr;
        this.base = base;
        this.diffN = diffN;
        this.rng = rng;
        this.evaluation = evaluation;

        if (epochs == -1) {
            this.epochs = Integer.MAX_VALUE;
        } else {
            this.epochs = epochs;
        }

    }

    @Override
    public void run() {
        population = new DiffPopulation(evaluation, popSize, rng);
        System.out.println("Scores:");
        try {
            System.out.println("HI");
            for (int epoch = 0; epoch < epochs; epoch++) {
                population.update(f, cr, base, diffN);
                System.out.println(epoch + ", " + population.getStatistics());
            }
        } catch (Exception e) {
            System.out.println("EndScores\n");
            throw e;
        }
        System.out.println("EndScores\n");
    }

    @Override
    public void printAlgorithmParameters() {
        System.out.println("Properties:");
        System.out.println("algorithmType = " + this.toString());
        System.out.println("EndProperties\n");
    }
}


class DiffPopulation {
    public static final int GENOME_SIZE = 10;
    private ContestEvaluation evaluation;
    private Random rng;

    private ArrayList<DiffIndividual> population;
    private int popSize;

    private double meanFitness = -1;
    private double worstFitness = -1;
    private double bestFitness = -1;
    private double variance = -1;

    private DiffIndividual bestIndividual;

    public DiffPopulation(ContestEvaluation evaluation, int popSize, Random rng) {
        this.evaluation = evaluation;
        this.popSize = popSize;
        this.rng = rng;

        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            population.add(new DiffIndividual(evaluation, rng));
        }
        updateStatistics();
    }

    public void update(double f, double cr, char base, int diffN) {
        ArrayList<DiffIndividual> newPopulation = new ArrayList<>();
        DiffIndividual x;
        DiffIndividual y;
        for (int i = 0; i < popSize; i++) {
            x = population.get(i);
            y = new DiffIndividual(evaluation, combine(x, select(i, base, diffN), f, cr));
            if (y.getFitness() > x.getFitness()) newPopulation.add(y);
            else newPopulation.add(x);
        }
        population = newPopulation;
        updateStatistics();
    }

    private ArrayList<DiffIndividual> select(int index, char base, int diffN) {
        // we need a set to avoid sampling the same element twice
        Set<DiffIndividual> parents = new HashSet<>();
        int i;

        if (base == 'b') parents.add(bestIndividual);

        while (parents.size() < 1 + diffN*2) {
            i = rng.nextInt(popSize);
            if (i != index) parents.add(population.get(i));
        }
        ArrayList<DiffIndividual> parentsArray = new ArrayList<>();
        parentsArray.addAll(parents);
        return parentsArray;
    }

    private double[] combine(DiffIndividual x, ArrayList<DiffIndividual> parents, double f, double cr){
        double[] y = x.getGenome().clone();

        double[] a = parents.get(0).getGenome();
        double d = 0.0;

        for (int i = 0; i < GENOME_SIZE; i++) {
            if(rng.nextDouble()<cr) {
                for (int j = 1; j < parents.size(); j++) {
                    d += parents.get(j).getGenome()[i] * Math.pow(-1, j);
                }
                y[i] =  a[i] + f*(d);
            }
        }
        return y;
    }

    public void updateStatistics() {
        meanFitness = 0;
        bestFitness = Double.MIN_VALUE;
        worstFitness = Double.MAX_VALUE;

        variance = 0;
        for (DiffIndividual individual : population) {
            double fitness = individual.getFitness();
            meanFitness += fitness;

            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestIndividual = individual;
            }

            if (fitness < worstFitness) {
                worstFitness = fitness;
            }
        }
        meanFitness /= population.size();

        for (DiffIndividual individual : population) {
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

}

class DiffIndividual {

    private double genome[];
    private double fitness = 0;

    private int max = 5;
    private int min = -5;

    public DiffIndividual(ContestEvaluation evaluation, Random rng) {
        genome = new double[DiffPopulation.GENOME_SIZE];
        for (int i = 0; i < DiffPopulation.GENOME_SIZE; i++) {
            genome[i] = rng.nextDouble() * (max - min) + min;
        }
        evaluate(evaluation);
    }

    public DiffIndividual(ContestEvaluation evaluation, double[] genome) {
        this.genome = clip(genome);
        evaluate(evaluation);
    }

    public double[] getGenome(){ return genome;}
    public double getFitness() {
        return fitness;
    }

    private double[] clip(double[] g){
        for (int i = 0; i < g.length; i++) {
            if (g[i] < min) g[i] = min;
            else if (g[i] > max) g[i] = max;
        }
        return g;
    }

    public void evaluate(ContestEvaluation evaluation) {
        this.fitness = (double) evaluation.evaluate(genome);
    }
}
