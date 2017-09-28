package src.genetics.components.crossover;

import src.genetics.Individual;

import java.util.ArrayList;

import static src.genetics.Population.BASE_GENOME_SIZE;
import static src.genetics.Population.FULL_GENOME_SIZE;

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
