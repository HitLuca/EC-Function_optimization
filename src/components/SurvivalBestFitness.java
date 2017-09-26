package src.components;

import src.ASelection;
import src.ISurvival;
import src.Individual;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class SurvivalBestFitness implements ISurvival {

    private int size;
    public SurvivalBestFitness(int size) {
        this.size = size;
    }

    @Override
    public ArrayList<Individual> survival(ArrayList<Individual> population, int size) {
        Collections.sort(population, new ASelection.FitnessComparator());
        return new ArrayList<>(population.subList(0, size));
    }

    @Override
    public ArrayList<Individual> survival(ArrayList<Individual> population) {
        return survival( population, size);
    }
}
