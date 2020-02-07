    1. Set parameters with command: java SetParams params.dat 128 64 8 .1 1000
     128 = Total population
     64 = Actual population
     8 = number of nodes (cities)
     .1 = mutation factor
     1000 = number of iterations to run this

    2. Run the program by entering:
        java TSPTst params.dat "abcdefgh" Matrix.csv 2

    // Argument 1: parameter file ( params.dat )
    // Argument 2: string of points in graph
    // Argument 3: matrix csv file ( Matrix.csv )
    // Argument 4: Pairing & Mating algorithm choice
    //             0 for Top-Down & Single-Point Crossover
    //             1 for Tournament & Single-Point Crossover
    //             2 for Top-Down & Dual-Point Crossover
    //             3 for Tournament & Dual-Point Crossover