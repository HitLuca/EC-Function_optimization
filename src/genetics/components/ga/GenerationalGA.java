package src.genetics.components.ga;

import org.vu.contest.ContestEvaluation;
import src.genetics.Individual;
import src.genetics.Population;
import src.genetics.components.Stagnancy;
import src.genetics.components.crossover.ACrossover;
import src.genetics.components.mutation.AMutation;
import src.genetics.components.selection.ASelection;
import src.genetics.components.survival.ASurvival;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GenerationalGA extends AGA {

    private int elitism;
    private Stagnancy stagnancy;

    public GenerationalGA(Random rng, int populationSize, Stagnancy stagnancy, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs, int elitism) {
        super(rng, populationSize, stagnancy, selection, crossover, mutation, survival, evaluation, epochs);
        this.elitism = elitism;
        this.stagnancy = stagnancy;
    }

    public void run() throws IOException {
        int epoch;
        if (printing) {
            System.out.println("Scores:");
        }

        try {
            for (epoch = 0; epoch < epochs; epoch++) {
                Population newPopulation = new Population(rng, population.getMaxSize(), stagnancy);
                population.sortIndividuals();
                newPopulation.addIndividuals(population.getElites(elitism));
                while (newPopulation.getCurrentSize() < population.getMaxSize()) {
                    ArrayList<Individual> parents = selection.select(population.getIndividuals());
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

                if(population.gotFitnessImprovement()) {
                    mutation.increaseMutation();
                } else {
                    mutation.decreaseMutation();
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
}
