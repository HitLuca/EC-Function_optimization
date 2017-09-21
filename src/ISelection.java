package src;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Недко on 21.9.2017 г..
 */
public interface ISelection {
    public class FitnessComparator implements Comparator<Individual>
    {
        // Used for sorting in ascending order of
        // roll name
        public int compare(Individual a, Individual b)
        {
            if(a.getFitness() > b.getFitness())
                return 1;
            else if (a.getFitness() == b.getFitness())
                return 0;
            else
                return -1;
        }
    }

    public ArrayList<Individual> select(ArrayList<Individual> population);
    public ArrayList<Individual> select(ArrayList<Individual> population, int size);
}
