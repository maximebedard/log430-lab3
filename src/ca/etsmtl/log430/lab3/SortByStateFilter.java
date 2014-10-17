package ca.etsmtl.log430.lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

/**
 * User: maximebedard
 * Date: 2014-10-15
 * Time: 12:08 PM
 */
public class SortByStateFilter extends Thread {


    private final PipedReader inputPipe = new PipedReader();
    private PipedWriter outputPipe = new PipedWriter();

    public SortByStateFilter(PipedWriter inputPipe, PipedWriter outputPipe)
    {

        try {
            this.inputPipe.connect(inputPipe);
            this.outputPipe = outputPipe;
        } catch (IOException e) {
            FilterUtils.log(SortByStateFilter.class, "Error connecting to other filters.");
        }

    }



    @Override
    public void run() {

        // Block to read all the lines in the pipe
        Collection<String> lines = PipeUtils.readAllLinesToContainer(inputPipe, SortByStateFilter.class);

        // Create TreeMap to sort items based on their state
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        for(String line: lines)
        {
            treeMap.put(getStateForLine(line), line);
        }

        // Forward the sorted elements to the other pipe
        PipeUtils.writeLines(outputPipe, treeMap.values(), SortByStateFilter.class);

        closePipes();
    }

    private void closePipes() {
        try {
            inputPipe.close();
            FilterUtils.log(SortByStateFilter.class, "input pipe closed.");
            outputPipe.close();
            FilterUtils.log(SortByStateFilter.class, "input pipe closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStateForLine(String line) {
        return line.split(" ")[5];
    }





}
