package src.genetics.components.ga;

import org.vu.contest.ContestEvaluation;
import src.genetics.Individual;
import src.genetics.components.Stagnancy;
import src.genetics.components.crossover.ACrossover;
import src.genetics.components.mutation.AMutation;
import src.genetics.components.selection.ASelection;
import src.genetics.components.survival.ASurvival;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SteadyStateGA extends AGA {
    private int replacementNumber;

    public SteadyStateGA(Random rng, int populationSize, Stagnancy stagnancy, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs, int replacementNumber) {
        super(rng, populationSize, stagnancy, selection, crossover, mutation, survival, evaluation, epochs);
        this.replacementNumber = replacementNumber;
    }

    public SteadyStateGA(Random rng, int populationSize, int fullGenomeSize, Stagnancy stagnancy, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs, int replacementNumber) {
        super(rng, populationSize, fullGenomeSize, stagnancy, selection, crossover, mutation, survival, evaluation, epochs);
        this.replacementNumber = replacementNumber;
    }

    public void run() {
        System.out.println("Scores:");
        try {
            for (int epoch = 0; epoch < epochs; epoch++) {
                while (population.getCurrentSize() < population.getMaxSize() + replacementNumber) {
                    ArrayList<Individual> parents = selection.select(population.getIndividuals());
                    ArrayList<Individual> children = crossover.crossover(parents);
                    mutation.mutate(children);
                    population.addIndividuals(children);
                }
                population.evaluateFitness(evaluation);
//                population.calculateSharedFitness();
                population.renewPopulation(survival.survival(population.getIndividuals(), population.getMaxSize()));
                population.updateStatistics();
                System.out.println(epoch + ", " + population.getStatistics());
            }
        } catch (Exception e) {
            System.out.println("EndScores\n");
            throw e;
        }
        System.out.println("EndScores\n");
    }
}
