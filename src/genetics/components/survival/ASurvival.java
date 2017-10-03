package src.genetics.components.survival;

import src.genetics.Individual;

import java.util.ArrayList;
import java.util.Random;

public abstract class ASurvival {
    protected Random rng;

    public ASurvival(Random rng) {
        this.rng = rng;
    }

    public abstract ArrayList<Individual> survival(ArrayList<Individual> population, int size);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
