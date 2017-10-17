import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;
import src.genetics.CMAEvolutionaryStrategy;
import src.genetics.components.Stagnancy;
import src.genetics.components.crossover.ACrossover;
import src.genetics.components.crossover.CrossoverAverageWeighted;
import src.genetics.components.ga.AGA;
import src.genetics.components.ga.GenerationalGA;
import src.genetics.components.ga.SteadyStateGA;
import src.genetics.components.mutation.AMutation;
import src.genetics.components.mutation.MutationGaussian;
import src.genetics.components.selection.ASelection;
import src.genetics.components.selection.SelectionLinearRanking;
import src.genetics.components.survival.ASurvival;
import src.genetics.components.survival.SurvivalBestFitness;

import java.util.Random;
import java.util.Properties;

public class player27 implements ContestSubmission {
    public Random rng;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;

    private String algorithmType;

    private int populationSize;
    private int fullGenomeSize;
    private int stagnancyThreshold;
    private int wipeoutThreshold;
    private double epurationDegree;
    private int epochs;
    private int elitism;
    private int replacementNumber;
    private ACrossover crossover;
    private double mutationSigma;
    private double mutationProbability;
    private AMutation mutation;
    private int parentsNumber;
    private double selectionPressure;
    private ASelection selection;
    private ASurvival survival;
    private Stagnancy stagnancy;

	private AGA ga;
	private CMAEvolutionaryStrategy es;

    private boolean isMultimodal;
    private boolean hasStructure;
    private boolean isSeparable;

    public player27() {
        rng = new Random();
    }

    public static void main(String[] args) {
        player27 p = new player27();
        p.run();
    }

    public void setSeed(long seed) {
        // Set seed of algortihms random process
        rng.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation) {
        evaluation_ = evaluation;

        Properties props = evaluation.getProperties();
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
    }

    public void run() {
        printProperties(evaluation_);

        rng.setSeed(System.currentTimeMillis());

        loadProperties();

        if (algorithmType.equals("CMA-ES")) {
            es.run(evaluation_, epochs);
        } else {
            ga.run();
        }
    }

    private void printProperties(ContestEvaluation evaluation) {
        Properties p = evaluation.getProperties();
        int maxEvaluations = Integer.parseInt(p.getProperty("Evaluations"));
        boolean isMultimodal = Boolean.parseBoolean(p.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(p.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(p.getProperty("Separable"));

        System.out.println("Evaluations: " + maxEvaluations + " Multimodal: " + isMultimodal + " Regular: " + hasStructure + " Separable: " + isSeparable);
        System.out.println();
    }

    private void loadProperties() {
        epochs = -1;
        mutationSigma = 0.05;
        mutationProbability = 1;
        parentsNumber = 2;

        algorithmType = "CMA-ES";

//        if (isMultimodal) {
//            algorithmType = "SteadyState";
//
//            if (evaluations_limit_ <= 100000) {
//                populationSize = evaluations_limit_ / 100;
//            } else {
//                populationSize = evaluations_limit_ / 1000;
//            }
//
//            replacementNumber = populationSize / 10;
//            selectionPressure = 1.75;
//
//            stagnancyThreshold = evaluations_limit_ / 4000;
//            wipeoutThreshold = evaluations_limit_ / 2000;
//            epurationDegree = 0.7;
//        } else {
//            algorithmType = "Generational";
//            populationSize = 4;
//            elitism = 1;
//
//            selectionPressure = 2;
//            stagnancyThreshold = 0;
//        }

        stagnancy = new Stagnancy(rng, stagnancyThreshold, wipeoutThreshold, epurationDegree, populationSize);
        crossover = new CrossoverAverageWeighted(rng);
        mutation = new MutationGaussian(rng, mutationSigma, mutationProbability);
        selection = new SelectionLinearRanking(rng, parentsNumber, selectionPressure);
        survival = new SurvivalBestFitness(rng);

        switch (algorithmType) {
            case "Generational": {
                ga = new GenerationalGA(rng,
                        populationSize,
                        stagnancy,
                        selection,
                        crossover,
                        mutation,
                        survival,
                        evaluation_,
                        epochs,
                        elitism);
                break;
            }
            case "SteadyState": {
                ga = new SteadyStateGA(rng,
                        populationSize,
                        stagnancy,
                        selection,
                        crossover,
                        mutation,
                        survival,
                        evaluation_,
                        epochs,
                        replacementNumber);
                break;
            }
            case "CMA-ES": {
                es = new CMAEvolutionaryStrategy(5, 15, false);
                break;
            }
            default: {
                System.out.println("Unrecognized algorithm type");
                break;
            }
        }
    }
}
