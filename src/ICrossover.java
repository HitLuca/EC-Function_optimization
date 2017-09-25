package src;

import org.vu.contest.ContestEvaluation;

/**
 * Created by Недко on 21.9.2017 г..
 */
public interface ICrossover {

    public Individual crossover(Individual parent1, Individual parent2, Mutation mutation, ContestEvaluation evaluation);
}
