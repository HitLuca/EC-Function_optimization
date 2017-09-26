package src.components;

import org.vu.contest.ContestEvaluation;
import src.ACrossover;
import src.AMutation;
import src.Individual;

public class CrossoverAverage extends ACrossover {

    @Override
    public Individual crossoverPair(Individual[] parentsPair, AMutation mutation, ContestEvaluation evaluation) {
        double[] child_genome = new double[Individual.GENOME_SIZE];

        for (int i = 0; i < Individual.GENOME_SIZE; i++) {
            child_genome[i] = 0.0;
            for (int j = 0; j < parentsPair.length; j++) {
                child_genome[i] += parentsPair[j].getGenome()[i] / parentsPair.length;
            }
        }
        child_genome = mutation.mutate(child_genome);

        return new Individual(child_genome, evaluation);
    }
//    @Override
//    public ArrayList<Individual> crossover(int parentsNumber, ArrayList<Individual> parentsPool, AMutation mutation, ContestEvaluation evaluation) {
//        Collections.shuffle(parentsPool);
//
//        ArrayList<Individual> pair = new ArrayList<Individual>();
//
//        ArrayList<Individual> children = new ArrayList<>();
//
//        for (int i=0; i+1<parentsPool.size(); i+=2)
//        {
//            if(pair.size()<2){
//                pair.add(parentsPool.get(i));
//                pair.add(parentsPool.get(i+1));
//            }
//            else {
//                pair.set(0, parentsPool.get(i));
//                pair.set(1, parentsPool.get(i + 1));
//            }
//
//            children.add(crossoverPair(pair, mutation, evaluation));
//        }
//
//        return children;
//    }

//    @Override
//    public Individual crossoverPair(ArrayList<Individual> parentsPair, AMutation mutation, ContestEvaluation evaluation) {
//        Individual parent1 = parentsPair.get(0);
//        Individual parent2 = parentsPair.get(1);
//
//        double[] child_genome = new double[Individual.GENOME_SIZE];
//        for(int i=0; i<Individual.GENOME_SIZE; i++) {
//            child_genome[i] = (parent1.getGenome()[i] + parent2.getGenome()[i])/2;
//        }
//        child_genome = mutation.mutate(child_genome);
//
//        return new Individual(child_genome, evaluation);
//    }
}
