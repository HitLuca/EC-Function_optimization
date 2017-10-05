package src.genetics.components.mutation;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public abstract class AMutation {
    protected int min = -5;
    protected int max = 5;
    protected Random rng;

    protected double sigma;
    protected double mutationProbability;

    public AMutation(Random rng, double sigma, double mutationProbability) {
        this.rng = rng;
        this.sigma = sigma;
        this.mutationProbability = mutationProbability;
    }

    public void mutate(ArrayList<Individual> individuals) {
        double[] genome;

        switch (Population.FULL_GENOME_SIZE - Population.BASE_GENOME_SIZE) {
            case 1: {
                for (Individual individual : individuals) {
                    if (rng.nextDouble() < mutationProbability) {
                        genome = individual.getGenome();
                        for (int i = 0; i < Population.BASE_GENOME_SIZE; i++) {
                            genome[i] += specificMutation(genome[Population.BASE_GENOME_SIZE]);
                        }
                        genome[Population.BASE_GENOME_SIZE] += rng.nextDouble()*sigma/5 - sigma/10;;
                        genome = clipToLimits(genome);
                        individual.setGenome(genome);
                    }
                }
                break;
            }
            case Population.BASE_GENOME_SIZE: {
                for (Individual individual : individuals) {
                    if (rng.nextDouble() < mutationProbability) {
                        genome = individual.getGenome();
                        for (int i = 0; i < Population.BASE_GENOME_SIZE; i++) {
                            genome[i] += specificMutation(genome[Population.BASE_GENOME_SIZE + i]);
                        }
                        for (int i = Population.BASE_GENOME_SIZE; i < Population.FULL_GENOME_SIZE; i++) {
                            genome[i] += rng.nextDouble()*sigma/5 - sigma/10;;
                        }
                        genome = clipToLimits(genome);
                        individual.setGenome(genome);
                    }
                }
                break;
            }
            default: {
                for (Individual individual : individuals) {
                    if (rng.nextDouble() < mutationProbability) {
                        genome = individual.getGenome();
                        for (int i = 0; i < Population.BASE_GENOME_SIZE; i++) {
                            genome[i] += specificMutation(sigma);
                        }
                        for (int i = Population.BASE_GENOME_SIZE; i < Population.FULL_GENOME_SIZE; i++) {
                            genome[i] += rng.nextDouble()*sigma/5 - sigma/10;
                        }
                        genome = clipToLimits(genome);
                        individual.setGenome(genome);
                    }
                }
                break;
            }
        }
    }

    protected abstract double specificMutation(double sigma);

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
