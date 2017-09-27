package src.components;

import src.AMutation;
import src.Individual;

import java.util.ArrayList;
import java.util.Random;

public class MutationUniform extends AMutation {

    private double max;
    double mutationProbability;

    public MutationUniform(double max, double mutationProbability) {
        this.max = max;
        this.mutationProbability = mutationProbability;
    }

    public void mutate(ArrayList<Individual> individuals)
    {
        Random rng = new Random();

        for(Individual individual:individuals) {
            if (rng.nextDouble() < mutationProbability) {
                double[] genome = individual.getGenome();
                for (int i = 0; i < genome.length; i++) {
                    genome[i] += (rng.nextDouble() * max * 2) - max;
                }
                individual.setGenome(genome);
            }
        }
    }
}