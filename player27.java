import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;
import src.genetics.CMAEvolutionaryStrategy;
import src.genetics.components.Stagnancy;
import src.genetics.components.crossover.*;
import src.genetics.components.ga.AGA;
import src.genetics.components.ga.GenerationalGA;
import src.genetics.components.ga.SteadyStateGA;
import src.genetics.components.mutation.AMutation;
import src.genetics.components.mutation.MutationGaussian;
import src.genetics.components.selection.ASelection;
import src.genetics.components.selection.SelectionFitnessProportional;
import src.genetics.components.selection.SelectionLinearRanking;
import src.genetics.components.selection.SelectionTournament;
import src.genetics.components.survival.ASurvival;
import src.genetics.components.survival.SurvivalBestFitness;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Properties;
import java.util.Random;

public class player27 implements ContestSubmission {
    public Random rng;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;

    private String algorithmType;

    private int populationSize;
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

	private boolean printOutput;

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
        // Set evaluation problem used in the run
        evaluation_ = evaluation;

        // Get evaluation properties
        Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
        // Property keys depend on specific evaluation
        // E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
    }

    public void run() {

        rng.setSeed(System.currentTimeMillis());
//		rng.setSeed(Long.MAX_VALUE);

        loadProperties();

        if (printOutput) {
            printProperties(evaluation_);
            printAlgorithmProperties();
        }

        try {
            ga.run();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: fix NullPointerException
        }
    }

    public void printProperties(ContestEvaluation evaluation) {
        Properties p = evaluation.getProperties();
        int maxEvaluations = Integer.parseInt(p.getProperty("Evaluations"));
        boolean isMultimodal = Boolean.parseBoolean(p.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(p.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(p.getProperty("Separable"));

        System.out.println("Evaluations: " + maxEvaluations + " Multimodal: " + isMultimodal + " Regular: " + hasStructure + " Separable: " + isSeparable);
        System.out.println();
    }

    private void loadProperties() {
        printOutput = true;
        epochs = -1;
        mutationSigma = 0.05;
        mutationProbability = 1;
        parentsNumber = 2;

        if (isMultimodal) {
            algorithmType = "SteadyState";

            if (evaluations_limit_ <= 100000) {
                populationSize = evaluations_limit_ / 100;
            } else {
                populationSize = evaluations_limit_ / 1000;
            }

            replacementNumber = populationSize / 10;
            selectionPressure = 1.75;

            stagnancyThreshold = evaluations_limit_ / 4000;
            wipeoutThreshold = evaluations_limit_ / 2000;
            epurationDegree = 0.7;
        } else {
            algorithmType = "Generational";
            populationSize = 4;
            elitism = 1;

            selectionPressure = 2;
            stagnancyThreshold = 0;
        }

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
                es = new CMAEvolutionaryStrategy(1, 10);
                break;
            }
            default: {
                System.out.println("Unrecognized algorithm type");
                break;
            }
        }
        ga.setPrinting(printOutput);

    }

    private void printAlgorithmProperties() {
        System.out.println("Properties:");
        System.out.println("algorithmType=" + ga);
        System.out.println("populationSize=" + populationSize);
        System.out.println("stagnancyThreshold=" + stagnancyThreshold);
        System.out.println("wipeoutThreshold=" + wipeoutThreshold);
        System.out.println("epurationDegree=" + epurationDegree);
        System.out.println("epochs=" + epochs);
        System.out.println("elitism=" + elitism);
        System.out.println("replacementNumber=" + replacementNumber);
        System.out.println("crossover=" + crossover);
        System.out.println("mutationSigma=" + mutationSigma);
        System.out.println("mutationProbability=" + mutationProbability);
        System.out.println("mutation=" + mutation);
        System.out.println("parentsNumber=" + parentsNumber);
        System.out.println("selectionPressure=" + selectionPressure);
        System.out.println("selection=" + selection);
        System.out.println("survival=" + survival);
        System.out.println("EndProperties\n");
    }
}
