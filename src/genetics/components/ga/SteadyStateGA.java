package src.genetics.components.ga;

import org.vu.contest.ContestEvaluation;
import src.genetics.*;
import src.genetics.components.crossover.ACrossover;
import src.genetics.components.mutation.AMutation;
import src.genetics.components.selection.ASelection;
import src.genetics.components.survival.ASurvival;

import java.io.IOException;
import java.util.ArrayList;

public class SteadyStateGA extends AGA {
    private int replacementNumber;

    public SteadyStateGA(int populationSize, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs, int replacementNumber) {
        super(populationSize, selection, crossover, mutation, survival, evaluation, epochs);
        this.replacementNumber = replacementNumber;
    }

    public void run() throws IOException
    {
        System.out.println("Scores:");
        System.out.println("epoch, mean fitness, best fitness, worst fitness");

        try {
            for (int epoch = 0; epoch < epochs; epoch++) {
                while (population.getCurrentSize() < population.getMaxSize() + replacementNumber) {
                    ArrayList<Individual> parents = selection.select(population.getIndividuals());
                    ArrayList<Individual> children = crossover.crossover(parents);
                    mutation.mutate(children);
                    population.addIndividuals(children);
                }
                population.evaluateFitness(evaluation);
                population.renewPopulation(survival.survival(population.getIndividuals(), population.getMaxSize()));
                System.out.println(epoch + ", " + population.getStatistics());
            }
        } catch (NullPointerException e) {
        }
        System.out.println("EndScores\n");
    }
}
