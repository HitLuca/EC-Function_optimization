package src;

import java.util.Random;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class Mutation {

    public Mutation() {
    }

    public double[] mutate(double[] child)
    {
        Random rnd = new Random();
        for(int i=0; i<Individual.GENOME_SIZE; i++)
        {
            child[i]+= rnd.nextGaussian()/10;
        }

        return child;
    }
}
