package src.genetics.GA.selection;

import src.genetics.GA.other.Individual;
import src.genetics.GA.other.Population;

import java.util.ArrayList;
import java.util.Random;

import static src.genetics.GA.other.Population.GENOME_SIZE;

public class SelectionTribalism extends ASelection {
    private boolean windowing;
    private double tribeWeight;

    public SelectionTribalism(Random rng, int size, double tribeWeight, boolean windowing) {
        super(rng, size);
        this.tribeWeight = tribeWeight;
        this.windowing = windowing;
    }

    // From http://introcs.cs.princeton.edu/java/91float/Gamma.java.html
    private static double logGamma(double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1)
                + 24.01409822 / (x + 2) - 1.231739516 / (x + 3)
                + 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);
        return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
    }

    private static double gamma(double x) {
        return Math.exp(logGamma(x));
    }

    @Override
    public ArrayList<Individual> select(Population pop) {
        ArrayList<Individual> population = new ArrayList<>(pop.getIndividuals());
        double worstFitness = 0;
        double[] attraction = new double[population.size()];


        if (windowing) {
            population.sort(new Individual.FitnessComparator());
            worstFitness = population.get(0).getFitness();
        }

        ArrayList<Individual> selectedIndividuals = new ArrayList<>();

        double totalWeight = 0;
        for (int i = 0; i < population.size(); i++) {
            attraction[i] = population.get(i).getFitness() - worstFitness;
            totalWeight += attraction[i];
        }

        double randomValue = rng.nextDouble() * totalWeight;
        for (int i = 0; i < population.size(); i++) {
            randomValue -= attraction[i];
            if (randomValue <= 0) {
                selectedIndividuals.add(population.get(i));
                population.remove(i);
                break;
            }
        }

        double[] attractionT;
        double a, b;
        a = 5;
        b = 1;

        while (selectedIndividuals.size() < parentsNumber) {
            // calculate tribalism
            attractionT = attraction;
            totalWeight = 0;
            for (int i = 0; i < population.size(); i++) {
                attractionT[i] *= Math.pow(measureTribalism(selectedIndividuals, population.get(i), a, b), tribeWeight);
                totalWeight += attractionT[i];
            }

            // extract the next parent based on fitness*tribalism
            randomValue = rng.nextDouble() * totalWeight;
            for (int i = 0; i < population.size(); i++) {
                randomValue -= attractionT[i];
                if (randomValue <= 0) {
                    selectedIndividuals.add(population.get(i));
                    population.remove(i);
                    break;
                }
            }
        }

        assert selectedIndividuals.size() == parentsNumber;
        return selectedIndividuals;
    }

    private double parentsDistance(Individual ind1, Individual ind2) {
        double distance = 0.0;
        for (int i = 0; i < GENOME_SIZE; i++) {
            distance += Math.pow(ind1.getGenome()[i] - ind2.getGenome()[i], 2);
        }
        return Math.sqrt(distance);
    }
    //

    private double gammaDistribution(double x, double a, double b) {
        double density = (Math.pow(a, b) / gamma(a)) * Math.pow(x, a - 1) * Math.exp(-b * x);
        return density;
    }

    private double measureTribalism(ArrayList<Individual> parents, Individual candidate, double a, double b) {
        double tribalism = 0.0;
        for (Individual p : parents) tribalism += gammaDistribution(parentsDistance(p, candidate), a, b);
        return tribalism / parents.size();
    }
}