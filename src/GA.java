package src;

import org.vu.contest.ContestEvaluation;
import src.components.MutationGaussian;

import java.util.ArrayList;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class GA {

    Population population;
    ISurvival survival;
    ICrossover crossover;
    ASelection selection;
    MutationGaussian mutation;
    ContestEvaluation evaluation;
    int epochs;

    public GA(Population population, ISurvival survival, ICrossover crossover, ASelection selection, MutationGaussian mutation, ContestEvaluation evaluation, int epochs) {
        this.population = population;
        this.survival = survival;
        this.crossover = crossover;
        this.selection = selection;
        this.mutation = mutation;
        this.evaluation = evaluation;
        this.epochs = epochs;
    }

    public void run()
    {
        for(int i=0; i<epochs; i++) {
            population.survive(survival);
            ArrayList<Individual> parents = population.select(selection);
            population.reproduce(parents, crossover, mutation, evaluation);
            population.updateStatistics();
        }
    }
}
