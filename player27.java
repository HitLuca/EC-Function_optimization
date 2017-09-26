import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import src.*;
import src.components.*;

import java.util.Random;
import java.util.Properties;
import java.util.logging.Logger;

public class player27 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
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
    
	public void run()
	{
		printProperties(evaluation_);
		// Run your algorithm here
		int populationSize = 100;
		int epochs = -1;
		int parentsNumber = 2;
		int elitism = 3;
		double mutationProbability = 0.7;

		Population population = new Population(populationSize, evaluation_);

		GA ga = new GA(population,
				new SelectionFitnessProportional(parentsNumber),
				elitism,
				new CrossoverAverageWeighted(),
				new MutationGaussian(0.1, mutationProbability),
				new SurvivalBestFitness(populationSize),
				evaluation_, epochs);

		ga.run();
	}

	public void printProperties(ContestEvaluation evaluation)
	{
		Properties p = evaluation.getProperties();
		int maxEvaluations = Integer.parseInt(p.getProperty("Evaluations"));
		boolean isMultimodal = Boolean.parseBoolean(p.getProperty("Multimodal"));
		boolean hasStructure = Boolean.parseBoolean(p.getProperty("Regular"));
		boolean isSeparable = Boolean.parseBoolean(p.getProperty("Separable"));

		System.out.println("Evaluations: " + maxEvaluations + " Multimodal: " + isMultimodal + " Regular: " + hasStructure + " Separable: " + isSeparable);
	}

	public static void main(String[] args)
	{
		player27 p = new player27();
		p.run();
	}
}
