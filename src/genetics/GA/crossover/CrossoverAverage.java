package src.genetics.GA.crossover;

import src.genetics.GA.other.Individual;

import java.util.ArrayList;
import java.util.Random;

import static src.genetics.GA.other.Population.BASE_GENOME_SIZE;

public class CrossoverAverage extends ACrossover {

    public CrossoverAverage(Random rng) {
        super(rng);
    }

    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();
        double[] childGenome = new double[BASE_GENOME_SIZE];

        for (int i = 0; i < BASE_GENOME_SIZE; i++) {
            childGenome[i] = 0.0;
            for (int j = 0; j < parents.size(); j++) {
                childGenome[i] += parents.get(j).getGenome()[i] / parents.size();
            }
        }

        children.add(new Individual(childGenome));
        return children;
    }
}
