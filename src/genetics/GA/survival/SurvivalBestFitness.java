package src.genetics.GA.survival;

import src.genetics.GA.other.Individual;
import src.genetics.GA.other.Population;

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
