package src;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ProjectLogger {
    private static BufferedWriter logger;

    public static void main(String args[]) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        Random rng = new Random();

//        String function = "BentCigarFunction";
        String function = "SchaffersEvaluation";
//        String function = "KatsuuraEvaluation";

        int runsNumber = 4;

        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;

        String outputPath = "logs/" + dateFormat.format(date) + "_" + function + "/";
        new File(outputPath).mkdir();

        for (int i=0; i<runsNumber; i++) {
            String filename = i + ".log";

            FileWriter fileWriter = new FileWriter(outputPath + filename);
            logger = new BufferedWriter(fileWriter);

            Process pr = run.exec("mv ./testrun/EC-Project.jar ./testrun/submission.jar");
            pr.waitFor();

            pr = run.exec("java -jar ./testrun/testrun.jar -submission=player27 -evaluation=" + function + " -seed=1");
//            pr.waitFor();

            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;

            while ((line = buf.readLine()) !=null )
            {
                System.out.println(line);
                logger.write(line + "\n");
            }
            logger.close();

            File source=new File(outputPath + filename);
            File destination=new File("logs/" + "last_run.log");

            copyFile(source,destination);
        }


    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new RandomAccessFile(sourceFile,"rw").getChannel();
            destination = new RandomAccessFile(destFile,"rw").getChannel();

            long position = 0;
            long count    = source.size();

            source.transferTo(position, count, destination);
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
}
