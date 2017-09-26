package src.components;

import org.vu.contest.ContestEvaluation;
import src.ACrossover;
import src.AMutation;
import src.Individual;

public class CrossoverAverageWeighted extends ACrossover {

    @Override
    public Individual crossoverPair(Individual[] parentsPair, AMutation mutation, ContestEvaluation evaluation) {
        double[] child_genome = new double[Individual.GENOME_SIZE];
        double[] ratio = new double[parentsPair.length];
        double tot = 0.0;

        for (int i = 0; i < parentsPair.length; i++) {
            tot += parentsPair[i].getFitness();
            ratio[i] = tot;
        }

        for (int i = 0; i < Individual.GENOME_SIZE; i++) {
            child_genome[i] = 0.0;
            for (int j = 0; j < parentsPair.length; j++) {
                child_genome[i] += parentsPair[j].getGenome()[i] * (ratio[j] / tot);
            }
        }
        child_genome = mutation.mutate(child_genome);

        return new Individual(child_genome, evaluation);
    }
}