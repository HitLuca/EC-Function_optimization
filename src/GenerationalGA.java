package src;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Properties;

public class GenerationalGA {

    private Population population;
    private ASurvival survival;
    private ACrossover crossover;
    private ASelection selection;
    private AMutation mutation;
    private ContestEvaluation evaluation;
    private int epochs;
    private int elitism;

    public GenerationalGA(Population population, ASelection selection, int elitism, ACrossover crossover, AMutation mutation, ASurvival survival, ContestEvaluation evaluation, int epochs) {
        this.population = population;
        this.survival = survival;
        this.crossover = crossover;
        this.selection = selection;
        this.mutation = mutation;
        this.evaluation = evaluation;
        this.elitism = elitism;

        Properties props = evaluation.getProperties();
        int evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));

        if(epochs == -1) {
            this.epochs = Integer.MAX_VALUE;
        } else {
            this.epochs = epochs; //Math.min(epochs, (evaluations_limit_-population.getMaxSize())/(int) Math.ceil(selection.getSize()/pairSize));
        }
    }

    public void run()
    {
        for(int i=0; i<epochs; i++) {
            Population newPopulation = new Population(population.getMaxSize());
            population.sortIndividuals();
            newPopulation.addIndividuals(population.getElites(elitism));
            while (newPopulation.getCurrentSize() < population.getMaxSize()) {
                ArrayList<Individual> parents = selection.select(population.getIndividuals());
                ArrayList<Individual> children = crossover.crossover(parents);
                mutation.mutate(children);
                newPopulation.addIndividuals(children);
            }
            mutation.mutate(newPopulation.getIndividuals());
            newPopulation.evaluateFitness(evaluation);
            population = newPopulation;
            population.updateStatistics();
            System.out.println(population.getStatistics());
        }
    }
}
