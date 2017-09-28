package src;

import java.io.*;
import java.util.Random;

public class ProjectLogger {
    private static BufferedWriter logger;

    public static void main(String args[]) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        Random rng = new Random();

//        String function = "BentCigarFunction";
//        String function = "SchaffersEvaluation";
        String function = "KaatsuraEvaluation";

        String outputPath = "logs/output.log";

        FileWriter fileWriter = new FileWriter(outputPath);
        logger = new BufferedWriter(fileWriter);
        writeHeader();

        Process pr = run.exec("mv ./testrun/EC-Project.jar ./testrun/submission.jar");
        pr.waitFor();

        pr = run.exec("java -jar ./testrun/testrun.jar -submission=player27 -evaluation=" + function + " -seed=1");
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;

        while ((line = buf.readLine()) !=null )
        {
            System.out.println(line);
            logger.write(line + "\n");
        }
        logger.close();
    }

    private static void writeHeader() throws IOException {
    }
}
