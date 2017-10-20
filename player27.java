import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;
import src.genetics.AEA;
import src.genetics.CMAEvolutionaryStrategy;
import src.genetics.DifferentialEvolution;
import src.genetics.GA.crossover.*;
import src.genetics.GA.other.Stagnancy;
import src.genetics.GA.GenerationalGA;
import src.genetics.GA.SteadyStateGA;
import src.genetics.GA.mutation.AMutation;
import src.genetics.GA.mutation.MutationGaussian;
import src.genetics.GA.selection.ASelection;
import src.genetics.GA.selection.SelectionLinearRanking;
import src.genetics.GA.survival.ASurvival;
import src.genetics.GA.survival.SurvivalBestFitness;
import src.genetics.PSO;

import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.*;

public class player27 implements ContestSubmission {
    public Random rng;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;

    private int stagnancyThreshold;
    private int wipeoutThreshold;
    private double epurationDegree;
    private int epochs = -1;

    private double mutationProbability = 1;
    private int parentsNumber = 2;

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

        runAlgorithm();
    }

    private void runAlgorithm() {
        Runnable fooRunner = new Runnable() {
            public void run() {
                try {
                    ea.run();
                } catch (Exception e) {}
            }
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            executor.submit(fooRunner).get(10, TimeUnit.SECONDS); // Timeout of 10 minutes.
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        executor.shutdown();
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
        String algorithmType = System.getProperty("algorithm");
        algorithmType = algorithmType == null ? "CMA-ES" : algorithmType;

        switch (algorithmType) {
            case "GGA": {
                setupGGA();
                break;
            }
            case "SSGA": {
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
        int mu = 10;
        int lambda = 30;
        String property = System.getProperty("lambda");
        lambda = property == null ? lambda : Integer.parseInt(property);

        property = System.getProperty("mu");
        mu = property == null ? mu : Integer.parseInt(property);

        ea = new CMAEvolutionaryStrategy(mu, lambda, evaluation_, epochs, printOutput);
    }

    private void setupSSGA() {
        int populationSize = evaluations_limit_ <= 100000 ? evaluations_limit_ / 100 : evaluations_limit_ / 1000;
        double sigma = 0.05;
        int replacementNumber = populationSize / 10;
        double selectionPressure = 1.75;

        String property = System.getProperty("replacementNumber");
        replacementNumber = property == null ? replacementNumber : (int)(1.0 * populationSize / Integer.parseInt(property));

        property = System.getProperty("sigma");
        sigma = property == null ? sigma : Double.parseDouble(property);

        stagnancyThreshold = isMultimodal ? evaluations_limit_ / 4000 : 0;
        wipeoutThreshold = evaluations_limit_ / 2000;
        epurationDegree = 0.7;

        Stagnancy stagnancy = new Stagnancy(rng, stagnancyThreshold, wipeoutThreshold, epurationDegree, populationSize);
        ACrossover crossover = getCrossoverProperty();
        AMutation mutation = new MutationGaussian(rng, sigma, mutationProbability);
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
        int populationSize = 4;
        int elitism = 1;
        double sigma = 0.05;
        double selectionPressure = 2;

        String property = System.getProperty("elitism");
        elitism = property == null ? elitism : Integer.parseInt(property);

        property = System.getProperty("selectionPressure");
        selectionPressure = property == null ? selectionPressure : Double.parseDouble(property);

        property = System.getProperty("sigma");
        sigma = property == null ? sigma : Double.parseDouble(property);

        stagnancyThreshold = 0;
        wipeoutThreshold = 0;
        epurationDegree = 0;

        Stagnancy stagnancy = new Stagnancy(rng, stagnancyThreshold, wipeoutThreshold, epurationDegree, populationSize);
        ACrossover crossover = getCrossoverProperty();
        AMutation mutation = new MutationGaussian(rng, sigma, mutationProbability);
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

        String property = System.getProperty("swarmSize");
        swarmSize = property == null ? swarmSize : Integer.parseInt(property);

        property = System.getProperty("w");
        w = property == null ? w : Double.parseDouble(property);

        property = System.getProperty("phi1");
        phi1 = property == null ? phi1 : Double.parseDouble(property);

        property = System.getProperty("phi2");
        phi2 = property == null ? phi2 : Double.parseDouble(property);

        ea = new PSO(swarmSize, epochs, w, phi1, phi2, rng, evaluation_, printOutput);
    }

    private void setupDE() {
        int popSize = 50;
        double f = 0.5;
        double cr = 0.4;
        char base = 'b';
        int diffN = 1;

        String property = System.getProperty("popSize");
        popSize = property == null ? popSize : Integer.parseInt(property);

        property = System.getProperty("f");
        f = property == null ? f : Double.parseDouble(property);

        property = System.getProperty("cr");
        cr = property == null ? cr : Double.parseDouble(property);

        property = System.getProperty("base");
        base = property == null ? base : property.charAt(0);

        property = System.getProperty("diffN");
        diffN = property == null ? diffN : Integer.parseInt(property);

        ea = new DifferentialEvolution(epochs, popSize, f, cr, base, diffN, rng, evaluation_, printOutput);
    }

    private ACrossover getCrossoverProperty() {
        String crossoverType = System.getProperty("crossover");
        if(crossoverType == null) {
            return new CrossoverAverageWeighted(rng);
        } else {
            switch (System.getProperty("crossover")) {
                case "CrossoverAverage": {
                    return new CrossoverAverage(rng);
                }
                case "CrossoverAverageWeighted": {
                    return new CrossoverAverageWeighted(rng);
                }
                case "CrossoverCoinFlip": {
                    return new CrossoverCoinFlip(rng);
                }
                case "CrossoverCoinFlipWeighted": {
                    return new CrossoverCoinFlipWeighted(rng);
                }
                case "CrossoverNPoints": {
                    return new CrossoverNPoints(rng, 2);
                }
                default: {
                    System.out.println("unrecognized crossover, using CrossoverAverageWeighted");
                    return new CrossoverAverageWeighted(rng);
                }
            }
        }
    }
}
