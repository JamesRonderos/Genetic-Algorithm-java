import java.util.*;

public class Pair {
    private ArrayList < Chromosome > PR_pop;

    public Pair(ArrayList < Chromosome > population) {
        PR_pop = population;
    }

    public int SimplePair() {
        return (PR_pop.size() / 4); //the number of mating pairs
    }

    public ArrayList < Chromosome > Tournament_pairing() {
        ArrayList < Chromosome > tmpList = new ArrayList < > (PR_pop.subList(0, PR_pop.size() / 4));
        Collections.shuffle(tmpList);
        return tmpList;
    }
}