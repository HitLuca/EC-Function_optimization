package src;

import java.util.ArrayList;

import static src.Population.BASE_GENOME_SIZE;
import static src.Population.FULL_GENOME_SIZE;


public abstract class ACrossover {

    protected double[] averageExtraGenome(double[] childGenome, ArrayList<Individual> parents)
    {
        for (int i = BASE_GENOME_SIZE; i < FULL_GENOME_SIZE; i++) {
            childGenome[i] = 0.0;
            for (int j = 0; j < parents.size(); j++) {
                childGenome[i] += parents.get(j).getGenome()[i] / parents.size();
            }
        }
        return childGenome;
    }

    public abstract ArrayList<Individual> crossover(ArrayList<Individual> parents);
}
