package src.components;

import src.AMutation;
import src.Individual;

import java.util.Random;

public class MutationGaussian extends AMutation {

    private double sigma;

    public MutationGaussian(double sigma) {
        this.sigma = sigma;
    }

    public double[] mutate(double[] child)
    {
        Random rnd = new Random();
        for(int i = 0; i< Individual.GENOME_SIZE; i++)
        {
            child[i]+= rnd.nextGaussian()*sigma;
        }

        return child;
    }
}
