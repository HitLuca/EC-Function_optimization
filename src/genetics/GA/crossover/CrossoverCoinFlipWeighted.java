package src.genetics.GA.crossover;

import src.genetics.GA.other.Individual;

import java.util.ArrayList;
import java.util.Random;

import static src.genetics.GA.other.Population.GENOME_SIZE;

public class CrossoverCoinFlipWeighted extends ACrossover {
    public CrossoverCoinFlipWeighted(Random rng) {
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
            ratio[i] = tot;
        }

        for (int i = 0; i < GENOME_SIZE; i++) {
            double t = rng.nextDouble() * tot;
            for (int j = 0; j < parents.size(); j++) {
                if (ratio[j] >= t) {
                    childGenome[i] = parents.get(j).getGenome()[i];
                    break;
                }
            }
        }

        children.add(new Individual(childGenome));
        return children;
    }
}