package src.genetics.components.mutation;

import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public class MutationUniform extends AMutation {

    public MutationUniform(Random rng, double max, double mutationProbability) {
        super(rng, max, mutationProbability);
    }

    protected double specificMutation(double max){
        return (rng.nextDouble() * max * 2) - max;
    }

}