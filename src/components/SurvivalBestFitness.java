package src.components;

import src.ASelection;
import src.ASurvival;
import src.Individual;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class SurvivalBestFitness extends ASurvival {

    public SurvivalBestFitness(int size) {
        super(size);
    }

    @Override
    public ArrayList<Individual> survival(ArrayList<Individual> population, int size) {
        Collections.sort(population, new ASelection.FitnessComparator());
        return new ArrayList<>(population.subList(0, size));
    }

}
