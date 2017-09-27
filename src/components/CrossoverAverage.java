package src.components;

import src.ACrossover;
import src.Individual;

import java.util.ArrayList;

import static src.Population.BASE_GENOME_SIZE;
import static src.Population.FULL_GENOME_SIZE;

public class CrossoverAverage extends ACrossover {

    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();
        double[] childGenome = new double[FULL_GENOME_SIZE];

        for (int i = 0; i < BASE_GENOME_SIZE; i++) {
            childGenome[i] = 0.0;
            for (int j = 0; j < parents.size(); j++) {
                childGenome[i] += parents.get(j).getGenome()[i] / parents.size();
            }
        }

        childGenome = averageExtraGenome(childGenome, parents);

        children.add(new Individual(childGenome));
        return children;
    }
}
