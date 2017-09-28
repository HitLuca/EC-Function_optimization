package src.genetics.components.mutation;

import src.genetics.Individual;

import java.util.ArrayList;

public abstract class AMutation {
    public abstract void mutate(ArrayList<Individual> individuals);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
