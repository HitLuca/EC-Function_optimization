package src.genetics.components.mutation;

import src.genetics.Individual;

import java.util.ArrayList;
import java.util.Random;

public abstract class AMutation {
    protected int min = -5;
    protected int max = 5;
    protected Random rng;
    protected double mutationProbability;

    public AMutation(Random rng, double mutationProbability) {
        this.rng = rng;
        this.mutationProbability = mutationProbability;
    }

    public abstract void mutate(ArrayList<Individual> individuals);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    protected double[] clipToLimits(double[] genome) {
        for (int i = 0; i < genome.length; i++) {
            if (genome[i] < min) {
                genome[i] = min;
            } else if (genome[i] > max) {
                genome[i] = max;
            }
        }
        return genome;
    }
}
