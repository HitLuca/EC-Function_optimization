package src.components;

import src.ASurvival;
import src.Individual;

import java.util.ArrayList;

public class SurvivalBestFitness extends ASurvival {

    public SurvivalBestFitness(int size) {
        super(size);
    }

    @Override
    public ArrayList<Individual> survival(ArrayList<Individual> population, int size) {
        population.sort(new Individual.FitnessComparator().reversed());

        return new ArrayList<>(population.subList(0, size));
    }

}
