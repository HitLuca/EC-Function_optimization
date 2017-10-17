package src.genetics.components.selection;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public class SelectionLinearRanking extends ASelection {
    private double selectionPressure;
    private int populationHashCode;
    private double totalWeight;

    public SelectionLinearRanking(Random rng, int parentsNumber, double selectionPressure) {
        super(rng, parentsNumber);
        this.selectionPressure = selectionPressure;
    }

    @Override
    public ArrayList<Individual> select(Population population) {
        int popHash = population.hashCode();
        ArrayList<Individual> selectedIndividuals = new ArrayList<>();

        if(popHash != populationHashCode) {
            population.sortIndividuals();

            totalWeight = 0;
            for (int i = 0; i < population.getCurrentSize(); i++) {
                int pos = i + 1;
                totalWeight += 2 - selectionPressure + 2 * (selectionPressure - 1) * (pos - 1) / (population.getCurrentSize() - 1);
            }
            populationHashCode = popHash;
        }


        for (int i = 0; i < parentsNumber; i++) {
            double randomValue = rng.nextDouble() * totalWeight;
            for (int j = 0; j < population.getCurrentSize(); j++) {
                int pos = j + 1;
                randomValue -= 2 - selectionPressure + 2 * (selectionPressure - 1) * (pos - 1) / (population.getCurrentSize() - 1);
                if (randomValue <= 0) {
                    selectedIndividuals.add(population.getIndividuals().get(j));
                    break;
                }
            }
        }

        return selectedIndividuals;
    }
}
