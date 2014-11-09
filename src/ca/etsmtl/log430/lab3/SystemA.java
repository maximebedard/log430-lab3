package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;

public class SystemA {

    public static void main(String[] args) {

        if(args.length < 3) {
            System.err.println("java SystemA <fichier d'entree> <fichier de sortie1> <fichier de sortie2>");
            return;
        }


        PipedWriter[] pipes = new PipedWriter[20];

        for(int i = 0; i < pipes.length; i++)
            pipes[i] = new PipedWriter();

        Thread[] threads = new Thread[]{

            // Instantiate Filter Threads
            new FileReaderFilter(args[0], pipes[0]),

            //Split stream into two parts. S2 will keep track of original stream
            new SplitPipeFilter(pipes[0],pipes[1], pipes[7]),

            new StatusFilter(pipes[1], pipes[2], pipes[3]),
            new StateFilter("RIS", pipes[2], pipes[4]),
            new StateFilter("DIF", pipes[3], pipes[5]),
            new MergeFilter(pipes[4], pipes[5], pipes[6]),

            //Compare the Initial stream vs the modified Stream. Outputs the selected projects stream and another stream
            //where the projects were not included in the first stream
            new SelectedProjectsFilter(pipes[7], pipes[6], pipes[8], pipes[11]),

            //Filter, sort and write using the selected projects stream
            new SortByStateFilter(pipes[8], pipes[9]),
            new FormatFilter(pipes[9], pipes[10]),
            new FileWriterFilter(args[1], pipes[10]),

            //Filter, sort and write using the non-selected projects stream
            new SortByStateFilter(pipes[11], pipes[12]),
            new FormatFilter(pipes[12], pipes[13]),
            new FileWriterFilter(args[2], pipes[13])
        };

        for(Thread t: threads)
            t.start();


    }


}
