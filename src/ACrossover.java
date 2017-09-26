package src;

import org.vu.contest.ContestEvaluation;

/**
 * Created by Недко on 21.9.2017 г..
 */
public abstract class ACrossover {

    public abstract Individual crossover(Individual parent1, Individual parent2, AMutation mutation, ContestEvaluation evaluation);
}
