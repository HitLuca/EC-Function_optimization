package src.components;

import org.vu.contest.ContestEvaluation;
import src.ACrossover;
import src.AMutation;
import src.Individual;

import java.util.Random;

public class CrossoverCoinFlip extends ACrossover {


    @Override
    public Individual crossoverPair(Individual[] parentsPair, AMutation mutation, ContestEvaluation evaluation) {

        double[] child_genome = new double[Individual.GENOME_SIZE];
        Random rnd = new Random();
        for(int i=0; i<Individual.GENOME_SIZE; i++) {
            int j = (int) rnd.nextFloat()*parentsPair.length;
            child_genome[i] = parentsPair[j].getGenome()[i];
        }
        child_genome = mutation.mutate(child_genome);

        return new Individual(child_genome, evaluation);
    }
}
