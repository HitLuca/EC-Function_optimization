package src;

import org.vu.contest.ContestEvaluation;
import src.components.MutationGaussian;

import java.util.ArrayList;

public class GA {

    private Population population;
    private ASurvival survival;
    private ACrossover crossover;
    private ASelection selection;
    private MutationGaussian mutation;
    private ContestEvaluation evaluation;
    private int epochs;
    private  int parentsNumber;

    public GA(Population population, ASelection selection, int parentsNumber, ACrossover crossover, MutationGaussian mutation, ASurvival survival,ContestEvaluation evaluation, int epochs) {
        this.population = population;
        this.survival = survival;
        this.crossover = crossover;
        this.selection = selection;
        this.mutation = mutation;
        this.evaluation = evaluation;
        this.epochs = epochs;
        this.parentsNumber = parentsNumber;
    }

    public void run()
    {
        for(int i=0; i<epochs; i++) {
            ArrayList<Individual> parents = population.select(selection);
            population.reproduce(parents, parentsNumber, crossover, mutation, evaluation);
            population.updateStatistics();
            population.survive(survival);
        }
    }
}
