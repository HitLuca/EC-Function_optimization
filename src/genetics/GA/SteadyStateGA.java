package src.genetics.GA;

import org.vu.contest.ContestEvaluation;
import src.genetics.GA.other.Stagnancy;
import src.genetics.GA.crossover.ACrossover;
import src.genetics.GA.mutation.AMutation;
import src.genetics.GA.other.AGA;
import src.genetics.GA.other.Individual;
import src.genetics.GA.mutation.MutationGaussian;
import src.genetics.GA.selection.ASelection;
import src.genetics.GA.survival.ASurvival;

import java.util.ArrayList;
import java.util.Random;

public class SteadyStateGA extends AGA {
    private int replacementNumber;

    public SteadyStateGA(Random rng, int populationSize, Stagnancy stagnancy, ASelection selection,
                         ACrossover crossover, AMutation mutation, ASurvival survival,
                         ContestEvaluation evaluation, int epochs, int replacementNumber,
                         boolean printOutput) {
        super(rng, populationSize, stagnancy, selection, crossover, mutation, survival,
                evaluation, epochs, printOutput);
        this.replacementNumber = replacementNumber;
    }

    public void run() {
        if (printing) {
            System.out.println("Scores:");
        }

        try {
            for (int epoch = 0; epoch < epochs; epoch++) {
                ArrayList<Individual> newChildren = new ArrayList<>();
                while (population.getCurrentSize() + newChildren.size() < population.getMaxSize() + replacementNumber) {
                    ArrayList<Individual> parents = selection.select(population);
                    ArrayList<Individual> children = crossover.crossover(parents);
                    mutation.mutate(children);
                    newChildren.addAll(children);
                }
                population.addIndividuals(newChildren);
                population.evaluateFitness(evaluation);
                ArrayList<Individual> survivors = survival.survival(population);
                population.renewPopulation(survivors);
                population.updateStatistics();
                if (printing) {
                    System.out.println(epoch + ", " + population.getStatistics());
                }

                if(population.getStagnancyLevel() == 0) {
                    MutationGaussian.increaseMutation();
                } else if(population.getStagnancyLevel() > 20){
                    MutationGaussian.decreaseMutation();
                }
            }
        } catch (Exception e) {
            if (printing) {
                System.out.println("EndScores\n");
            }
            throw e;
        }
        if (printing) {
            System.out.println("EndScores\n");
        }
    }

    @Override
    public void printAlgorithmParameters() {
        System.out.println("Properties:");
        System.out.println("algorithmType = " + this.toString());
        System.out.println("EndProperties\n");
    }
}
