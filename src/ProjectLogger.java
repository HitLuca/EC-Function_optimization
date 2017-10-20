package src;

import src.genetics.GA.crossover.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProjectLogger {
    private static Runtime run;
    private static String[] functions = new String[]{
            "BentCigarFunction",
            "SchaffersEvaluation",
            "KatsuuraEvaluation"};

    private static String[] algorithms = new String[]{
            "SSGA",
            "GGA",
            "PSO",
            "DE",
            "CMA-ES"};

    private static String[] crossovers = new String[] {
            "CrossoverAverage",
            "CrossoverAverageWeighted",
            "CrossoverCoinFlip",
            "CrossoverCoinFlipWeighted",
            "CrossoverNPoints"};

    private static Process pr;

    public static void main(String args[]) throws IOException, InterruptedException {
        run = Runtime.getRuntime();
        pr = run.exec("mv ./testrun/EC-Project.jar ./testrun/submission.jar");
        pr.waitFor();

        logCrossoverResults();
//        logAlgorithmResults();
//        logGridSearch();
    }

    private static void logGridSearch() throws IOException {
        String outputDir = "logs/gridSearch/";

        int runsNumber = 8;

        for (String algorithm : algorithms) {
            System.out.println("Algorithm: " + algorithm);
            ArrayList<String> gridSearchStrings = gridSearchString(algorithm);

            for (String function : functions) {
                System.out.println("Function: " + function);
                for (String gridSearchString : gridSearchStrings) {
                    System.out.println("String: " + gridSearchString);
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    String fileName = dateFormat.format(date) + "_" + algorithm + "_" + function + gridSearchString + ".log";

                    FileWriter fileWriter = new FileWriter(outputDir + fileName);
                    BufferedWriter logger = new BufferedWriter(fileWriter);

                    for (int i = 0; i < runsNumber; i++) {
                        pr = run.exec("java -Dalgorithm=" + algorithm + " " + gridSearchString + " -jar ./testrun/testrun.jar -submission=player27 -evaluation=" + function + " -seed=1");

                        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                        String line;

                        while ((line = buf.readLine()) != null) {
                            System.out.println(line);
                            logger.write(line + "\n");
                        }
                    }
                    logger.close();
                }
            }
        }
    }

    private static ArrayList<String> gridSearchString(String algorithm) {
        ArrayList<String> searchString = new ArrayList<>();
        switch (algorithm) {
            case "GGA": {
                int[] elitisms = new int[]{1, 3, 5};
                double[] sigmas = new double[]{0.01, 0.05, 0.1};

                for (int elitism : elitisms) {
                    for(double sigma:sigmas) {
                        String s = " -Delitism=" + elitism +
                                " -Dsigma=" + sigma;
                        searchString.add(s);
                    }
                }
                break;
            }
            case "SSGA": {
                double[] sigmas = new double[]{0.01, 0.05, 0.1};

                for (int replacementNumber = 10; replacementNumber <= 50; replacementNumber += 10) {
                    for(double sigma:sigmas) {
                        String s = " -DreplacementNumber=" + replacementNumber +
                                " -Dsigma=" + sigma;
                        searchString.add(s);
                    }
                }
                break;
            }
            case "PSO": {
                int[] swarmSizes = new int[]{50, 80, 100};
                for (int swarmSize : swarmSizes) {
                    for (double w = 0.7; w <= 0.9; w += 0.1) {
                        for (double phi1 = 0.1; phi1 <= 0.7; phi1 += 0.2) {
                            for (double phi2 = 0.1; phi2 <= 0.7; phi2 += 0.2) {
                                String s = " -DswarmSize=" + swarmSize +
                                        " -Dw=" + w +
                                        " -Dphi1=" + phi1 +
                                        " -Dphi2=" + phi2;
                                searchString.add(s);
                            }
                        }
                    }
                }
                break;
            }
            case "DE": {
                int[] popSizes = new int[]{20, 50, 100};
                char[] bases = new char[]{'b', 'r'};
                int[] diffNs = new int[]{1, 2};

                for (int popSize : popSizes) {
                    for (double f = 0.1; f <= 0.7; f += 0.2) {
                        for (double cr = 0.1; cr <= 0.9; cr += 0.2) {
                            for (char base : bases) {
                                for (int diffN : diffNs) {
                                    String s = " -DpopSize=" + popSize +
                                            " -Df=" + f +
                                            " -Dcr=" + cr +
                                            " -Dbase=" + base +
                                            " -DdiffN=" + diffN;
                                    searchString.add(s);
                                }
                            }
                        }
                    }
                }
                break;
            }
            case "CMA-ES": {
                int[] mus = new int[]{2, 3, 4};
                for (int lambda = 10; lambda <= 40; lambda += 5) {
                    for (int mu : mus) {
                        String s = " -Dlambda=" + lambda +
                                " -Dmu=" + (int) (1.0 * lambda / mu);
                        searchString.add(s);
                    }
                }
                break;
            }
        }
        return searchString;
    }

    private static void logCrossoverResults() throws IOException {
        String outputDir = "logs/crossoverTests/";
        int runsNumber = 6;

        for (String crossover:crossovers) {
            for (String function : functions) {
                System.out.println("Function: " + function);

                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

                String filename = dateFormat.format(date) + "_" + function + "_" + crossover + ".log";

                FileWriter fileWriter = new FileWriter(outputDir + filename);
                BufferedWriter logger = new BufferedWriter(fileWriter);

                for (int i = 0; i < runsNumber; i++) {
                    pr = run.exec("java -Dalgorithm=" + algorithms[0] + " -Dcrossover=" + crossover + " -jar ./testrun/testrun.jar -submission=player27 -evaluation=" + function + " -seed=1");

                    BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                    String line;

                    while ((line = buf.readLine()) != null) {
                        System.out.println(line);
                        logger.write(line + "\n");
                    }
                }
                logger.close();
            }
        }
    }

    private static void logAlgorithmResults() throws IOException {
        String outputDir = "logs/results/";

        int runsNumber = 10;

        for (String algorithm : algorithms) {
            System.out.println("Algorithm: " + algorithm);
            for (String function : functions) {
                System.out.println("Function: " + function);
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String filename =  dateFormat.format(date) + "_" + algorithm + "_" + function + ".log";

                FileWriter fileWriter = new FileWriter(outputDir + filename);
                BufferedWriter logger = new BufferedWriter(fileWriter);

                for (int i = 0; i < runsNumber; i++) {
                    pr = run.exec("java -Dalgorithm=" + algorithm + " -jar ./testrun/testrun.jar -submission=player27 -evaluation=" + function + " -seed=1");

                    BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                    String line;

                    while ((line = buf.readLine()) != null) {
                        System.out.println(line);
                        logger.write(line + "\n");
                    }
                }
                logger.close();
            }
        }
    }
}