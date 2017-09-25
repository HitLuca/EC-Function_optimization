package src;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class SelectionBestFitness implements ISelection {

    private int size;
    public SelectionBestFitness(int size) {
        this.size=size;
    }

    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        return select(population, size);
    }

    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population, int size) {
        population.sort(new FitnessComparator());
        return new ArrayList<>(population.subList(0, size));
    }
}
