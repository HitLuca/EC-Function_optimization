package src.genetics.components.mutation;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public class MutationGaussian extends AMutation {

    private double sigma;

    public MutationGaussian(Random rng, double sigma, double mutationProbability) {
        super(rng, mutationProbability);
        this.sigma = sigma;
    }

    public void mutate(ArrayList<Individual> individuals) {
        for (Individual individual : individuals) {
            if (rng.nextDouble() < mutationProbability) {
                double[] genome = individual.getGenome();
                for (int i = 0; i < Population.BASE_GENOME_SIZE; i++) {
                    genome[i] += rng.nextGaussian() * sigma;
                }
                genome = clipToLimits(genome);
                individual.setGenome(genome);
            }
        }
    }

    @Override
    public void increaseMutation() {
        if(sigma < 0.5) {
            sigma += sigma * 0.01;
        }
    }

    @Override
    public void decreaseMutation() {
        if(sigma > 0.005) {
            sigma -= sigma * 0.01;
        }
    }

    public double getSigma() {
        return sigma;
    }
}
