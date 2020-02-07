import java.lang.*;
import java.util.*;

public abstract class GA extends Object
{
    protected int     GA_numChromesInit;
    protected int     GA_numChromes;
    protected int     GA_numGenes;
    protected double  GA_mutFact;
    protected int     GA_numIterations;
    protected ArrayList<Chromosome> GA_pop;
    protected String GA_target;
    protected String GA_algorithm;
    protected ArrayList<Character> locations;

    public GA(String ParamFile, String target, String algo)
    {
        GetParams GP        = new GetParams(ParamFile);
        Parameters P        = GP.GetParameters();
        GA_numChromesInit   = P.GetNumChromesI();
        GA_numChromes       = P.GetNumChromes();
        GA_numGenes         = P.GetNumGenes();
        GA_mutFact          = P.GetMutFact();
        GA_numIterations    = P.GetNumIterations();
        GA_pop              = new ArrayList<Chromosome>();
        GA_target           = target;
        GA_algorithm        = algo;
    }

    public void DisplayParams()
    {
        System.out.print("Initial Chromosomes:  ");
        System.out.println(GA_numChromesInit);
        System.out.print("Chromosomes: ");
        System.out.println(GA_numChromes);
        System.out.print("Genes: ");
        System.out.println(GA_numGenes);
        System.out.print("Mutation Factor: ");
        System.out.println(GA_mutFact);
        System.out.print("Iterations: ");
        System.out.println(GA_numIterations);
        System.out.print("Pairing & Mating Algorithm: ");
        Mate mate = new Mate(GA_pop,GA_numGenes,GA_numChromes);
        if(GA_algorithm.equalsIgnoreCase("1")) {
            System.out.print("Tournament & Single-Point Crossover");
        }
        else if(GA_algorithm.equalsIgnoreCase("2")) {
            System.out.print("Top-Down & Dual-Point Crossover");
        }
        else if(GA_algorithm.equalsIgnoreCase("3")) {
            System.out.print("Tournament & Dual-Point Crossover");
        }
        else
            System.out.print("Top-Down & Single-Point Crossover");

        System.out.println();
    }

    public void DisplayPop()
    {
        Iterator<Chromosome> itr = GA_pop.iterator();
        System.out.println("Number\tContents\t\tCost");

        int chromeNum = 0;
        while (itr.hasNext())
        {
            Chromosome chrome = itr.next();
            System.out.print(chromeNum);
            ++chromeNum;
            System.out.print("\t");
            DisplayChromosome(chrome);
            System.out.println();
        }
    }

    private void DisplayChromosome(Chromosome chrome)
    {
        chrome.DisplayGenes();
        System.out.print("\t\t\t");
        System.out.print(chrome.GetCost());
    }

    protected void SortPop()
    {
        Collections.sort(GA_pop, new CostComparator());
    }

    private class CostComparator implements Comparator <Chromosome>
    {
        int result;
        public int compare(Chromosome obj1, Chromosome obj2)
        {
            result = new Integer( obj1.GetCost() ).compareTo(
                    new Integer( obj2.GetCost() ) );
            return result;
        }
    }

    protected void TidyUp()
    {
        int end = GA_numChromesInit - 1;
        while (GA_pop.size() > GA_numChromes)
        {
            GA_pop.remove(end);
            end--;
        }
    }

    protected void Mutate()
    {
        int numMutate   = (int) (GA_numChromes * GA_mutFact);
        Random rnum     = new Random();

        for (int i = 0; i < numMutate; i++)
        {
            int chromMut = 1 + (rnum.nextInt(GA_numChromes - 1));
            int geneMutOne = rnum.nextInt(GA_numGenes); //index of the mutated gene
            int geneMutTwo = rnum.nextInt(GA_numGenes);
            Chromosome newChromosome = (Chromosome) GA_pop.remove(chromMut); //new chromosome from listS
            char tmp = newChromosome.GetGene(geneMutOne);
            newChromosome.SetGene(geneMutOne, newChromosome.GetGene(geneMutTwo));
            newChromosome.SetGene(geneMutTwo, tmp);//then mutate
            GA_pop.add(newChromosome); //put new chromosome at the end of the array
        }

    }

    protected void InitPop()
    {
        for (int index = 0; index < GA_numChromesInit; index++)
        {
            Chromosome Chrom = new Chromosome(GA_numGenes);
            ArrayList<Character> randomInit = new ArrayList<>(locations);
            Collections.shuffle(randomInit);
            for(int j = 0; j < randomInit.size(); j++){
                Chrom.SetGene(j,randomInit.get(j));
            }
            Chrom.SetCost(0);
            GA_pop.add(Chrom);
        }
    }
    protected abstract void ComputeCost();

    protected void Evolve()
    {
        int iterationCt = 0;
        Pair pairs      = new Pair(GA_pop);
        int numPairs    = pairs.SimplePair();
        boolean found   = false;
        Chromosome currBest = GA_pop.get(0);
        Chromosome prevBest = GA_pop.get(0);
        int numToConverge = 0;

        while (!hasConverged() && (iterationCt < GA_numIterations))
        {

            Mate mate = new Mate(GA_pop,GA_numGenes,GA_numChromes);
            if(GA_algorithm.equalsIgnoreCase("1")) {
                GA_pop = mate.TournamentCrossover(GA_pop,numPairs);
            }
            else if(GA_algorithm.equalsIgnoreCase("2")) {
                GA_pop = mate.DualPointCrossover(GA_pop,numPairs);
            }
            else if(GA_algorithm.equalsIgnoreCase("3")) {
                GA_pop = mate.TournamentDualPointCrossover(GA_pop,numPairs);
            }
            else
                GA_pop = mate.Crossover(GA_pop,numPairs);

            Mutate();

            ComputeCost();

            SortPop();

            //get best
            Chromosome chrome = GA_pop.get(0);

            System.out.println("Gen: " + iterationCt + " | Best: " + chrome.toString() + " | Lowest: " + chrome.GetCost());
            ++iterationCt;

        }
    }

    protected boolean hasConverged(){
        int sum = 0;
        double average = 0;
        if(!GA_pop.isEmpty()){ // if chromosome array list GA_pop is not empty
            for(Chromosome chrom: GA_pop){
                sum += chrom.GetCost();
            }
            average = (double) sum/GA_pop.size();
        }

        sum = 0;
        double stddev = 0;
        if(!GA_pop.isEmpty()){
            for (Chromosome chrom: GA_pop){
                sum += Math.pow((chrom.GetCost() - average), 2);
            }
            stddev = Math.sqrt( (double) sum / (GA_pop.size()));
        }

        if(!GA_pop.isEmpty()){
            double tmp = 0;
            for (Chromosome chrom : GA_pop){
                tmp = (double) Math.abs(chrom.GetCost() - average);
                if (tmp > stddev){
                    return false;
                }
            }
        }
        return true;
    }

}

