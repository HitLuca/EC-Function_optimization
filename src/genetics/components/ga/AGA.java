package src.genetics.components.ga;

import org.vu.contest.ContestEvaluation;
import src.genetics.Population;
import src.genetics.components.Stagnancy;
import src.genetics.components.crossover.ACrossover;
import src.genetics.components.mutation.AMutation;
import src.genetics.components.selection.ASelection;
import src.genetics.components.survival.ASurvival;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public abstract class AGA {
    protected Population population;
    protected ASurvival survival;
    protected ACrossover crossover;
    protected ASelection selection;
    protected AMutation mutation;
    protected ContestEvaluation evaluation;
    protected int epochs;
    protected Random rng;


    public AGA(Random rng, int populationSize, Stagnancy stagnancy, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs) {
        this.rng = rng;
        this.population = new Population(rng, populationSize, evaluation, stagnancy);
        this.survival = survival;
        this.crossover = crossover;
        this.selection = selection;
        this.mutation = mutation;
        this.evaluation = evaluation;

        Properties props = evaluation.getProperties();
        int evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));

        if (epochs == -1) {
            this.epochs = Integer.MAX_VALUE;
        } else {
            this.epochs = epochs; //Math.min(epochs, (evaluations_limit_-population.getMaxSize())/(int) Math.ceil(selection.getSize()/pairSize));
        }
    }

    public AGA(Random rng, int populationSize, int fullGenomeSize, Stagnancy stagnancy, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs) {
        this.rng = rng;
        this.population = new Population(rng, populationSize, evaluation, fullGenomeSize, stagnancy);
        this.survival = survival;
        this.crossover = crossover;
        this.selection = selection;
        this.mutation = mutation;
        this.evaluation = evaluation;

        Properties props = evaluation.getProperties();
        int evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));

        if (epochs == -1) {
            this.epochs = Integer.MAX_VALUE;
        } else {
            this.epochs = epochs; //Math.min(epochs, (evaluations_limit_-population.getMaxSize())/(int) Math.ceil(selection.getSize()/pairSize));
        }
    }

    public abstract void run() throws IOException;

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
