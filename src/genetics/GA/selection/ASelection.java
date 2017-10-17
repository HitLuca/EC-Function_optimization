package src.genetics.GA.selection;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;


public abstract class ASelection {
    protected Random rng;
    protected int parentsNumber;

    public ASelection(Random rng, int parentsNumber) {
        this.rng = rng;
        this.parentsNumber = parentsNumber;
    }

    public abstract ArrayList<Individual> select(Population population);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
