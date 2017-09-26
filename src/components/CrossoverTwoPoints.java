package src.components;

import src.ACrossover;
import src.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CrossoverTwoPoints extends ACrossover {
    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();

        assert parents.size() == 2;
        double[] child1Genome = new double[Individual.GENOME_SIZE];
        double[] child2Genome = new double[Individual.GENOME_SIZE];

        Random rng = new Random();

        int[] crossoverIndexes = new int[2];

        for(int i=0; i<crossoverIndexes.length; i++) {
            crossoverIndexes[i] = rng.nextInt(Individual.GENOME_SIZE);
        }

        Arrays.sort(crossoverIndexes);

        boolean swap = false;
        int crossoverCurrentIndex = 0;

        for(int i=0; i<Individual.GENOME_SIZE; i++) {
            if(swap) {
                child1Genome[i] = parents.get(1).getGenome()[i];
                child2Genome[i] = parents.get(0).getGenome()[i];
            } else {
                child1Genome[i] = parents.get(0).getGenome()[i];
                child2Genome[i] = parents.get(1).getGenome()[i];
            }
            if(crossoverCurrentIndex < crossoverIndexes.length && i == crossoverIndexes[crossoverCurrentIndex]) {
                swap = !swap;
                crossoverCurrentIndex ++;
            }
        }

        children.add(new Individual(child1Genome));
        children.add(new Individual(child2Genome));

        return children;
    }
}
