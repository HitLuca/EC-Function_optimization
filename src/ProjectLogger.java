package src;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectLogger {
    private static BufferedWriter logger;

    public static void main(String args[]) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();

//        String function = "BentCigarFunction";
        String function = "SchaffersEvaluation";
//        String function = "KatsuuraEvaluation";

        int runsNumber = 3;

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        String outputPath = "logs/" + dateFormat.format(date) + "_" + function + "/";
        new File(outputPath).mkdir();

//        deleteLastRunFile();

        FileWriter fileWriter = new FileWriter(outputPath + "/run.log");
        logger = new BufferedWriter(fileWriter);

        for (int i = 0; i < runsNumber; i++) {
            Process pr = run.exec("mv ./testrun/EC-Project.jar ./testrun/submission.jar");
            pr.waitFor();

            pr = run.exec("java -jar ./testrun/testrun.jar -submission=player27 -evaluation=" + function + " -seed=1");
//            pr.waitFor();

            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;

            while ((line = buf.readLine()) != null) {
                System.out.println(line);
                logger.write(line + "\n");
            }

//            File source = new File(outputPath + filename);
//            File destination = new File("logs/" + "last_run.log");
//
////            copyFile(source,destination);
//            appendFile(source, destination);
        }
        logger.close();


    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new RandomAccessFile(sourceFile, "rw").getChannel();
            destination = new RandomAccessFile(destFile, "rw").getChannel();

            long position = 0;
            long count = source.size();

            source.transferTo(position, count, destination);
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    private static void appendFile(File sourceFile, File destFile) throws IOException {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(destFile, true));
            BufferedReader in = new BufferedReader(new FileReader(sourceFile));
            String str;
            while ((str = in.readLine()) != null) {
                out.write(str + '\n');
            }
            in.close();
            out.close();
        } catch (IOException e) {
        }
    }

    private static void deleteLastRunFile() {
        File lastRun = new File("logs/" + "last_run.log");
        if (lastRun.exists()) {
            lastRun.delete();
        }
    }
}
