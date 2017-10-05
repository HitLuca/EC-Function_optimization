package src.genetics.components.mutation;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public class MutationGaussian extends AMutation {

    public MutationGaussian(Random rng, double sigma, double mutationProbability) {
        super(rng, sigma, mutationProbability);
    }

    protected double specificMutation(double sigma){
        return rng.nextGaussian() * sigma;
    }

//    public void mutate(ArrayList<Individual> individuals) {
//        switch (Population.FULL_GENOME_SIZE - Population.BASE_GENOME_SIZE) {
//            case 1: {
//                for (Individual individual : individuals) {
//                    if (rng.nextDouble() < mutationProbability) {
//                        double[] genome = individual.getGenome();
//                        for (int i = 0; i < Population.BASE_GENOME_SIZE; i++) {
//                            genome[i] += rng.nextGaussian() * genome[Population.BASE_GENOME_SIZE];
//                        }
//                        genome = clipToLimits(genome);
//                        individual.setGenome(genome);
//                    }
//                }
//                break;
//            }
//            case Population.BASE_GENOME_SIZE: {
//                for (Individual individual : individuals) {
//                    if (rng.nextDouble() < mutationProbability) {
//                        double[] genome = individual.getGenome();
//                        for (int i = 0; i < Population.BASE_GENOME_SIZE; i++) {
//                            genome[i] += rng.nextGaussian() * genome[Population.BASE_GENOME_SIZE + i];
//                        }
//                        genome = clipToLimits(genome);
//                        individual.setGenome(genome);
//                    }
//                }
//                break;
//            }
//            default: {
//                for (Individual individual : individuals) {
//                    if (rng.nextDouble() < mutationProbability) {
//                        double[] genome = individual.getGenome();
//                        for (int i = 0; i < Population.BASE_GENOME_SIZE; i++) {
//                            genome[i] += rng.nextGaussian() * sigma;
//                        }
//                        genome = clipToLimits(genome);
//                        individual.setGenome(genome);
//                    }
//                }
//                break;
//            }
//        }
//    }
}
