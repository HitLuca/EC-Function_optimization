package src.components;

import src.ACrossover;
import src.Individual;

import java.util.ArrayList;

public class CrossoverAverage extends ACrossover {

    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();
        double[] childGenome = new double[Individual.GENOME_SIZE];

        for (int i = 0; i < Individual.GENOME_SIZE; i++) {
            childGenome[i] = 0.0;
            for (int j = 0; j < parents.size(); j++) {
                childGenome[i] += parents.get(j).getGenome()[i] / parents.size();
            }
        }

        children.add(new Individual(childGenome));
        return children;
    }
}
