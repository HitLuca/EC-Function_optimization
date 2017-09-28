package src.genetics.components.selection;

import src.genetics.Individual;

import java.util.ArrayList;
import java.util.Random;

public class SelectionLinearRanking extends ASelection {
    double selectionPressure;

    public SelectionLinearRanking(int size, double selectionPressure) {
        super(size);
        this.selectionPressure = selectionPressure;
    }

    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        population.sort(new Individual.FitnessComparator());

        ArrayList<Individual> selectedIndividuals = new ArrayList<>();
        Random rng = new Random();

        double totalWeight = 0;
        for(int i=0; i<population.size(); i++) {
            int pos = i+1;
            totalWeight += 2 - selectionPressure + 2 * (selectionPressure - 1) * (pos - 1) / (population.size() - 1);
        }

        for(int i=0; i<parentsNumber; i++) {
            double randomValue = rng.nextDouble() * totalWeight;
            for(int j=0; j<population.size(); j++) {
                int pos = j+1;
                randomValue -= 2 - selectionPressure + 2 * (selectionPressure - 1) * (pos - 1) / (population.size() - 1);
                if (randomValue <=0) {
                    selectedIndividuals.add(population.get(j));
                    break;
                }
            }
        }

        assert selectedIndividuals.size() == parentsNumber;
        return selectedIndividuals;
    }
}
