package src;

import java.util.ArrayList;


public abstract class ASelection {
    private int size;
    public ASelection(int size) {
            this.size=size;
    }

    public ArrayList<Individual> select(ArrayList<Individual> population)
    {
        return select(population, size);
    }

    public abstract ArrayList<Individual> select(ArrayList<Individual> population, int size);

    public int getSize() {
        return size;
    }
}
