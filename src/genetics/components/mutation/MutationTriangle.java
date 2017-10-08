package src.genetics.components.mutation;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public class MutationTriangle extends AMutation {

    double mutationProbability;
    private double max;

    public MutationTriangle(Random rng, double max, double mutationProbability) {
        super(rng, mutationProbability);
        this.max = max;
    }

    public void mutate(ArrayList<Individual> individuals) {
        for (Individual individual : individuals) {
            if (rng.nextDouble() < mutationProbability) {
                double[] genome = individual.getGenome();
                for (int i = 0; i < Population.BASE_GENOME_SIZE; i++) {
                    genome[i] += ((rng.nextDouble() + rng.nextDouble()) * max) - max;
                }
                genome = clipToLimits(genome);
                individual.setGenome(genome);
            }
        }
    }
}