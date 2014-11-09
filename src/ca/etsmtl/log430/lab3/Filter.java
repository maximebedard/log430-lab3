package ca.etsmtl.log430.lab3;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class Filter extends Thread {

    public interface FilterVisitor{
        void onRead(String line);
    }

    private final ArrayList<PipedReader> inputPipes = new ArrayList<PipedReader>();
    private final ArrayList<PipedWriter> outputPipes = new ArrayList<PipedWriter>();

    public Filter(Collection<PipedWriter> inputPipes, Collection<PipedWriter> outputPipes) {
        for(PipedWriter inputPipe: inputPipes) {
            PipedReader pipedReader = new PipedReader();
            try {
                pipedReader.connect(inputPipe);
            } catch (IOException e) {
                log("Error connecting to pipe");
            }
            this.inputPipes.add(pipedReader);
        }

        for(PipedWriter outputPipe: outputPipes)
            this.outputPipes.add(outputPipe);
    }

    public Filter(PipedWriter inputPipe, PipedWriter outputPipe) {
        this(Arrays.asList(inputPipe), Arrays.asList(outputPipe));
    }

    private void closePipes(){
        for(PipedReader inputPipe : inputPipes) {
            try {
                log("Closing pipe reader.");
                inputPipe.close();
            } catch (IOException e) {
                log("Error closing pipe reader.");
            }
        }

        for(PipedWriter outputPipe : outputPipes) {
            try {
                log("Closing pipe writer.");
                outputPipe.close();
            } catch (IOException e) {
                log("Error closing pipe writer.");
            }
        }
    }

    public Collection<PipedReader> getInputPipes() {return inputPipes;}

    public Collection<PipedWriter> getOutputPipes() {return outputPipes;}

    public abstract void execute();

    @Override
    public void run() {

        execute();

        closePipes();
    }

    /**
     * Read pipedReader line and calls the visitor for each line read
     * @param pipedReader
     * @param visitor
     */
    protected void read(PipedReader pipedReader, FilterVisitor visitor) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(pipedReader);

            String line;
            while ((line = reader.readLine()) != null) {
                log("receiving " + line + ".");
                visitor.onRead(line);
            }
        } catch (IOException ex) {
            log("Error receiving on pipe.");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log("Error closing buffered reader.");
            }
        }
    }

    /**
     * Read all the input pipes and calls the visitor for each line read
     * @param visitor
     */
    protected void readAll(FilterVisitor visitor) {
        for(PipedReader inputPipe : inputPipes)
            read(inputPipe, visitor);
    }


    /**
     * Write a string to all the output pipes
     * @param output
     */
    protected void writeAll(String output) {
        for(PipedWriter outputPipe : outputPipes)
            write(outputPipe, output);
    }

    protected void write(PipedWriter pipedWriter, String output) {

        log("sending " + output + ".");

        String copy = output;

        if(!copy.endsWith("\n"))
            copy += "\n";

        try {
            pipedWriter.write(copy, 0, copy.length());
            pipedWriter.flush();
        } catch (IOException e) {
            log("Error sending on pipe.");
        }

    }

    protected void log(String msg) {
        System.out.println(this.getClass().getSimpleName() + "::" + msg + ".");
    }

}
