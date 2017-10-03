package src.genetics.components.crossover;

import src.genetics.Individual;

import java.util.ArrayList;
import java.util.Random;

import static src.genetics.Population.BASE_GENOME_SIZE;
import static src.genetics.Population.FULL_GENOME_SIZE;


public abstract class ACrossover {
    protected Random rng;

    public ACrossover(Random rng) {
        this.rng = rng;
    }

    protected double[] averageExtraGenome(double[] childGenome, ArrayList<Individual> parents) {
        for (int i = BASE_GENOME_SIZE; i < FULL_GENOME_SIZE; i++) {
            childGenome[i] = 0.0;
            for (int j = 0; j < parents.size(); j++) {
                childGenome[i] += parents.get(j).getGenome()[i] / parents.size();
            }
        }
        return childGenome;
    }

    public abstract ArrayList<Individual> crossover(ArrayList<Individual> parents);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
