package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PopulationLogger {
    private BufferedWriter writer;

    public PopulationLogger(String outputPath) throws IOException {
        writer.close();
        FileWriter fileWriter = new FileWriter(outputPath);
        writer = new BufferedWriter(fileWriter);
    }

    public void writeHeader() throws IOException {
        writer.write("epoch, mean fitness, best fitness, worst fitness");
    }
    public void logPopulation(int epoch, Population population) throws IOException {
        writer.write(epoch + ", " + population.getMeanFitness() + ", " + population.getBestFitness() + ", " + population.getWorstFitness());
    }

    public void close() throws IOException {
        writer.close();
    }
}
