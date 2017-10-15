package src.genetics;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.special.Gamma;
import org.vu.contest.ContestEvaluation;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.List;

public class CMAEvolutionaryStrategy {
    // region parameters
    private final int N = 10;

    //User defined population parameters
    private int lambda;
    private int mu;

    //Strategy parameters
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

    //Strategy Gaussian parameters
    private RealVector m;
    private double sigma;

    private double stagnancyStep = 1e-10;
    private int stagnancyLimit = 10;

    private double[] sigmas = new double[]{0.5, 0.3, 0.4, 0.1, 0.6};
    private int selected_sigma_index = 0;
    private double sigma_threshold_high = 3;
    private double sigma_threshold_low = 1e-5;

    private boolean useFixedSigma = false;

    private double best_old = 0;
    private double best;
    private double algorithmBest;
    private int stagnancyCounter = 0;
    private RealVector m_old;
    // endregion

    public CMAEvolutionaryStrategy(int mu, int lambda) {
        init(mu, lambda);
    }

    private void init(int mu, int lambda) {
        this.lambda = lambda;
        this.mu = mu;
        try{zerosVector(1);}
        catch (Exception e)
        {
            e.printStackTrace();
        }

        C = MatrixUtils.createRealIdentityMatrix(N);

        //Initialize weights uniformly
        weights = zerosVector(mu).mapAdd(1.0/mu);
//        weights = new ArrayRealVector(new double[]{0.5, 0.25, 0.1125, 0.05625, 0.05625});


        double weights_norm = weights.getNorm();
        double sumOfSq = weights_norm*weights_norm;
//        this.lambda = (int)(1/sumOfSq);
//        lambda = (int)(1/sumOfSq);

        //Initialize parameters with the recommended values
        mu_w = 0.3*lambda;//1/sumOfSq;
        c_c = 4.0/ N;
        c_sigma = 4.0/ N; //3 / N ?
        c_1 = 2.0/(N * N);
        c_mu = mu_w/(N * N);
        d_sigma = 1 + Math.sqrt(mu_w/ (double)N);

        p_c = zerosVector(N);
        p_sigma = zerosVector(N);

        //Initial distribution params!
        m = zerosVector(N);
        sigma = sigmas[0];

        expectation = Math.sqrt(2) * Gamma.gamma((N + 1) / 2.0) / Gamma.gamma(N / 2.0);
    }

    public void run(ContestEvaluation evaluation, int epochs) {
        m_old = m.copy();

        for(int ep = 0; ep < epochs || epochs < 0; ep++) {
            // sample and evaluate
            List<Individual> xs = sample(evaluation);
            Individual parent = new Individual(m_old.toArray(), evaluation);

            xs.add(parent);
            xs.sort(new Individual.FitnessComparator().reversed());

            best = xs.get(0).getFitness();

            // check best fitness
            if(best > algorithmBest) {
                algorithmBest = best;
            }

            // check stagnancy
            if (useFixedSigma && (best - best_old) < stagnancyStep) {
                stagnancyCounter ++;
                if(stagnancyCounter > stagnancyLimit) {
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

            for (Individual x : xs) {
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

            // check sigma against thresholds
            if (sigma > sigma_threshold_high || sigma < sigma_threshold_low) {
                useFixedSigma = true;
                resetParameters();
            }

            System.out.println("Epoch: " + ep
                    + " Best Fitness: " + algorithmBest
                    + " Fitness: " + best
                    + " sigma: " + sigma);
        }
    }

    private ArrayList<Individual> sample(ContestEvaluation evaluation) {
        ArrayList<Individual> sampled = new ArrayList<>();
        MultivariateNormalDistribution mnd =
                new MultivariateNormalDistribution(zerosVector(N).toArray(), C.getData());

        for (int i = 0; i < lambda; i++) {
            RealVector vector = new ArrayRealVector(mnd.sample());
            vector = vector.mapMultiply(sigma).add(m);
            sampled.add(new Individual(vector.toArray(), evaluation));
        }
        return sampled;
    }

    public static void printArray(double matrix[][]) {
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                System.out.print(matrix[row][column] + " ");
            }
            System.out.println();
        }
    }

    private RealVector weightVectors(List<RealVector> ys) {
        RealVector weighted = zerosVector(N);

        for (int i=0; i<mu; i++) {
            double weight = weights.getEntry(i);
            weighted = weighted.add(ys.get(i).mapMultiply(weight));
        }

//        for (RealVector v: ys)
//        {
//            averaged = averaged.add(v);
//        }
//
//        averaged.mapDivideToSelf(ys.size());

        return weighted;
    }

    private RealMatrix weightMatrices(ArrayList<RealMatrix> ms) {
        RealMatrix weighted = zerosMatrix(N);

        for (int i=0; i<mu; i++) {
            double weight = weights.getEntry(i);
            weighted = weighted.add(ms.get(i).scalarMultiply(weight));

        }
        return weighted;
    }

    private RealVector zerosVector(int dim) {
        double[] vArray = new double[dim];
        for(int i=0; i<dim; i++)
            vArray[i]=0;

        return new ArrayRealVector(vArray);
    }

    private RealMatrix zerosMatrix(int dim) {
        RealVector zeros = zerosVector(dim);
        return zeros.outerProduct(zeros);
    }

    private RealMatrix inverseSquareRootC() {
        EigenDecomposition e = new EigenDecomposition(C);
        if(!e.getSolver().isNonSingular()) {
            DiagonalMatrix error_C = new DiagonalMatrix(zerosVector(N).mapAdd(1e-6).toArray());
//            MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(zerosVector(N).toArray(), error_C.getData());
            C = C.add(error_C);
            e = new EigenDecomposition(C);
        }

        return MatrixUtils.inverse(e.getSquareRoot());
    }

    private void updateC(List<RealVector> ys) {
//        double indicatorSq = 0;
//        double p_sigma_norm = p_sigma.getNorm();
//        if(p_sigma_norm*p_sigma_norm <= 1.5*Math.sqrt(N)) {
//            indicatorSq = 1;
//        }
//
//        double c_s = (1-indicatorSq)*c_1*c_c*(2-c_c);

        RealMatrix C_addend1 = C.scalarMultiply(1 - c_1 - c_mu); // + c_s
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
        if(!useFixedSigma) {
            sigma *= Math.exp((c_sigma / d_sigma) * ((p_sigma.getNorm() / expectation) - 1));
        }
    }

    private void update_m(RealVector yw) {
        m = yw.mapMultiply(sigma).add(m);
    }

    private void resetParameters() {
        init(mu, lambda);
        m_old = zerosVector(N);
        best_old = 0;
        stagnancyCounter = 0;
        sigma = sigmas[selected_sigma_index];
        selected_sigma_index ++;
        selected_sigma_index = selected_sigma_index % sigmas.length;
        if(selected_sigma_index == 0) {
            stagnancyLimit += 5;
        }
    }
}
