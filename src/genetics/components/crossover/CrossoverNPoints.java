package src.genetics.components.crossover;

import src.genetics.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static src.genetics.Population.BASE_GENOME_SIZE;

public class CrossoverNPoints extends ACrossover {
    private int points;

    public CrossoverNPoints(Random rng, int points) {
        super(rng);
        this.points = points;
    }

    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();

        assert parents.size() == 2;
        double[] child1Genome = new double[BASE_GENOME_SIZE];
        double[] child2Genome = new double[BASE_GENOME_SIZE];

        int[] crossoverIndexes = new int[points];

        for (int i = 0; i < crossoverIndexes.length; i++) {
            crossoverIndexes[i] = rng.nextInt(BASE_GENOME_SIZE);
        }

        Arrays.sort(crossoverIndexes);

        boolean swap = false;
        int crossoverCurrentIndex = 0;

        for (int i = 0; i < BASE_GENOME_SIZE; i++) {
            if (swap) {
                child1Genome[i] = parents.get(1).getGenome()[i];
                child2Genome[i] = parents.get(0).getGenome()[i];
            } else {
                child1Genome[i] = parents.get(0).getGenome()[i];
                child2Genome[i] = parents.get(1).getGenome()[i];
            }
            if (crossoverCurrentIndex < crossoverIndexes.length && i == crossoverIndexes[crossoverCurrentIndex]) {
                swap = !swap;
                crossoverCurrentIndex++;
            }
        }

        child1Genome = averageExtraGenome(child1Genome, parents);
        child2Genome = averageExtraGenome(child2Genome, parents);

        children.add(new Individual(child1Genome));
        children.add(new Individual(child2Genome));

        return children;
    }
}
