package src.genetics;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.special.Gamma;
import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMAEvolutionaryStrategy {
    public final int N = 10;

    private ArrayList<Individual> population;
    private int maxSize;



    private double meanFitness = -1;
    private double worstFitness = -1;
    private double bestFitness = -1;

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

    //Strategy Gaussian parameters
    private RealVector m;
    private double sigma;

    public CMAEvolutionaryStrategy(int mu, int lambda)
    {
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
        double weights_norm = weights.getNorm();
        double sumOfSq = weights_norm*weights_norm;

        //Initialize parameters with the recommended values
        mu_w = 1/sumOfSq;
        c_c = 4.0/ N;
        c_sigma = 4.0/ N; //3 / N ?
        c_1 = 2.0/(N * N);
        c_mu = mu_w/(N * N);
        d_sigma = 1 + Math.sqrt(mu_w/ N);

        p_c = zerosVector(N);
        p_sigma = zerosVector(N);

        //Initial distribution params!
        m = zerosVector(N);
        sigma = 1;
    }

    public void run(ContestEvaluation evaluation, int epochs)
    {
        RealVector m_old = m.copy();
        for(int ep=0; ep<epochs; ep++) {
            ///SAMPLE AND EVALUATE

            List<Individual> xs = sample(evaluation);
            Individual parent = new Individual(m_old.toArray(), evaluation);
            System.out.println(parent.getFitness());

            xs.add(parent);
            xs.sort(new Individual.FitnessComparator().reversed());
            System.out.println("Best: " + xs.get(0).getFitness());

            //SELECTION
            xs = xs.subList(0, mu);
            List<RealVector> ys = new ArrayList<>();

            for (Individual x : xs) {
                RealVector y = new ArrayRealVector(x.getGenome());
                y = y.subtract(m).mapDivide(sigma);
                ys.add(y);
            }

            //UPDATE M
            m_old = m.copy();
            RealVector yw = averageVectors(ys);
            m = yw.mapMultiply(sigma).add(m);

            ///COVARIANCE CUMMULATION

            double indicator = 0;
            if (p_sigma.getNorm() <= 1.5 * Math.sqrt(N))
                indicator = 1;

            p_c = p_c.mapMultiply(1 - c_c).add(yw.mapMultiply(indicator *
                    Math.sqrt(1 - (1 - c_c) * (1 - c_c)) * Math.sqrt(mu_w)));

            ///CSA

            //Computing C^(-1/2)
            EigenDecomposition e = new EigenDecomposition(C);
            RealMatrix C_mod = MatrixUtils.inverse(e.getSquareRoot());

            RealVector p_sigma_addend2 = C_mod.operate(yw).mapMultiply(Math.sqrt(1 - (1 - c_sigma) * (1 - c_sigma)) * Math.sqrt(mu_w));

            p_sigma = p_sigma.mapMultiply(1 - c_sigma).add(p_sigma_addend2);

            ///UPDATE C
//        double indicatorSq = 0;
//        double p_sigma_norm = p_sigma.getNorm();
//        if(p_sigma_norm*p_sigma_norm <= 1.5*Math.sqrt(N))
//            indicatorSq = 1;
//
//        double c_s = (1-indicatorSq)*c_1*c_c*(2-c_c);

            RealMatrix C_addend1 = C.scalarMultiply(1 - c_1 - c_mu); // + c_s
            RealMatrix C_addend2 = p_c.outerProduct(p_c).scalarMultiply(c_1);

            ArrayList<RealMatrix> ys_product = new ArrayList<>();

            for (RealVector y : ys) {
                ys_product.add(y.outerProduct(y));
            }

            RealMatrix C_addend3 = averageMatrices(ys_product).scalarMultiply(c_mu);
            C = C_addend1.add(C_addend2.add(C_addend3));

            ///UPDATE SIGMA

            double expectation = Math.sqrt(2) * Gamma.gamma((N + 1) / 2.0) / Gamma.gamma(N / 2.0);
            sigma = sigma * Math.exp((c_sigma / d_sigma) * ((p_sigma.getNorm() / expectation) - 1));

//            System.out.println(Arrays.toString(m.toArray()));

            System.out.println("Epoch: " + ep + " Fitness: " + (double) evaluation.evaluate(m.toArray()));
        }
    }

    public ArrayList<Individual> sample(ContestEvaluation evaluation)
    {
        ArrayList<Individual> sampled = new ArrayList<>();
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(m.toArray(), C.getData());

        for(int i=0; i<lambda;i++) {
            RealVector vector = new ArrayRealVector(mnd.sample());
            vector = vector.mapMultiply(sigma).add(m);
            sampled.add(new Individual(vector.toArray(), evaluation));
        }

        return sampled;
    }

    public void updateMean(ArrayList<RealVector> vs)
    {
        m = averageVectors(vs);
    }

    private RealVector averageVectors(List<RealVector> vs)
    {
        RealVector averaged = zerosVector(N);

        for (RealVector v: vs)
        {
            averaged = averaged.add(v);
        }

        averaged.mapDivideToSelf(vs.size());

        return averaged;
    }

    private RealMatrix averageMatrices(ArrayList<RealMatrix> ms)
    {
        RealMatrix averaged = zerosMatrix(N);

        for (RealMatrix m: ms)
        {
            averaged = averaged.add(m);
        }

        averaged = averaged.scalarMultiply(1.0/ms.size());

        return averaged;
    }

    private RealVector zerosVector(int dim)
    {
        double[] vArray = new double[dim];
        for(int i=0; i<dim; i++)
            vArray[i]=0;

        return new ArrayRealVector(vArray);
    }

    private RealMatrix zerosMatrix(int dim)
    {
        RealVector zeros = zerosVector(dim);
        return zeros.outerProduct(zeros);
    }
}
