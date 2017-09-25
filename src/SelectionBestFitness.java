package src;

import java.util.ArrayList;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class SelectionBestFitness extends ASelection {
    public SelectionBestFitness(int size) {
        super(size);
    }

    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population, int size) {
        population.sort(new FitnessComparator());
        return new ArrayList<>(population.subList(0, size));
    }
}
