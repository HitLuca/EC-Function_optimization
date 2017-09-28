package src.genetics.components.selection;

import src.genetics.Individual;

import java.util.ArrayList;


public abstract class ASelection {
    protected int parentsNumber;
    public ASelection(int parentsNumber) {
            this.parentsNumber=parentsNumber;
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
