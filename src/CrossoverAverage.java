package src;

import org.vu.contest.ContestEvaluation;

import java.util.Random;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class CrossoverAverage implements ICrossover{

    @Override
    public Individual crossover(Individual parent1, Individual parent2, Mutation mutation, ContestEvaluation evaluation) {
        double[] child_genome = new double[Individual.GENOME_SIZE];
        for(int i=0; i<Individual.GENOME_SIZE; i++) {
            child_genome[i] = parent1.getGenome()[i] + parent2.getGenome()[i];
        }
        child_genome = mutation.mutate(child_genome);

        return new Individual(child_genome, evaluation);
    }
}
