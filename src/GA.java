package src;

import org.vu.contest.ContestEvaluation;
import src.components.MutationGaussian;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Properties;

public class GA {

    private Population population;
    private ASurvival survival;
    private ACrossover crossover;
    private ASelection selection;
    private MutationGaussian mutation;
    private ContestEvaluation evaluation;
    private int epochs;
    private int parentsNumber;

    public GA(Population population, ASelection selection, int pairSize, ACrossover crossover, MutationGaussian mutation, ASurvival survival,ContestEvaluation evaluation, int epochs) {
        this.population = population;
        this.survival = survival;
        this.crossover = crossover;
        this.selection = selection;
        this.mutation = mutation;
        this.evaluation = evaluation;

        Properties props = evaluation.getProperties();
        int evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));

        this.epochs = Math.min(epochs, (evaluations_limit_-population.getSize())/(int) Math.ceil(selection.getSize()/pairSize));
        this.parentsNumber = pairSize;
    }

    public void run()
    {
        for(int i=0; i<epochs; i++) {
            ArrayList<Individual> parents = population.select(selection);
            population.reproduce(parents, parentsNumber, crossover, mutation, evaluation);
            System.out.print("Epoch: " + i + " ");
            population.updateStatistics();
            population.survive(survival);
        }
    }
}
