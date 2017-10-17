package src.genetics.GA.crossover;

import src.genetics.GA.other.Individual;

import java.util.ArrayList;
import java.util.Random;

import static src.genetics.GA.other.Population.GENOME_SIZE;

public class CrossoverAverage extends ACrossover {

    public CrossoverAverage(Random rng) {
        super(rng);
    }

    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();
        double[] childGenome = new double[GENOME_SIZE];

        for (int i = 0; i < GENOME_SIZE; i++) {
            childGenome[i] = 0.0;
            for (int j = 0; j < parents.size(); j++) {
                childGenome[i] += parents.get(j).getGenome()[i] / parents.size();
            }
        }

        children.add(new Individual(childGenome));
        return children;
    }
}
