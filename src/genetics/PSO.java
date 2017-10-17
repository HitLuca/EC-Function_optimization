package src.genetics;

import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Random;

public class PSO extends AEA {
    private int epochs;
    private int popSize;

    private double w;
    private double phi1;
    private double phi2;

    private Swarm population;

    private Random rng;
    private ContestEvaluation evaluation;

    public PSO(int popSize, int epochs, double w, double phi1, double phi2, Random rng,
               ContestEvaluation evaluation) {
        this.popSize = popSize;

        if (epochs == -1) {
            this.epochs = Integer.MAX_VALUE;
        } else {
            this.epochs = epochs;
        }

        this.w = w;
        this.phi1 = phi1;
        this.phi2 = phi2;
        this.rng = rng;

        this.evaluation = evaluation;
    }

    @Override
    public void run() {
        population = new Swarm(popSize, evaluation, rng);
        System.out.println("Scores:");
        try {
            for (int epoch = 0; epoch < epochs; epoch++) {
                population.updateSwarm(w, phi1, phi2);
                System.out.println(epoch + ", " + population.getStatistics());
            }
        } catch (Exception e) {
            System.out.println("EndScores\n");
            throw e;
        }
        System.out.println("EndScores\n");
    }

    @Override
    public void printAlgorithmParameters() {
        System.out.println("Properties:");
        System.out.println("algorithmType = " + this.toString());
        System.out.println("EndProperties\n");
    }
}

class Swarm {
    public static final int GENOME_SIZE = 10;
    private ContestEvaluation evaluation;
    private Random rng;

    private ArrayList<Particle> population;
    private int popSize;

    private double historicalBestFitness = 0.0;
    private double historicalBest[] = new double[GENOME_SIZE];

    private double meanFitness = -1;
    private double worstFitness = -1;
    private double bestFitness = -1;

    public Swarm(int popSize, ContestEvaluation evaluation, Random rng) {
        this.rng = rng;
        this.evaluation = evaluation;

        this.popSize = popSize;
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            population.add(new Particle(evaluation, rng));
        }
    }

    public void updateSwarm(double w, double phi1, double phi2) {
        double fitness;

        bestFitness = 0.0;
        meanFitness = 0.0;
        worstFitness = 10.0;

        for (Particle p : population) {
            p.updateVelocity(historicalBest, w, phi1, phi2, rng);
            p.move(evaluation);

            fitness = p.getFitness();
            meanFitness += fitness;
            bestFitness = Math.max(bestFitness, fitness);
            worstFitness = Math.min(worstFitness, fitness);

            if (historicalBestFitness < fitness) {
                historicalBestFitness = fitness;
                historicalBest = p.getGenome();
            }
        }

        meanFitness /= popSize;

        if ((bestFitness - worstFitness) / bestFitness < 0.01) {
            epuration();
        }
    }

    public String getStatistics() {
        return meanFitness
                + ", " + bestFitness
                + ", " + worstFitness
                + ", " + historicalBestFitness;
    }

    public void epuration() {
        population = new ArrayList<>();
        population.add(new Particle(historicalBest, historicalBestFitness));

        while (population.size() < popSize) {
            population.add(new Particle(evaluation, rng));
        }
    }
}

class Particle {
    private double genome[];
    private double velocity[];

    private double fitness = 0;

    private double historicalBestFitness = -1.0;
    private double historicalBest[];

    private int max = 5;
    private int min = -5;

    public Particle(ContestEvaluation evaluation, Random rng) {
        genome = new double[Swarm.GENOME_SIZE];
        velocity = new double[Swarm.GENOME_SIZE];
        for (int i = 0; i < Swarm.GENOME_SIZE; i++) {
            genome[i] = rng.nextDouble() * (max - min) + min;
            velocity[i] = 0.0;
        }
        evaluate(evaluation);
    }

    public Particle(double[] genome, double fitness) {
        this.genome = genome;
        this.fitness = fitness;

        velocity = new double[Swarm.GENOME_SIZE];
        for (int i = 0; i < Swarm.GENOME_SIZE; i++) {
            velocity[i] = 0.0;
        }
        checkBestResult();
    }

    public double[] getGenome() {
        return genome.clone();
    }

    public double getFitness() {
        return fitness;
    }

    public void evaluate(ContestEvaluation evaluation) {
        this.fitness = (double) evaluation.evaluate(genome);
        checkBestResult();
    }

    public void checkBestResult() {
        if (fitness > historicalBestFitness) {
            historicalBestFitness = fitness;
            historicalBest = genome.clone();
        }
    }

    public void move(ContestEvaluation evaluation) {
        for (int i = 0; i < Swarm.GENOME_SIZE; i++) {
            genome[i] = clip(genome[i] + velocity[i]);
        }
        evaluate(evaluation);
    }

    public void updateVelocity(double[] globalBest, double w, double phi1, double phi2, Random rng) {
        for (int i = 0; i < Swarm.GENOME_SIZE; i++) {
            velocity[i] = w * velocity[i] + phi1 * rng.nextDouble() * (historicalBest[i] - genome[i]) + phi2 * rng.nextDouble() * (globalBest[i] - genome[i]);
        }
    }

    public double clip(double value) {
        if (value < min) return min;
        else if (value > max) return max;
        else return value;
    }

}
