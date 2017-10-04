package src.genetics;

import org.vu.contest.ContestEvaluation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static src.genetics.Population.BASE_GENOME_SIZE;
import static src.genetics.Population.FULL_GENOME_SIZE;

public class Individual {
    public static int objectCount = 0;
    private double genome[];
    private double fitness = 0;
    private boolean isEvaluated = false;
    private int id;
    private int max = 5;
    private int min = -5;
    public Individual(Random rng, ContestEvaluation evaluation) {
        objectCount++;
        id = objectCount;

        genome = new double[FULL_GENOME_SIZE];
        for (int i = 0; i < FULL_GENOME_SIZE; i++)
            genome[i] = rng.nextDouble() * (max - min) + min;

        evaluate(evaluation, genome);
    }

    public Individual(double genome[], ContestEvaluation evaluation) {
        objectCount++;
        id = objectCount;
        this.genome = genome;
        evaluate(evaluation, genome);
    }

    public Individual(double genome[]) {
        objectCount++;
        id = objectCount;
        this.genome = genome;
    }

    public double[] getGenome() {
        return genome;
    }

    public void setGenome(double[] genome) {
        this.genome = genome;
    }

    public void evaluate(ContestEvaluation evaluation) {
        evaluate(evaluation, genome);
    }

    public void evaluate(ContestEvaluation evaluation, double[] genome) {
        if (!isEvaluated) {
            this.fitness = (double) evaluation.evaluate(Arrays.copyOfRange(genome, 0, BASE_GENOME_SIZE));
            isEvaluated = true;
        }
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        assert isEvaluated;
        this.fitness = fitness;
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

    static public class FitnessComparator implements Comparator<Individual> {
        // Used for sorting in ascending order
        public int compare(Individual a, Individual b) {
            if (a.getFitness() > b.getFitness())
                return 1;
            else if (a.getFitness() == b.getFitness())
                return 0;
            else
                return -1;
        }
    }
}
