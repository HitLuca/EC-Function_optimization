package src.genetics.GA.crossover;

import src.genetics.GA.other.Individual;

import java.util.ArrayList;
import java.util.Random;

import static src.genetics.GA.other.Population.GENOME_SIZE;

public class CrossoverAverageWeighted extends ACrossover {
    public CrossoverAverageWeighted(Random rng) {
        super(rng);
    }

    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();

        double[] childGenome = new double[GENOME_SIZE];
        double[] ratio = new double[parents.size()];
        double tot = 0.0;

        for (int i = 0; i < parents.size(); i++) {
            tot += parents.get(i).getFitness();
            ratio[i] = parents.get(i).getFitness();
        }

        // to fix the problem where both parents have fitness of 0 (0/0 = NaN)
        if (tot == 0) {
            tot = 1;
        }

        for (int i = 0; i < GENOME_SIZE; i++) {
            childGenome[i] = 0.0;
            for (int j = 0; j < parents.size(); j++) {
                childGenome[i] += parents.get(j).getGenome()[i] * (ratio[j] / tot);
            }
        }

        children.add(new Individual(childGenome));
        return children;
    }
}