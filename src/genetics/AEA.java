package src.genetics;


public abstract class AEA implements Runnable {
    public abstract void run();

    public abstract void printAlgorithmParameters();

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
