package src;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Недко on 21.9.2017 г..
 */
public abstract class ASelection {

    private int size;
    public ASelection(int size) {
            this.size=size;
    }


    static public class FitnessComparator implements Comparator<Individual>
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

    public ArrayList<Individual> select(ArrayList<Individual> population)
    {
        return select(population, size);
    }


    public abstract ArrayList<Individual> select(ArrayList<Individual> population, int size);
}
