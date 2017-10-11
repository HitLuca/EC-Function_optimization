import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;
import src.genetics.CMAEvolutionaryStrategy;
import src.genetics.DifferentialEvolution;
import src.genetics.PSO;
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
    //PSO
    private int swarmSize;
    private double w;
    private double phi1;
    private double phi2;
    //Differential
    private int difPopulationSize;
    private double f;
    private double cr;
    private char base;
    private int diffN;

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
	private PSO pso;
	private DifferentialEvolution de;

    private boolean isMultimodal;
    private boolean hasStructure;
    private boolean isSeparable;

    private int function;

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

        setFunction();
    }

    private void setFunction() {
        if(!isMultimodal) {
            function = 0;
        } else if (hasStructure) {
            function = 1;
        } else {
            function = 2;
        }
    }
    public void run() {
        printProperties(evaluation_);

        rng.setSeed(System.currentTimeMillis());
//		rng.setSeed(Long.MAX_VALUE);

        loadProperties();
        printAlgorithmProperties();

        try {
            switch (algorithmType){
                case "CMA-ES": {
                    es.run(evaluation_, epochs);
                    break;
                }
                case "PSO": {
                    pso.run(evaluation_);
                    break;
                }
                case "DE": {
                    de.run(evaluation_);
                    break;
                }
                default: {
                    ga.run();
                    break;
                }
            }
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
//		algorithmType = "Generational";
//        algorithmType = "SteadyState";
//      algorithmType = "CMA-ES";
//        algorithmType = "PSO";
        algorithmType = "DE";

        fullGenomeSize = 0;
        switch(function) {
            case 0: {
                populationSize = 250;

                stagnancyThreshold = 100;
                wipeoutThreshold = 250;
                epurationDegree = 0.7;

                epochs = -1;
                elitism = 3;
                replacementNumber = 50;


                mutationSigma = 0.1;
                mutationProbability = 0.9;

                parentsNumber = 2;
                selectionPressure = 1.75;

                swarmSize = 50;
                w = 0.86;
                phi1 = 0.07;
                phi2 = 0.07;

                difPopulationSize = 50;
                f = 0.15;
                cr = 0.2;
                base = 'b';
                diffN = 1;

                break;
            }
            case 1: {
                populationSize = 1000;

                stagnancyThreshold = 100;
                wipeoutThreshold = 250;
                epurationDegree = 0.7;

                epochs = -1;
                elitism = 3;
                replacementNumber = 100;


                mutationSigma = 0.1;
                mutationProbability = 0.9;

                parentsNumber = 2;
                selectionPressure = 1.75;

                swarmSize = 80;
                w = 0.8;
                phi1 = 0.2;
                phi2 = 0.7;

                difPopulationSize = 50;
                f = 0.5;
                cr = 0.4;
                base = 'b';
                diffN = 1;

                break;
            }
            case 2: {
                populationSize = 2500;
                fullGenomeSize = 0;

                stagnancyThreshold = 100;
                wipeoutThreshold = 250;
                epurationDegree = 0.7;

                epochs = -1;
                elitism = 3;
                replacementNumber = 150;


                mutationSigma = 0.01;
                mutationProbability = 0.9;

                parentsNumber = 2;
                selectionPressure = 1.75;

                swarmSize = 500;
                w = 0.5;
                phi1 = 0.05;
                phi2 = 0.1;

                difPopulationSize = 18;
                f = 0.6714;
                cr = 0.5026;
                base = 'b';
                diffN = 1;
                // from http://www.hvass-labs.org/people/magnus/publications/pedersen10good-de.pdf

                break;
            }
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
                        fullGenomeSize,
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
                        fullGenomeSize,
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
            case "PSO": {
                pso = new PSO(swarmSize, epochs, w, phi1, phi2, rng);
                break;
            }
            case "DE": {
                de = new DifferentialEvolution(epochs, difPopulationSize, f, cr, base, diffN, rng);
                break;
            }
            default: {
                System.out.println("Unrecognized algorithm type");
                break;
            }
        }
    }

    private void printAlgorithmProperties() {
        System.out.println("Properties:");
        System.out.println("algorithmType=" + algorithmType);
        System.out.println("populationSize=" + populationSize);
        System.out.println("stagnancyThreshold=" + stagnancyThreshold);
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
