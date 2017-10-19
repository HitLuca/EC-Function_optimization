import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;
import src.genetics.AEA;
import src.genetics.CMAEvolutionaryStrategy;
import src.genetics.DifferentialEvolution;
import src.genetics.GA.other.Stagnancy;
import src.genetics.GA.crossover.ACrossover;
import src.genetics.GA.crossover.CrossoverAverageWeighted;
import src.genetics.GA.GenerationalGA;
import src.genetics.GA.SteadyStateGA;
import src.genetics.GA.mutation.AMutation;
import src.genetics.GA.mutation.MutationGaussian;
import src.genetics.GA.selection.ASelection;
import src.genetics.GA.selection.SelectionLinearRanking;
import src.genetics.GA.survival.ASurvival;
import src.genetics.GA.survival.SurvivalBestFitness;
import src.genetics.PSO;

import java.util.Properties;
import java.util.Random;

public class player27 implements ContestSubmission {
    public Random rng;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;

    private int populationSize;
    private int stagnancyThreshold;
    private int wipeoutThreshold;
    private double epurationDegree;
    private int epochs;

    private double mutationSigma = 0.05;
    private double mutationProbability = 1;
    private int parentsNumber = 2;
    private double selectionPressure;

    private AEA ea;

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
        evaluation_ = evaluation;

        Properties props = evaluation.getProperties();
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
    }

    public void run() {
        printOutput = false;

        rng.setSeed(System.currentTimeMillis());

        setupAlgorithm();

        if (printOutput) {
            printProperties(evaluation_);
            ea.printAlgorithmParameters();
        }

        ea.run();
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

    private void setupAlgorithm() {
        epochs = -1;

        String algorithmType = "CMA-ES";
//        String algorithmType = "Generational";
//        String algorithmType = "SteadyState";
//        String algorithmType = "PSO";
//        String algorithmType = "DE";

        switch (algorithmType) {
            case "Generational": {
                setupGGA();
                break;
            }
            case "SteadyState": {
                setupSSGA();
                break;
            }
            case "CMA-ES": {
                setupCMAES();
                break;
            }
            case "PSO": {
                setupPSO();
                break;
            }
            case "DE": {
                setupDE();
                break;
            }
            default: {
                System.out.println("Unrecognized algorithm type");
                break;
            }
        }
    }

    private void setupCMAES() {
        ea = new CMAEvolutionaryStrategy(10, 30, evaluation_, epochs, printOutput);
    }

    private void setupSSGA() {
        if (evaluations_limit_ <= 100000) {
            populationSize = evaluations_limit_ / 100;
        } else {
            populationSize = evaluations_limit_ / 1000;
        }

        int replacementNumber = populationSize / 10;
        selectionPressure = 1.75;

        stagnancyThreshold = evaluations_limit_ / 4000;
        wipeoutThreshold = evaluations_limit_ / 2000;
        epurationDegree = 0.7;

        Stagnancy stagnancy = new Stagnancy(rng, stagnancyThreshold, wipeoutThreshold, epurationDegree, populationSize);
        ACrossover crossover = new CrossoverAverageWeighted(rng);
        AMutation mutation = new MutationGaussian(rng, mutationSigma, mutationProbability);
        ASelection selection = new SelectionLinearRanking(rng, parentsNumber, selectionPressure);
        ASurvival survival = new SurvivalBestFitness(rng);

        ea = new SteadyStateGA(rng,
                populationSize,
                stagnancy,
                selection,
                crossover,
                mutation,
                survival,
                evaluation_,
                epochs,
                replacementNumber,
                printOutput);
    }

    private void setupGGA() {
        populationSize = 4;
        int elitism = 1;

        selectionPressure = 2;
        stagnancyThreshold = 0;
        wipeoutThreshold = 0;
        epurationDegree = 0;

        Stagnancy stagnancy = new Stagnancy(rng, stagnancyThreshold, wipeoutThreshold, epurationDegree, populationSize);
        ACrossover crossover = new CrossoverAverageWeighted(rng);
        AMutation mutation = new MutationGaussian(rng, mutationSigma, mutationProbability);
        ASelection selection = new SelectionLinearRanking(rng, parentsNumber, selectionPressure);
        ASurvival survival = new SurvivalBestFitness(rng);

        ea = new GenerationalGA(rng,
                populationSize,
                stagnancy,
                selection,
                crossover,
                mutation,
                survival,
                evaluation_,
                epochs,
                elitism,
                printOutput);
    }

    private void setupPSO() {
        int swarmSize = 50;
        double w = 0.86;
        double phi1 = 0.07;
        double phi2 = 0.07;

        ea = new PSO(swarmSize, epochs, w, phi1, phi2, rng, evaluation_);
    }

    private void setupDE() {
        int difPopulationSize = 50;
        double f = 0.5;
        double cr = 0.4;
        char base = 'b';
        int diffN = 1;

        ea = new DifferentialEvolution(epochs, difPopulationSize, f, cr, base, diffN, rng, evaluation_);
    }
}
