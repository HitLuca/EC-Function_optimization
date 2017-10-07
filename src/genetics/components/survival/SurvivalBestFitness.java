package src.genetics.components.survival;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public class SurvivalBestFitness extends ASurvival {

    public SurvivalBestFitness(Random rng) {
        super(rng);
    }

    @Override
    public ArrayList<Individual> survival(Population population) {
        population.sortIndividualsReversed();

        return new ArrayList<>(population.getIndividuals().subList(0, population.getMaxSize()));
    }

}
