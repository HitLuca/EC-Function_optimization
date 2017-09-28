import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import src.genetics.components.crossover.ACrossover;
import src.genetics.components.crossover.CrossoverAverage;
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

public class player27 implements ContestSubmission
{
	public Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

	private String algorithmType;

	private int populationSize;
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

	private AGA ga;

	public player27()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed) {
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
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
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }
    
	public void run() {
		printProperties(evaluation_);

		// Run your algorithm here
		loadProperties();
		printAlgorithmProperties();

		try {ga.run();} catch (Exception e) {
			System.err.println(e.getMessage());}
	}

	public void printProperties(ContestEvaluation evaluation)
	{
		Properties p = evaluation.getProperties();
		int maxEvaluations = Integer.parseInt(p.getProperty("Evaluations"));
		boolean isMultimodal = Boolean.parseBoolean(p.getProperty("Multimodal"));
		boolean hasStructure = Boolean.parseBoolean(p.getProperty("Regular"));
		boolean isSeparable = Boolean.parseBoolean(p.getProperty("Separable"));

		System.out.println("Evaluations: " + maxEvaluations + " Multimodal: " + isMultimodal + " Regular: " + hasStructure + " Separable: " + isSeparable);
		System.out.println();
	}

	public static void main(String[] args)
	{
		player27 p = new player27();
		p.run();
	}

	private void loadProperties() {
//		algorithmType = "Generational";
		algorithmType = "SteadyState";

		populationSize = 150;
		epochs = 1;
		elitism = 1;
		replacementNumber = 10;

		crossover = new CrossoverAverage();

		mutationSigma = 0.1;
		mutationProbability = 1;
		mutation = new MutationGaussian(mutationSigma, mutationProbability);

		parentsNumber = 2;
		selectionPressure = 2;
		selection = new SelectionLinearRanking(parentsNumber, selectionPressure);

		survival = new SurvivalBestFitness(populationSize);

		switch(algorithmType) {
			case "Generational": {
				ga = new GenerationalGA(populationSize,
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
				ga = new SteadyStateGA(populationSize,
						selection,
						crossover,
						mutation,
						survival,
						evaluation_,
						epochs,
						replacementNumber);
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
		System.out.println("algorithmType=" + ga);
		System.out.println("populationSize=" + populationSize);
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
