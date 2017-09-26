package src.components;

import src.ASelection;
import src.Individual;

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
        population.sort(new  Individual.FitnessComparatorDecreasing());
        return new ArrayList<>(population.subList(0, size));
    }
}
