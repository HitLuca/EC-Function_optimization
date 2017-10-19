package src.genetics;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.special.Gamma;
import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CMAEvolutionaryStrategy extends AEA {
    // region parameters
    private final int N = 10;

    //User defined population parameters
    private int lambda;
    private int mu;

    // strategy parameters
    private RealMatrix C;
    private RealVector weights;
    private double mu_w;
    private double c_c;
    private double c_sigma;
    private double c_1;
    private double c_mu;
    private double d_sigma;
    private RealVector p_c;
    private RealVector p_sigma;
    private double expectation;

    // Gaussian parameters
    private RealVector m;
    private double sigma;

    // sigma parameters
    private double stagnancyStep = 1e-15;
    private int stagnancyLimit = 10;
    private double stoppingThreshold = 10.0;// - 1e-4;

    private double[] sigmas = new double[]{0.5, 0.3, 0.4, 0.1, 0.6};
    private int selected_sigma_index = 0;
    private double sigma_threshold_high = 3;
    private double sigma_threshold_low = 1e-5;

    private boolean useFixedSigma = false;

    // run parameters
    private double best_old = 0;
    private double algorithmBest;
    private int stagnancyCounter = 0;
    private RealVector m_old;

    // print flag
    private boolean printOutput;

    private int epochs;
    private ContestEvaluation evaluation;
    // endregion

    public CMAEvolutionaryStrategy(int mu, int lambda, ContestEvaluation evaluation, int epochs, boolean printOutput) {
        this.lambda = lambda;
        this.mu = mu;
        this.evaluation = evaluation;
        this.epochs = epochs;
        this.printOutput = printOutput;

        initParameters();
        resetVariables();
    }

    private void initParameters() {
        //Initialize parameters with the recommended values
        mu_w = 0.3 * lambda;
        c_c = 4.0 / N;
        c_sigma = 4.0 / N;
        c_1 = 2.0 / (N * N);
        c_mu = mu_w / (N * N);
        d_sigma = 1 + Math.sqrt(mu_w / (double) N);

        // calculate expectation
        expectation = Math.sqrt(2) * Gamma.gamma((N + 1) / 2.0) / Gamma.gamma(N / 2.0);
    }

    private void resetVariables() {
        C = MatrixUtils.createRealIdentityMatrix(N);

        //Initialize weights uniformly
        weights = zerosVector(mu).mapAdd(1.0 / mu);

        p_c = zerosVector(N);
        p_sigma = zerosVector(N);

        //Initial distribution params
        m = zerosVector(N);
        sigma = sigmas[selected_sigma_index];
        sigma_threshold_low = sigma;

        m_old = zerosVector(N);
        best_old = 0;
        stagnancyCounter = 0;
    }

    public void run() {
        m_old = m.copy();

        for (int ep = 0; ep < epochs || epochs < 0; ep++) {
            // sample and evaluate
            List<CMAIndividual> xs = sample(evaluation);
            CMAIndividual parent = new CMAIndividual(m_old.toArray(), evaluation);

            xs.add(parent);
            xs.sort(new CMAIndividual.FitnessComparator().reversed());

            double best = xs.get(0).getFitness();

            // check best fitness
            if (best > algorithmBest) {
                algorithmBest = best;
            }

            // check stagnancy
            if ( (best - best_old) < stagnancyStep) {
                stagnancyCounter++;
                if (stagnancyCounter > stagnancyLimit) {
                    resetParameters();
                    continue;
                }
            } else {
                stagnancyCounter = 0;
            }

            best_old = xs.get(0).getFitness();

            // selection
            xs = xs.subList(0, mu);
            List<RealVector> ys = new ArrayList<>();

            for (CMAIndividual x : xs) {
                RealVector y = new ArrayRealVector(x.getGenome());
                y = y.subtract(m).mapDivide(sigma);
                ys.add(y);
            }

            m_old = m.copy();
            RealVector yw = weightVectors(ys);

            update_m(yw);
            update_p_sigma(yw);
            update_p_c(yw);
            updateC(ys);
            update_sigma();

            if (printOutput) {
                System.out.println("Epoch: " + ep
                        + " Best Fitness: " + algorithmBest
                        + " Fitness: " + best
                        + " sigma: " + sigma);
            }

            // check sigma against thresholds
            if (sigma > sigma_threshold_high)
                sigma = sigma_threshold_high;

            if (sigma < sigma_threshold_low)
                sigma = sigma_threshold_low;

//            if (sigma > sigma_threshold_high || sigma < sigma_threshold_low) {
//                useFixedSigma = true;
//                resetParameters();
//            }

            if (algorithmBest >= stoppingThreshold) {
                return;
            }
        }
    }

    @Override
    public void printAlgorithmParameters() {
        System.out.println("Properties:");
        System.out.println("algorithmType = " + this.toString());
        System.out.println("EndProperties\n");
    }

    private ArrayList<CMAIndividual> sample(ContestEvaluation evaluation) {
        ArrayList<CMAIndividual> sampled = new ArrayList<>();
        MultivariateNormalDistribution mnd =
                new MultivariateNormalDistribution(zerosVector(N).toArray(), C.getData());

        for (int i = 0; i < lambda; i++) {
            RealVector vector = new ArrayRealVector(mnd.sample());
            vector = vector.mapMultiply(sigma).add(m);
            sampled.add(new CMAIndividual(vector.toArray(), evaluation));
        }
        return sampled;
    }

    private RealVector weightVectors(List<RealVector> ys) {
        RealVector weighted = zerosVector(N);

        for (int i = 0; i < mu; i++) {
            double weight = weights.getEntry(i);
            weighted = weighted.add(ys.get(i).mapMultiply(weight));
        }

        return weighted;
    }

    private RealMatrix weightMatrices(ArrayList<RealMatrix> ms) {
        RealMatrix weighted = zerosMatrix(N);

        for (int i = 0; i < mu; i++) {
            double weight = weights.getEntry(i);
            weighted = weighted.add(ms.get(i).scalarMultiply(weight));

        }
        return weighted;
    }

    private RealVector zerosVector(int dim) {
        double[] vArray = new double[dim];
        for (int i = 0; i < dim; i++)
            vArray[i] = 0;

        return new ArrayRealVector(vArray);
    }

    private RealMatrix zerosMatrix(int dim) {
        RealVector zeros = zerosVector(dim);
        return zeros.outerProduct(zeros);
    }

    private RealMatrix inverseSquareRootC() {
        EigenDecomposition e = new EigenDecomposition(C);
        if (!e.getSolver().isNonSingular()) {
            DiagonalMatrix error_C = new DiagonalMatrix(zerosVector(N).mapAdd(1e-6).toArray());
            C = C.add(error_C);
            e = new EigenDecomposition(C);
        }

        return MatrixUtils.inverse(e.getSquareRoot());
    }

    private void updateC(List<RealVector> ys) {
        RealMatrix C_addend1 = C.scalarMultiply(1 - c_1 - c_mu);
        RealMatrix C_addend2 = (p_c.outerProduct(p_c)).scalarMultiply(c_1);

        ArrayList<RealMatrix> ys_product = new ArrayList<>();
        for (RealVector y : ys) {
            ys_product.add(y.outerProduct(y));
        }
        RealMatrix C_addend3 = weightMatrices(ys_product).scalarMultiply(c_mu);

        C = C_addend1.add(C_addend2.add(C_addend3));
    }

    private void update_p_sigma(RealVector yw) {
        RealMatrix C_mod = inverseSquareRootC();

        RealVector p_sigma_addend2 = C_mod.operate(yw).mapMultiply(Math.sqrt(1 - (1 - c_sigma) * (1 - c_sigma)) * Math.sqrt(mu_w));

        p_sigma = p_sigma.mapMultiply(1 - c_sigma).add(p_sigma_addend2);
    }

    private void update_p_c(RealVector yw) {
        double indicator = 0;
        if (p_sigma.getNorm() <= 1.5 * Math.sqrt(N))
            indicator = 1;

        p_c = p_c.mapMultiply(1 - c_c).add(yw.mapMultiply(indicator *
                Math.sqrt(1 - (1 - c_c) * (1 - c_c)) * Math.sqrt(mu_w)));
    }

    private void update_sigma() {
        if (!useFixedSigma) {
            sigma *= Math.exp((c_sigma / d_sigma) * ((p_sigma.getNorm() / expectation) - 1));
        }
    }

    private void update_m(RealVector yw) {
        m = yw.mapMultiply(sigma).add(m);
    }

    private void resetParameters() {
        resetVariables();
        selected_sigma_index++;
        selected_sigma_index = selected_sigma_index % sigmas.length;
        if (selected_sigma_index == 0) {
            stagnancyLimit += 5;
        }
    }
}

class CMAIndividual {
    private double[] genome;
    private double fitness;

    public CMAIndividual(double[] genome, ContestEvaluation evaluation) {
        this.genome = genome;
        fitness = (double) evaluation.evaluate(genome);
    }

    public double getFitness() {
        return fitness;
    }

    public double[] getGenome() {
        return genome;
    }

    static public class FitnessComparator implements Comparator<CMAIndividual> {
        // Used for sorting in ascending order
        public int compare(CMAIndividual a, CMAIndividual b) {
            if (a.getFitness() > b.getFitness())
                return 1;
            else if (a.getFitness() == b.getFitness())
                return 0;
            else
                return -1;
        }
    }
}
