//Class: CPSC 427
//Team Member 1: James Ronderos
//To run: java TSPTst params.dat "abcdefgh" Matrix.csv 0


import java.lang.*;

public class TSPTst
{

    public static void main(String args[])
    {
        // Argument 1: parameter file ( params.dat )
        // Argument 2: string of points in graph
        // Argument 3: matrix csv file ( Matrix.csv )
        // Argument 4: Pairing & Mating algorithm choice
        //             0 for Top-Down & Single-Point Crossover
        //             1 for Tournament & Single-Point Crossover
        //             2 for Top-Down & Dual-Point Crossover
        //             3 for Tournament & Dual-Point Crossover

        TSP GA = new TSP(args[0],args[1],args[2],args[3]);

        System.out.println();
        //GA.DisplayParams(); // Uncomment to display the contents of the parameter file
        //GA.DisplayPop(); // Uncomment to display the population before evolution
        GA.Evolve();
        //GA.DisplayPop(); // Uncomment to display the population after evolution
        System.out.println();
    }
}