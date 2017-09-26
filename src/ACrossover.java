package src;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Collections;


public abstract class ACrossover {

    //public abstract ArrayList<Individual> crossover(int parentsNumber, ArrayList<Individual> parentsPool, AMutation mutation, ContestEvaluation evaluation);
    //public abstract Individual crossoverPair(ArrayList<Individual> parentsPair, AMutation mutation, ContestEvaluation evaluation);

    public ArrayList<Individual> crossover(int parentsNumber, ArrayList<Individual> parentsPool, AMutation mutation, ContestEvaluation evaluation) {
        Collections.shuffle(parentsPool);

        Individual[] pair = new Individual[parentsNumber];

        ArrayList<Individual> children = new ArrayList<>();

        for (int i = 0; i <= parentsPool.size() - parentsNumber; i += parentsNumber) {
            for (int j = 0; j < parentsNumber; j++) {
                pair[j] = parentsPool.get(i + j);
            }

            children.add(crossoverPair(pair, mutation, evaluation));
        }

        return children;
    }

    public abstract Individual crossoverPair(Individual[] parentsPair, AMutation mutation, ContestEvaluation evaluation);
}
