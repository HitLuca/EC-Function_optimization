package src.genetics.components.selection;

import src.genetics.Individual;

import java.util.ArrayList;
import java.util.Random;

public class SelectionFitnessProportional extends ASelection {
    private boolean windowing;

    public SelectionFitnessProportional(int size, boolean windowing) {
        super(size);
        this.windowing = windowing;
    }

    @Override
    public ArrayList<Individual> select(ArrayList<Individual> population) {
        double worstFitness = 0;

        if(windowing) {
            population.sort(new Individual.FitnessComparator());
            worstFitness = population.get(0).getFitness();
        }

        ArrayList<Individual> selectedIndividuals = new ArrayList<>();
        Random rng = new Random();

        double totalWeight = 0;
        for(Individual i:population) {
            totalWeight += i.getFitness();
        }

        totalWeight -= population.size() * worstFitness;

        for(int i=0; i<parentsNumber; i++) {
            double randomValue = rng.nextDouble() * totalWeight;
            for(int j=0; j<population.size(); j++) {
                randomValue -= population.get(j).getFitness();
                randomValue += worstFitness;
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
