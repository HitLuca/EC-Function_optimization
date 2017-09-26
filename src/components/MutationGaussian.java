package src.components;

import src.AMutation;
import src.Individual;

import java.util.Random;

public class MutationGaussian extends AMutation {

    public MutationGaussian() {
    }

    public double[] mutate(double[] child)
    {
        Random rnd = new Random();
        for(int i = 0; i< Individual.GENOME_SIZE; i++)
        {
            child[i]+= rnd.nextGaussian()/10;
        }

        return child;
    }
}
