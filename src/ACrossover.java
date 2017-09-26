package src;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;

/**
 * Created by Недко on 21.9.2017 г..
 */
public abstract class ACrossover {

    public abstract ArrayList<Individual> crossover(ArrayList<Individual> parentsPool, AMutation mutation, ContestEvaluation evaluation);
    public abstract Individual crossoverPair(ArrayList<Individual> parentsPair, AMutation mutation, ContestEvaluation evaluation);
}
