package src;

import org.vu.contest.ContestEvaluation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Individual {
    static public class FitnessComparator implements Comparator<Individual> {
        // Used for sorting in ascending order
        public int compare(Individual a, Individual b)
        {
            if(a.getFitness() > b.getFitness())
                return 1;
            else if (a.getFitness() == b.getFitness())
                return 0;
            else
                return -1;
        }
    }

    public static final int GENOME_SIZE = 10;
    public static int objectCount = 0;

    private double genome[];
    private double fitness;
    private boolean isEvaluated = false;
    private int id;

    private int max = 5;
    private int min = -5;

    public Individual(ContestEvaluation evaluation) {
        objectCount++;
        id = objectCount;

        genome = new double[GENOME_SIZE];
        Random rnd = new Random();
        for (int i=0; i<GENOME_SIZE; i++)
            genome[i] = rnd.nextDouble() * (max-min) + min;

        fitness = (double) evaluation.evaluate(genome);
        isEvaluated = true;
    }

    public Individual(double genome[], ContestEvaluation evaluation) {
        objectCount++;
        id = objectCount;
        this.genome = genome;
        fitness = (double) evaluation.evaluate(genome);
        isEvaluated = true;
    }

    public Individual(double genome[]) {
        objectCount++;
        id = objectCount;
        this.genome = genome;
    }

    public double[] getGenome() {
        return genome;
    }

    public void evaluate(ContestEvaluation evaluation) {
        if(!isEvaluated) {
            this.fitness = (double) evaluation.evaluate(genome);
            isEvaluated = true;
        }
    }

    public void setGenome(double[] genome) {this.genome = genome;}

    public double getFitness() {
        return fitness;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "id=" + id +
                ", genome=" + Arrays.toString(genome) +
                ", fitness=" + fitness +
                '}';
    }
}
