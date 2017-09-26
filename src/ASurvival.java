package src;

import java.util.ArrayList;

public abstract class ASurvival {

    private int size;
    public ASurvival(int size) {
        this.size = size;
    }

    public ArrayList<Individual> survival(ArrayList<Individual> population) {
        return survival( population, size);
    }

    public abstract ArrayList<Individual> survival(ArrayList<Individual> population, int size);
}
