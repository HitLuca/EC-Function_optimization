package src.genetics.components.ga;

import org.vu.contest.ContestEvaluation;
import src.genetics.Individual;
import src.genetics.components.Stagnancy;
import src.genetics.components.crossover.ACrossover;
import src.genetics.components.mutation.AMutation;
import src.genetics.components.mutation.MutationGaussian;
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

    public void run() throws IOException {
        double tselection = 0;
        int nselection = 0;
        double tcrossover = 0;
        int ncrossover = 0;
        double tmutation = 0;
        int nmutation = 0;
        double tsurvival = 0;
        int nsurvival = 0;
        double tevaluation = 0;
        int nevaluation = 0;
        double t;
        if (printing) {
            System.out.println("Scores:");
        }
        double t1 = System.currentTimeMillis();

        try {
            for (int epoch = 0; epoch < epochs; epoch++) {
                ArrayList<Individual> newChildren = new ArrayList<>();
                while (population.getCurrentSize() + newChildren.size() < population.getMaxSize() + replacementNumber) {
                    t = System.currentTimeMillis();
                    ArrayList<Individual> parents = selection.select(population);
                    tselection += System.currentTimeMillis() - t;
                    nselection ++;
                    t = System.currentTimeMillis();
                    ArrayList<Individual> children = crossover.crossover(parents);
                    tcrossover += System.currentTimeMillis() - t;
                    ncrossover ++;
                    t = System.currentTimeMillis();
                    mutation.mutate(children);
                    tmutation += System.currentTimeMillis() - t;
                    nmutation ++;
                    newChildren.addAll(children);
                }
                population.addIndividuals(newChildren);
                t = System.currentTimeMillis();
                population.evaluateFitness(evaluation);
                tevaluation += System.currentTimeMillis() - t;
                nevaluation ++;
                t = System.currentTimeMillis();
                ArrayList<Individual> survivors = survival.survival(population);
                population.renewPopulation(survivors);
                tsurvival += System.currentTimeMillis() - t;
                nsurvival ++;
                population.updateStatistics();
                if (printing) {
                    System.out.println(epoch + ", " + population.getStatistics());
                }

                if(population.getStagnancyLevel() == 0) {
                    ((MutationGaussian)mutation).increaseMutation();
                } else if(population.getStagnancyLevel() > 20){
                    ((MutationGaussian)mutation).decreaseMutation();
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

        System.out.println("Selection " + tselection + " " + nselection + " " + tselection / nselection);
        System.out.println("Crossover " + tcrossover + " " + ncrossover + " " + tcrossover / ncrossover);
        System.out.println("Mutation " + tmutation + " " + nmutation + " " + tmutation / nmutation);
        System.out.println("Survival " + tsurvival + " " + nsurvival + " " + tsurvival / nsurvival);
        System.out.println("Evaluation " + tevaluation + " " + nevaluation + " " + tevaluation / nevaluation);
        System.out.println("Total chunks " + (tselection + tcrossover + tmutation + tsurvival + tevaluation));
        System.out.println("Total " + (System.currentTimeMillis() - t1));
    }
}
