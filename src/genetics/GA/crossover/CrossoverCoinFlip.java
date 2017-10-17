package src.genetics.GA.crossover;

import src.genetics.GA.other.Individual;

import java.util.ArrayList;
import java.util.Random;

import static src.genetics.GA.other.Population.BASE_GENOME_SIZE;

public class CrossoverCoinFlip extends ACrossover {
    public CrossoverCoinFlip(Random rng) {
        super(rng);
    }

    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();

        double[] childGenome = new double[BASE_GENOME_SIZE];

        for (int i = 0; i < BASE_GENOME_SIZE; i++) {
            int j = (int) rng.nextFloat() * parents.size();
            childGenome[i] = parents.get(j).getGenome()[i];
        }

        children.add(new Individual(childGenome));
        return children;
    }
}
