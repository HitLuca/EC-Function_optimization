package src.genetics.components.crossover;

import src.genetics.Individual;

import java.util.ArrayList;
import java.util.Random;


public abstract class ACrossover {
    protected Random rng;

    public ACrossover(Random rng) {
        this.rng = rng;
    }

    public abstract ArrayList<Individual> crossover(ArrayList<Individual> parents);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
