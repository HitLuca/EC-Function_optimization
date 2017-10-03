package src.genetics.components.selection;

import src.genetics.Individual;

import java.util.ArrayList;
import java.util.Random;


public abstract class ASelection {
    protected Random rng;
    protected int parentsNumber;

    public ASelection(Random rng, int parentsNumber) {
        this.rng = rng;
        this.parentsNumber = parentsNumber;
    }

    public abstract ArrayList<Individual> select(ArrayList<Individual> population);

    public int getSize() {
        return parentsNumber;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
