package src.components;

import org.vu.contest.ContestEvaluation;
import src.ACrossover;
import src.AMutation;
import src.Individual;

import java.util.Random;

public class CrossoverCoinFlipWeighted extends ACrossover {

    @Override
    public Individual crossoverPair(Individual[] parentsPair, AMutation mutation, ContestEvaluation evaluation) {

        double[] child_genome = new double[Individual.GENOME_SIZE];
        Random rnd = new Random();
        double[] ratio = new double[parentsPair.length];
        double tot = 0.0;
        for (int i=0; i<parentsPair.length; i++){
            tot += parentsPair[i].getFitness();
            ratio[i] = tot;
        }

        for(int i=0; i<Individual.GENOME_SIZE; i++) {
            double t = rnd.nextDouble()*tot;
            for (int j=0; j<parentsPair.length; j++) {
                if (ratio[j] >= t) {
                    child_genome[i] = parentsPair[j].getGenome()[i];
                    break;
                }
            }
        }
        child_genome = mutation.mutate(child_genome);

        return new Individual(child_genome, evaluation);
    }
}