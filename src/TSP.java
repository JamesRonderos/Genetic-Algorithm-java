import java.util.*;
import java.io.*;

public class TSP extends GA {

    private String WG_target;


    private Map < Character, Map < Character, Integer >> costMatrix;

    // NxN matrix
    public TSP(String fileNameParams, String target, String filenameMatrix, String algo) {
        super(fileNameParams, target, algo);
        WG_target = target;
        GA_algorithm = algo;
        GA_numGenes = WG_target.length();

        costMatrix = new HashMap < > ();
        try {
            BufferedReader infile = new BufferedReader(new FileReader(filenameMatrix));
            String line = infile.readLine();
            String[] indexes = line.split(",");
            this.locations = new ArrayList < > ();
            for (String index: indexes) {
                this.locations.add(Character.valueOf(index.charAt(0)));
            }

            // Continue to end of file,
            int lineNumber = 0;
            while ((line = infile.readLine()) != null) {
                String[] costs = line.split(",");
                HashMap < Character, Integer > innerMap = new HashMap < > ();
                // For each number in the line,
                for (int i = 0; i < costs.length; i++) {
                    innerMap.put(locations.get(i), Integer.valueOf(costs[i]));
                }
                costMatrix.put(locations.get(lineNumber), innerMap);
                lineNumber++;
            }
        } catch (java.lang.Exception exception) {
            exception.printStackTrace();
        }
        super.DisplayParams();
        InitPop();
    }

    public void InitPop() {
        super.InitPop();
        ComputeCost();
        SortPop();
        TidyUp();
    }

    public void DisplayParams() {
        System.out.print("Target: ");
        System.out.println(WG_target);
        super.DisplayParams();
    }

    protected void ComputeCost() {
        for (int i = 0; i < GA_pop.size(); i++) {
            int cost = 0;
            Chromosome chrom = GA_pop.remove(i);
            for (int j = 0; j < GA_numGenes; j++) {
                char start = chrom.GetGene(j);
                char end = chrom.GetGene((j + 1) % chrom.GetNumGenes());
                cost += costMatrix.get(start).get(end);
            }

            chrom.SetCost(cost);
            GA_pop.add(i, chrom);
        }

    }

}