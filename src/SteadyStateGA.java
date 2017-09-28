package src;

import org.vu.contest.ContestEvaluation;

import java.io.IOException;
import java.util.ArrayList;

public class SteadyStateGA extends AGA {
    private int replacementNumber;

    public SteadyStateGA(Population population, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs, int replacementNumber) {
        super(population, selection, crossover, mutation, survival, evaluation, epochs);
        this.replacementNumber = replacementNumber;
    }

    public void run() throws IOException
    {
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
                System.out.println("Epoch " + epoch + " " + population.getStatistics());
            }
        } catch (NullPointerException e) {

        }
    }
}
