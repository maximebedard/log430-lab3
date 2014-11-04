package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;

/**
 * This class contains the main method for assignment 3. The program
 * consists of these files:<br><br>
 * 
 * 1) SystemInitialize: instantiates all filters and pipes, starts all filters.<br>
 * 2) FileReaderFilter: reads input file and sends each line to its output pipe.<br>
 * 3) StatusFilter: separates the input stream in two project types (REG, CRI) and writes
 *    lines to the appropriate output pipe.<br>
 * 4) StateFilter: determines if an entry contains a particular state specified
 *    at instantiation. If so, sends the whole line to its output pipe.<br>
 * 5) MergeFilter: accepts inputs from 2 input pipes and writes them to its output pipe.<br>
 * 6) FileWriterFilter: sends its input stream to a text file.<br><br>
 * 
 * Pseudo Code:
 * <pre>
 * instantiate all filters and pipes
 * start FileReaderFilter
 * start StatusFilter
 * start StateFilter for RIS
 * start StateFilter for DIF
 * start MergeFilter
 * start FileWriterFilter
 * </pre>
 * 
 * Running the program:
 * <pre>
 * java SystemInitialize InputFile OutputFile > DebugFile
 * 
 * SystemInitialize - Program name
 * InputFile - Text input file (see comments below)
 * OutputFile - Text output file with students
 * DebugFile - Optional file to direct debug statements
 * </pre>
 * 
 * @author A.J. Lattanze
 */

public class SystemInitialize {

	public static void main(String argv[]) {
		// Let's make sure that input and output files are provided on the
		// command line

		if (argv.length != 2) {

			System.out
					.println("\n\nNombre incorrect de parametres d'entree. Utilisation:");
			System.out
					.println("\njava SystemInitialize <fichier d'entree> <fichier de sortie>");

		} else {
            SystemInitialize.SystemA(argv[0], SystemInitialize.GenerateOutputFileName(argv[1], "SA"));
            SystemInitialize.SystemB(argv[0], SystemInitialize.GenerateOutputFileName(argv[1], "SB"));
        }  // if
		
	} // main

    private static void SystemA(String inputFile, String outputFile)
    {
        // These are the declarations for the pipes.

        //Entry pipe
        PipedWriter entry = new PipedWriter();

        //Stream 1
        PipedWriter pipe01 = new PipedWriter();
        PipedWriter pipe02 = new PipedWriter();
        PipedWriter pipe03 = new PipedWriter();
        PipedWriter pipe04 = new PipedWriter();
        PipedWriter pipe05 = new PipedWriter();
        PipedWriter pipe06 = new PipedWriter();
        PipedWriter pipe07 = new PipedWriter();
        PipedWriter pipe08 = new PipedWriter();
        PipedWriter pipe09 = new PipedWriter();

        //Stream 2
        PipedWriter s2pipe01 = new PipedWriter();
        PipedWriter s2pipe02 = new PipedWriter();
        PipedWriter s2pipe03 = new PipedWriter();
        PipedWriter s2pipe04 = new PipedWriter();

        // Instantiate Filter Threads
        Thread fileReaderFilter = new FileReaderFilter(inputFile, entry);

        //Split stream into two parts. S2 will keep track of original stream
        Thread splitPipe = new SplitPipeFilter(entry,pipe01, s2pipe01);

        Thread statusFilter = new StatusFilter(pipe01, pipe02, pipe03);
        Thread stateFilter1 = new StateFilter("RIS", pipe02, pipe04);
        Thread stateFilter2 = new StateFilter("DIF", pipe03, pipe05);
        Thread mergeFilter = new MergeFilter(pipe04, pipe05, pipe06);

        //Compare the Initial stream vs the modified Stream. Outputs the selected projects stream and another stream
        //where the projects were not included in the first stream
        Thread selectedProjectsFilter = new SelectedProjectsFilter(s2pipe01, pipe06, pipe07, s2pipe02);

        //Filter, sort and write using the selected projects stream
        Thread sortFilter = new SortByStateFilter(pipe07, pipe08);
        Thread formatFilter = new FormatFilter(pipe08, pipe09);
        Thread fileWriterFilter = new FileWriterFilter(outputFile, pipe09);

        //Filter, sort and write using the non-selected projects stream
        Thread sortFilter2 = new SortByStateFilter(s2pipe02, s2pipe03);
        Thread formatFilter2 = new FormatFilter(s2pipe03, s2pipe04);
        Thread fileWriterFilter2 = new FileWriterFilter("notselected-"+outputFile, s2pipe04);


        // Start the threads
        fileReaderFilter.start();
        splitPipe.start();
        statusFilter.start();
        stateFilter1.start();
        stateFilter2.start();
        mergeFilter.start();
        selectedProjectsFilter.start();
        sortFilter.start();
        formatFilter.start();
        sortFilter2.start();
        formatFilter2.start();
        fileWriterFilter.start();
        fileWriterFilter2.start();

        try {
            fileWriterFilter.join();
            fileWriterFilter2.join();
        }catch (InterruptedException ex)
        {

        }


    }

    private static void SystemB(String inputFile, String outputFile)
    {
        SystemInitialize.SystemB1(inputFile, SystemInitialize.GenerateOutputFileName(outputFile, "1"));
        SystemInitialize.SystemB2(inputFile, SystemInitialize.GenerateOutputFileName(outputFile, "2"));
    }

    private static void SystemB1(String inputFile, String outputFile)
    {
        // These are the declarations for the pipes.
        PipedWriter pipe01 = new PipedWriter();
        PipedWriter pipe02 = new PipedWriter();
        PipedWriter pipe03 = new PipedWriter();
        PipedWriter pipe04 = new PipedWriter();
        PipedWriter pipe05 = new PipedWriter();
        PipedWriter pipe06 = new PipedWriter();
        PipedWriter pipe07 = new PipedWriter();
        PipedWriter pipe08 = new PipedWriter();
        PipedWriter pipe09 = new PipedWriter();


        // Instantiate Filter Threads
        Thread fileReaderFilter = new FileReaderFilter(inputFile, pipe01);
        Thread statusFilter = new StatusFilter2("REG", pipe01, pipe02);
        Thread rateFilter = new RateFilter("50", RateFilter.Compare.Lower, pipe02, pipe03);
        Thread splitPipeFilter = new SplitPipeFilter(pipe03, pipe05, pipe06);
        Thread stateFilter1 = new StateFilter("RIS", pipe05, pipe07);
        Thread stateFilter2 = new StateFilter("DIF", pipe06, pipe08);
        Thread mergeFilter = new MergeFilter(pipe07, pipe08, pipe09);
        Thread fileWriterFilter = new FileWriterFilter(outputFile, pipe09);

        // Start the threads
        fileReaderFilter.start();
        rateFilter.start();
        statusFilter.start();
        splitPipeFilter.start();
        stateFilter1.start();
        stateFilter2.start();
        mergeFilter.start();
        fileWriterFilter.start();

        try {
            fileWriterFilter.join();
        }catch (InterruptedException ex)
        {

        }
    }

    private static void SystemB2(String inputFile, String outputFile)
    {
        // These are the declarations for the pipes.
        PipedWriter pipe01 = new PipedWriter();
        PipedWriter pipe02 = new PipedWriter();
        PipedWriter pipe03 = new PipedWriter();
        PipedWriter pipe04 = new PipedWriter();
        PipedWriter pipe05 = new PipedWriter();
        PipedWriter pipe06 = new PipedWriter();
        PipedWriter pipe07 = new PipedWriter();
        PipedWriter pipe08 = new PipedWriter();
        PipedWriter pipe09 = new PipedWriter();


        // Instantiate Filter Threads
        Thread fileReaderFilter = new FileReaderFilter(inputFile, pipe01);
        Thread statusFilter = new StatusFilter2("CRI", pipe01, pipe02);
        Thread splitPipeFilter = new SplitPipeFilter(pipe02, pipe03, pipe04);

        Thread stateFilter1 = new StateFilter2("RIS", StateFilter2.Compare.Equal, pipe03, pipe05);
        Thread rateFilter1 = new RateFilter("25", RateFilter.Compare.Equal, pipe05, pipe06);

        Thread stateFilter2 = new StateFilter2("RIS", StateFilter2.Compare.Different, pipe04, pipe07);
        Thread rateFilter2 = new RateFilter("75", RateFilter.Compare.Upper, pipe07, pipe08);

        Thread mergeFilter = new MergeFilter(pipe06, pipe08, pipe09);
        Thread fileWriterFilter = new FileWriterFilter(outputFile, pipe09);

        // Start the threads
        fileReaderFilter.start();
        statusFilter.start();
        splitPipeFilter.start();
        stateFilter1.start();
        rateFilter1.start();
        stateFilter2.start();
        rateFilter2.start();
        mergeFilter.start();
        fileWriterFilter.start();

        try {
            fileWriterFilter.join();
        }catch (InterruptedException ex)
        {

        }
    }

    private static String GenerateOutputFileName(String fileName, String systemName)
    {
        String[] splitFileName = fileName.split("[.]");
        return splitFileName[0] + systemName + "." + splitFileName[1];
    }
	
} // SystemInitialize