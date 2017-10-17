package src.genetics.GA;

import org.vu.contest.ContestEvaluation;
import src.genetics.GA.crossover.ACrossover;
import src.genetics.GA.mutation.AMutation;
import src.genetics.GA.mutation.MutationGaussian;
import src.genetics.GA.other.AGA;
import src.genetics.GA.other.Individual;
import src.genetics.GA.other.Population;
import src.genetics.GA.other.Stagnancy;
import src.genetics.GA.selection.ASelection;
import src.genetics.GA.survival.ASurvival;

import java.util.ArrayList;
import java.util.Random;

public class GenerationalGA extends AGA {

    private int elitism;
    private Stagnancy stagnancy;

    public GenerationalGA(Random rng, int populationSize, Stagnancy stagnancy, ASelection selection,
                          ACrossover crossover, AMutation mutation, ASurvival survival,
                          ContestEvaluation evaluation, int epochs, int elitism, boolean printOutput) {
        super(rng, populationSize, stagnancy, selection, crossover, mutation, survival,
                evaluation, epochs, printOutput);
        this.elitism = elitism;
        this.stagnancy = stagnancy;
    }

    public void run() {
        int epoch;
        if (printing) {
            System.out.println("Scores:");
        }

        try {
            for (epoch = 0; epoch < epochs; epoch++) {
                Population newPopulation = new Population(rng, population.getMaxSize(), stagnancy);
                population.sortIndividualsReversed();
                newPopulation.addIndividuals(population.getElites(elitism));
                while (newPopulation.getCurrentSize() < population.getMaxSize()) {
                    ArrayList<Individual> parents = selection.select(population);
                    ArrayList<Individual> children = crossover.crossover(parents);
                    mutation.mutate(children);
                    newPopulation.addIndividuals(children);
                }
                population.renewPopulation(newPopulation.getIndividuals());
                population.evaluateFitness(evaluation);
                population.updateStatistics();

                if (printing) {
                    System.out.println(epoch + ", " + population.getStatistics());
                }

                if (population.getStagnancyLevel() == 0) {
                    MutationGaussian.increaseMutation();
                } else if (population.getStagnancyLevel() > 10) {
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
