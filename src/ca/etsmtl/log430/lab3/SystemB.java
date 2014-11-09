package ca.etsmtl.log430.lab3;


import java.io.PipedWriter;

public class SystemB {


    public static void main(String[] args)
    {
        if(args.length < 3) {
            System.err.println("java SystemB <fichier d'entree> <fichier de sortie1> <fichier de sortie2>");
            return;
        }

        PipedWriter[] pipes = new PipedWriter[20];

        for(int i = 0; i < pipes.length; i++)
            pipes[i] = new PipedWriter();


        Thread[] threads = new Thread[]{
                new FileReaderFilter(args[0], pipes[0]),
                new StatusFilter(pipes[0], pipes[1], pipes[2]),

                // Status REG
                new SplitPipeFilter(pipes[1], pipes[3], pipes[4]),
                new AdvancedStateFilter("DIF", AdvancedStateFilter.SameComparator, pipes[3], pipes[5]),
                new AdvancedStateFilter("RIS", AdvancedStateFilter.SameComparator, pipes[4], pipes[6]),
                new MergeFilter(pipes[5], pipes[6], pipes[7]),
                new RateFilter(50, RateFilter.Lower, pipes[7], pipes[8]),
                new SortByStateFilter(pipes[8], pipes[9]),
                new FormatFilter(pipes[9], pipes[10]),
                new FileWriterFilter(args[1], pipes[10]),

                // Stauts CRI
                new SplitPipeFilter(pipes[2], pipes[11], pipes[14]),

                new AdvancedStateFilter("RIS", AdvancedStateFilter.SameComparator ,pipes[11], pipes[12]),
                new RateFilter(25, RateFilter.Equals, pipes[12], pipes[13]),

                new AdvancedStateFilter("RIS", AdvancedStateFilter.DifferentComparator, pipes[14], pipes[15]),
                new RateFilter(75, RateFilter.Greater, pipes[15], pipes[16]),

                new MergeFilter(pipes[13], pipes[16], pipes[17]),
                new SortByStateFilter(pipes[17], pipes[18]),
                new FormatFilter(pipes[18], pipes[19]),
                new FileWriterFilter(args[2], pipes[19])
        };

        for (Thread t : threads)
            t.start();



    }


}
