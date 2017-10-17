package src.genetics.GA.other;

import java.util.ArrayList;

import static src.genetics.GA.other.Population.GENOME_SIZE;

public class FitnessSharing {
    private static double sigma = 0.1;

    public static double calculateSharedFitness(Individual individual, ArrayList<Individual> population) {
        double fitness = individual.getFitness();
        double[] individualGenome = individual.getGenome();

        double denominator = 0;
        for (Individual ind : population) {
            denominator += sh(calculateEuclideanDistance(individualGenome, ind.getGenome()));
        }
        return fitness / denominator;
    }

    private static double calculateEuclideanDistance(double[] array1, double[] array2) {
        double distance = 0;
        for (int i = 0; i < GENOME_SIZE; i++) {
            distance += Math.pow(array1[i] - array2[i], 2);
        }
        distance = Math.sqrt(distance);
        return distance;
    }

    private static double sh(double distance) {
        if (distance <= sigma) {
            return 1.0 - distance / sigma;
        }
        return 0;
    }
}
