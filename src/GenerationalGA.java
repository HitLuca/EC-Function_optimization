package src;

import org.vu.contest.ContestEvaluation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class GenerationalGA extends AGA {

    private int elitism;

    public GenerationalGA(Population population, ASelection selection, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs, int elitism) {
        super(population, selection, crossover, mutation, survival, evaluation, epochs);
        this.elitism = elitism;
    }

    public void run() throws IOException {
        int epoch = 0;
        try {
            for (epoch = 0; epoch < epochs; epoch++) {
//                logger.logPopulation(epoch, population);
                Population newPopulation = new Population(population.getMaxSize());
                population.sortIndividuals();
                newPopulation.addIndividuals(population.getElites(elitism));
                while (newPopulation.getCurrentSize() < population.getMaxSize()) {
                    ArrayList<Individual> parents = selection.select(population.getIndividuals());
                    ArrayList<Individual> children = crossover.crossover(parents);
                    mutation.mutate(children);
                    newPopulation.addIndividuals(children);
                }
                newPopulation.evaluateFitness(evaluation);
                newPopulation.updateStatistics();
                population = newPopulation;
                System.out.println(epoch + " " + population.getStatistics());
            }
        } catch (NullPointerException e) {
//            logger.logPopulation(epoch, population);
        }
    }
}
