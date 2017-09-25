package src;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class Individual {

    public static final int GENOME_SIZE = 10;
    private static int object_count = 0;

    private double genome[];
    private double fitness;
    private int id;

    public Individual(ContestEvaluation evaluation)
    {
        object_count++;
        id = object_count;

        double genome[] = new double[10];
        Random rnd = new Random();
        for (int i=0; i<10; i++)
            genome[i] = rnd.nextDouble()*10-5;

        fitness = (double) evaluation.evaluate(genome);
    }

    public Individual(double _genome[], ContestEvaluation evaluation)
    {
        object_count++;
        id = object_count;
        genome = _genome;
        fitness = (double) evaluation.evaluate(genome);
    }

    public double[] getGenome() {
        return genome;
    }

    public double getFitness() {
        return fitness;
    }

    public int getId() {
        return id;
    }


}
