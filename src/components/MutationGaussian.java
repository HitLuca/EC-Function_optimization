package src.components;

import src.AMutation;
import src.Individual;

import java.util.ArrayList;
import java.util.Random;

public class MutationGaussian extends AMutation {

    private double sigma;
    double mutationProbability;

    public MutationGaussian(double sigma, double mutationProbability) {
        this.sigma = sigma;
        this.mutationProbability = mutationProbability;
    }

    public void mutate(ArrayList<Individual> individuals)
    {
        Random rng = new Random();

        for(Individual individual:individuals) {
            if (rng.nextDouble() < mutationProbability) {
                double[] genome = individual.getGenome();
                for (int i = 0; i < genome.length; i++) {
                    genome[i] += rng.nextGaussian() * sigma;
                }
                individual.setGenome(genome);
            }
        }
    }
}
