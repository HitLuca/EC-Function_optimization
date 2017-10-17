package src.genetics;

public abstract class AEA {
    public abstract void run();

    public abstract void printAlgorithmParameters();

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
