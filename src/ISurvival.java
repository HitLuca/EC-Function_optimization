package src;

import java.util.ArrayList;

/**
 * Created by Недко on 21.9.2017 г..
 */
public interface ISurvival {
    public ArrayList<Individual> survival(ArrayList<Individual> population);
    public ArrayList<Individual> survival(ArrayList<Individual> population, int size);
}
