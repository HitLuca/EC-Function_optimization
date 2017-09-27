package src;

import org.vu.contest.ContestEvaluation;

import java.util.Properties;

public abstract class AGA {
    protected Population population;
    protected ASurvival survival;
    protected ACrossover crossover;
    protected ASelection selection;
    protected AMutation mutation;
    protected ContestEvaluation evaluation;
    protected int epochs;

    public AGA(Population population, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs) {
        this.population = population;
        this.survival = survival;
        this.crossover = crossover;
        this.selection = selection;
        this.mutation = mutation;
        this.evaluation = evaluation;

        Properties props = evaluation.getProperties();
        int evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));

        if(epochs == -1) {
            this.epochs = Integer.MAX_VALUE;
        } else {
            this.epochs = epochs; //Math.min(epochs, (evaluations_limit_-population.getMaxSize())/(int) Math.ceil(selection.getSize()/pairSize));
        }
    }

    public abstract void run();
}
