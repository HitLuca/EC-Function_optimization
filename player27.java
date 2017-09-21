import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import src.*;

import java.util.Random;
import java.util.Properties;

public class player27 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
	public player27()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
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
//		if(evaluation_ == null)
//			System.out.println("HIII POOP");
//		// Run your algorithm here
//		Population population = new Population(50, new CrossoverAverage(), new Mutation(), new SelectionBestFitness(20), evaluation_, new SurvivalBestFitness(10));
//
//
//		GA ga = new GA(population, 10);
//		ga.run();

        int evals = 0;
//         init population
//         calculate fitness
        while(evals<evaluations_limit_){
            // Select parents
            // Apply crossover / mutation operators
            double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(child);
            System.out.println(fitness);
            evals++;
            // Select survivors
        }

	}

	public static void main(String[] args)
	{
		System.out.println("Shit");
		player27 p = new player27();
		p.run();
	}
}
