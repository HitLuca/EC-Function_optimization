package src.genetics.components;

import org.vu.contest.ContestEvaluation;
import src.genetics.Individual;
import src.genetics.Population;

import java.util.ArrayList;
import java.util.Random;

public class Stagnancy {
    private int stagnancyLevel;
    private int wipeoutLevel;

    private int stagnancyThreshold;
    private int wipeoutThreshold;
    private double epurationDegree;
    private Random rng;
    private int populationMaxSize;

    public Stagnancy(Random rng, int stagnancyThreshold, int wipeoutThreshold, double epurationDegree, int populationMaxSize) {
        this.stagnancyThreshold = stagnancyThreshold;
        this.wipeoutThreshold = wipeoutThreshold;
        this.epurationDegree = epurationDegree;
        this.rng = rng;
        this.populationMaxSize = populationMaxSize;
    }

    public ArrayList<Individual> epuration(double epurationDegree, ArrayList<Individual> population) {
        population.sort(new Individual.FitnessComparator().reversed());
        return new ArrayList<>(population.subList(0, populationMaxSize - (int) (populationMaxSize * epurationDegree)));
    }

    public ArrayList<Individual> checkStagnancy(double oldBest, double newBest, ContestEvaluation evaluation, ArrayList<Individual> population) {
//        if (stagnancyThreshold == 0) {
//            return population;
//        }

        if (newBest > oldBest) {
            stagnancyLevel = 0;
            wipeoutLevel = 0;
        } else {
            stagnancyLevel++;
            wipeoutLevel++;
        }

        if (wipeoutLevel > wipeoutThreshold) {
            population = epuration(0.99, population);
            wipeoutLevel = 0;
            stagnancyLevel = 0;
            while (population.size() < populationMaxSize) {
                population.add(new Individual(rng, evaluation));
            }
        } else if (stagnancyLevel > stagnancyThreshold) {
            population = epuration(epurationDegree, population);
            stagnancyLevel = 0;
            while (population.size() < populationMaxSize) {
                population.add(new Individual(rng, evaluation));
            }
        }
        return population;
    }
}
