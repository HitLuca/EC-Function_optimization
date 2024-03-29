package src.genetics.GA.mutation;

import src.genetics.GA.other.Individual;

import java.util.ArrayList;
import java.util.Random;

import static src.genetics.GA.other.Population.GENOME_SIZE;

public class MutationGaussian extends AMutation {

    private static double sigma;

    public MutationGaussian(Random rng, double sigma, double mutationProbability) {
        super(rng, mutationProbability);
        this.sigma = sigma;
    }

    public static void increaseMutation() {
        if (sigma < 0.1) {
            sigma += sigma * 0.01;
        }
    }

    public static void decreaseMutation() {
        if (sigma > 0.001) {
            sigma -= sigma * 0.01;
        }
    }

    public static void resetMutation() {
        sigma = 0.05;
    }

    public void mutate(ArrayList<Individual> individuals) {
        for (Individual individual : individuals) {
            if (rng.nextDouble() < mutationProbability) {
                double[] genome = individual.getGenome();
                for (int i = 0; i < GENOME_SIZE; i++) {
                    genome[i] += rng.nextGaussian() * sigma;
                }
                genome = clipToLimits(genome);
                individual.setGenome(genome);
            }
        }
    }

    public double getSigma() {
        return sigma;
    }
}
