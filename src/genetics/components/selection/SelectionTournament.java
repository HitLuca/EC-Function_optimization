package src.genetics.components.selection;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public class SelectionTournament extends ASelection {
    private int tournamentSize;

    public SelectionTournament(Random rng, int parentsNumber, int tournamentSize) {
        super(rng, parentsNumber);
        this.tournamentSize = tournamentSize;
    }

    @Override
    public ArrayList<Individual> select(Population population) {
        ArrayList<Individual> selectedIndividuals = new ArrayList<>();

        ArrayList<Individual> tournament = new ArrayList<>();
        for(int k=0; k<tournamentSize; k++) {
            tournament.add(population.getIndividuals().get(rng.nextInt(population.getCurrentSize())));
        }
        tournament.sort(new Individual.FitnessComparator().reversed());
        for (int i = 0; i < parentsNumber; i++) {
            selectedIndividuals.add(tournament.get(i));
        }

        return selectedIndividuals;
    }
}
