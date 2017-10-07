package src.genetics.components.selection;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public class SelectionFitnessProportional extends ASelection {
    private boolean windowing;

    private double worstFitness;
    private double totalWeight;

    private int populationHashCode;

    public SelectionFitnessProportional(Random rng, int size, boolean windowing) {
        super(rng, size);
        this.windowing = windowing;
    }

    @Override
    public ArrayList<Individual> select(Population population) {
        int popHash = population.hashCode();

        ArrayList<Individual> selectedIndividuals = new ArrayList<>();

        if (popHash != populationHashCode) {
            if (windowing) {
                population.sortIndividuals();
                worstFitness = population.getIndividuals().get(0).getFitness();
            }


            totalWeight = 0;
            for (Individual i : population.getIndividuals()) {
                totalWeight += i.getFitness();
            }

            totalWeight -= population.getCurrentSize() * worstFitness;
            populationHashCode = popHash;
        }

        for (int i = 0; i < parentsNumber; i++) {
            double randomValue = rng.nextDouble() * totalWeight;
            for (int j = 0; j < population.getCurrentSize(); j++) {
                randomValue -= population.getIndividuals().get(j).getFitness();
                randomValue += worstFitness;
                if (randomValue <= 0) {
                    selectedIndividuals.add(population.getIndividuals().get(j));
                    break;
                }
            }
        }

        return selectedIndividuals;
    }
}
