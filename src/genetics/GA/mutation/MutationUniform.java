package src.genetics.GA.mutation;

import src.genetics.GA.other.Individual;

import java.util.ArrayList;
import java.util.Random;

import static src.genetics.GA.other.Population.GENOME_SIZE;

public class MutationUniform extends AMutation {
    private double max;

    public MutationUniform(Random rng, double max, double mutationProbability) {
        super(rng, mutationProbability);
        this.max = max;
    }

    public void mutate(ArrayList<Individual> individuals) {
        for (Individual individual : individuals) {
            if (rng.nextDouble() < mutationProbability) {
                double[] genome = individual.getGenome();
                for (int i = 0; i < GENOME_SIZE; i++) {
                    genome[i] += (rng.nextDouble() * max * 2) - max;
                }
                genome = clipToLimits(genome);
                individual.setGenome(genome);
            }
        }
    }
}