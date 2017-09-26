package src;

import org.vu.contest.ContestEvaluation;
import src.components.MutationGaussian;

import java.util.ArrayList;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class GA {

    private Population population;
    private ASurvival survival;
    private ACrossover crossover;
    private ASelection selection;
    private MutationGaussian mutation;
    private ContestEvaluation evaluation;
    private int epochs;

    public GA(Population population, ASelection selection, ACrossover crossover, MutationGaussian mutation, ASurvival survival,ContestEvaluation evaluation, int epochs) {
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
            ArrayList<Individual> parents = population.select(selection);
            population.reproduce(parents, crossover, mutation, evaluation);
            population.updateStatistics();
            population.survive(survival);
        }
    }
}
