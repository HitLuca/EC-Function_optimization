package src;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;

/**
 * Created by Недко on 21.9.2017 г..
 */
public class GA {

    Population population;
//    ISurvival survival;
//    ICrossover crossover;
//    ISelection selection;
//    Mutation mutation;
//    ContestEvaluation evaluation;
    int epochs;

    public GA(Population population, int epochs) {
        this.population = population;
        this.epochs = epochs;
    }

    public void run()
    {
        for(int i=0; i<epochs; i++) {
            population.survive();
            ArrayList<Individual> parents = population.select();
            population.reproduce(parents);
            population.updateStatistics();
            System.out.print(i);
        }
    }
}
