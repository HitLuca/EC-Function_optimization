package src.components;

import src.ACrossover;
import src.Individual;

import java.util.ArrayList;

public class CrossoverAverageWeighted extends ACrossover {
    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();

        double[] childGenome = new double[Individual.GENOME_SIZE];
        double[] ratio = new double[parents.size()];
        double tot = 0.0;

        for (int i = 0; i < parents.size(); i++) {
            tot += parents.get(i).getFitness();
            ratio[i] = parents.get(i).getFitness();
        }

        for (int i = 0; i < Individual.GENOME_SIZE; i++) {
            childGenome[i] = 0.0;
            for (int j = 0; j < parents.size(); j++) {
                childGenome[i] += parents.get(j).getGenome()[i] * (ratio[j] / tot);
            }
        }

        children.add(new Individual(childGenome));
        return children;
    }
}