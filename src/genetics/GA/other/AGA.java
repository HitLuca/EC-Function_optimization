package src.genetics.GA.other;

import org.vu.contest.ContestEvaluation;
import src.genetics.AEA;
import src.genetics.GA.crossover.ACrossover;
import src.genetics.GA.mutation.AMutation;
import src.genetics.GA.selection.ASelection;
import src.genetics.GA.survival.ASurvival;

import java.util.Random;

public abstract class AGA extends AEA {
    protected Population population;
    protected ASurvival survival;
    protected ACrossover crossover;
    protected ASelection selection;
    protected AMutation mutation;
    protected ContestEvaluation evaluation;
    protected int epochs;
    protected Random rng;

    protected boolean printing;

    public AGA(Random rng, int populationSize, Stagnancy stagnancy, ASelection selection,
               ACrossover crossover, AMutation mutation, ASurvival survival,
               ContestEvaluation evaluation, int epochs, boolean printing) {
        this.rng = rng;
        this.population = new Population(rng, populationSize, evaluation, stagnancy);
        this.survival = survival;
        this.crossover = crossover;
        this.selection = selection;
        this.mutation = mutation;
        this.evaluation = evaluation;
        this.printing = printing;

        if (epochs == -1) {
            this.epochs = Integer.MAX_VALUE;
        } else {
            this.epochs = epochs;
        }
    }
}
