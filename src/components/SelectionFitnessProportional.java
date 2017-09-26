package src.components;

import src.ASelection;
import src.Individual;

import java.util.ArrayList;
import java.util.Random;

public class SelectionFitnessProportional extends ASelection {

    public SelectionFitnessProportional(int size) {
        super(size);
    }

    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population, int size) {
        ArrayList<Individual> selectedIndividuals = new ArrayList<>();
        Random rng = new Random();

        double totalWeight = 0;
        for(Individual i:population) {
            totalWeight += i.getFitness();
        }

        for(int i=0; i<size; i++) {
            double randomValue = rng.nextDouble() * totalWeight;
            for(int j=0; j<population.size(); j++) {
                randomValue -= population.get(j).getFitness();
                if (randomValue <=0) {
                    selectedIndividuals.add(population.get(j));
                    break;
                }
            }
        }

        assert selectedIndividuals.size() == size;
        return selectedIndividuals;
    }
}
