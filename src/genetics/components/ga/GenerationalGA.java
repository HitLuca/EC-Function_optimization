package src.genetics.components.ga;

import org.vu.contest.ContestEvaluation;
import src.genetics.Individual;
import src.genetics.Population;
import src.genetics.components.crossover.ACrossover;
import src.genetics.components.mutation.AMutation;
import src.genetics.components.selection.ASelection;
import src.genetics.components.survival.ASurvival;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GenerationalGA extends AGA {

    private int elitism;

    public GenerationalGA(Random rng, int populationSize, int stagnancyThreshold, double epurationDegree, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs, int elitism) {
        super(rng, populationSize, stagnancyThreshold, epurationDegree, selection, crossover, mutation, survival, evaluation, epochs);
        this.elitism = elitism;
    }

    public void run() throws IOException {
        int epoch;
        System.out.println("Scores:");
//        System.out.println("epoch, mean fitness, best fitness, worst fitness");

        for (epoch = 0; epoch < epochs; epoch++) {
            Population newPopulation = new Population(rng, population.getMaxSize());
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
            System.out.println(epoch + ", " + population.getStatistics());
        }
        System.out.println("EndScores\n");
    }
}