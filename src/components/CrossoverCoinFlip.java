package src.components;

import src.ACrossover;
import src.Individual;

import java.util.ArrayList;
import java.util.Random;

import static src.Population.BASE_GENOME_SIZE;
import static src.Population.FULL_GENOME_SIZE;

public class CrossoverCoinFlip extends ACrossover {
    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();

        double[] childGenome = new double[FULL_GENOME_SIZE];
        Random rnd = new Random();

        for(int i = 0; i<BASE_GENOME_SIZE; i++) {
            int j = (int) rnd.nextFloat()*parents.size();
            childGenome[i] = parents.get(j).getGenome()[i];
        }

        childGenome = averageExtraGenome(childGenome, parents);

        children.add(new Individual(childGenome));
        return children;
    }
}
