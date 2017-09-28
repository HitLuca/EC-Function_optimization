package src.genetics.components.crossover;

import src.genetics.Individual;

import java.util.ArrayList;

import static src.genetics.Population.BASE_GENOME_SIZE;
import static src.genetics.Population.FULL_GENOME_SIZE;

public class CrossoverAverageWeighted extends ACrossover {
    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();

        double[] childGenome = new double[FULL_GENOME_SIZE];
        double[] ratio = new double[parents.size()];
        double tot = 0.0;

        for (int i = 0; i < parents.size(); i++) {
            tot += parents.get(i).getFitness();
            ratio[i] = parents.get(i).getFitness();
        }

        for (int i = 0; i < BASE_GENOME_SIZE; i++) {
            childGenome[i] = 0.0;
            for (int j = 0; j < parents.size(); j++) {
                childGenome[i] += parents.get(j).getGenome()[i] * (ratio[j] / tot);
            }
        }

        childGenome = averageExtraGenome(childGenome, parents);

        children.add(new Individual(childGenome));
        return children;
    }
}