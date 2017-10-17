package src.genetics.GA.survival;

import src.genetics.GA.other.Individual;
import src.genetics.GA.other.Population;

import java.util.ArrayList;
import java.util.Random;

public abstract class ASurvival {
    protected Random rng;

    public ASurvival(Random rng) {
        this.rng = rng;
    }

    public abstract ArrayList<Individual> survival(Population population);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
