package src.components;

import src.ACrossover;
import src.Individual;

import java.util.ArrayList;
import java.util.Random;

public class CrossoverCoinFlip extends ACrossover {
    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();

        double[] childGenome = new double[Individual.GENOME_SIZE];
        Random rnd = new Random();

        for(int i=0; i<Individual.GENOME_SIZE; i++) {
            int j = (int) rnd.nextFloat()*parents.size();
            childGenome[i] = parents.get(j).getGenome()[i];
        }

        children.add(new Individual(childGenome));
        return children;
    }
}
