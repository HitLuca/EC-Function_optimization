package src.components;

import org.vu.contest.ContestEvaluation;
import src.ACrossover;
import src.AMutation;
import src.Individual;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class CrossoverAverage extends ACrossover {

    @Override
    public Individual crossover(Individual parent1, Individual parent2, AMutation mutation, ContestEvaluation evaluation) {
        double[] child_genome = new double[Individual.GENOME_SIZE];
        for(int i=0; i<Individual.GENOME_SIZE; i++) {
            child_genome[i] = (parent1.getGenome()[i] + parent2.getGenome()[i])/2;
        }
        child_genome = mutation.mutate(child_genome);

        return new Individual(child_genome, evaluation);
    }
}
