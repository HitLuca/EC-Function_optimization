package src;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectLogger {
    private static Runtime run;
    private static String[] functions = new String[]{
            "BentCigarFunction",
            "SchaffersEvaluation",
            "KatsuuraEvaluation"};

    private static String[] algorithms = new String[] {
            "SSGA",
            "GGA",
            "PSO",
            "DE",
            "CMA-ES"};
    private static Process pr;

    public static void main(String args[]) throws IOException, InterruptedException {
        run = Runtime.getRuntime();
        pr = run.exec("mv ./testrun/EC-Project.jar ./testrun/submission.jar");
        pr.waitFor();

//        logCrossoverResults();
        logAlgorithmResults();
    }

    private static void logCrossoverResults() throws IOException {
        String outputDir = "logs/crossoverTests/";
        int runsNumber = 6;

        for(int crossover = 0; crossover < 5; crossover ++) {
            for (String function : functions) {
                System.out.println("Function: " + function);

                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

                String outputPath = outputDir + dateFormat.format(date) + "_" + function + "/";
                new File(outputPath).mkdir();

                FileWriter fileWriter = new FileWriter(outputPath + "/run.log");
                BufferedWriter logger = new BufferedWriter(fileWriter);

                for (int i = 0; i < runsNumber; i++) {
                    pr = run.exec("java -Dcrossover=" + crossover + " -jar ./testrun/testrun.jar -submission=player27 -evaluation=" + function + " -seed=1");

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

        for(String algorithm:algorithms) {
            System.out.println("Algorithm: " + algorithm);
            for (String function : functions) {
                System.out.println("Function: " + function);
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String outputPath = outputDir + dateFormat.format(date) + "_" + algorithm + "_" + function + "/";
                new File(outputPath).mkdir();

                FileWriter fileWriter = new FileWriter(outputPath + "/run.log");
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
