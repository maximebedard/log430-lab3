package ca.etsmtl.log430.lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: maximebedard
 * Date: 2014-10-15
 * Time: 10:23 AM
 */
public class FormatFilter extends Thread {


    private final PipedReader inputPipe = new PipedReader();
    private PipedWriter outputPipe = new PipedWriter();

    public FormatFilter(PipedWriter inputPipe, PipedWriter outputPipe) {
        try {
            this.inputPipe.connect(inputPipe);
            this.outputPipe = outputPipe;
        } catch (Exception ex) {
            FilterUtils.log(FormatFilter.class, "Error connecting to other filters.");
        }
    }


    @Override
    public void run() {


        PipeUtils.readLines(inputPipe, new PipeUtils.ReadVisitor() {
            @Override
            public void onRead(String line) {

                String formatted = formatString(line) + '\n';

                FilterUtils.log(FormatFilter.class, "sending: " + formatted.substring(0, formatted.length() - 2) + ".");

                try {
                    outputPipe.write(formatted, 0, formatted.length());
                    outputPipe.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, FormatFilter.class);


        closePipes();

    }

    private String formatString(String input) {

        String[] arr = input.split(" ");

        String no = arr[0];
        String statut = arr[1];
        String taux = arr[4];
        String etat = arr[5];

        return String.format("%s %s %s %s", statut, etat, taux, no);
    }

    private void closePipes() {
        try {
            inputPipe.close();
            FilterUtils.log(FormatFilter.class, "input pipe closed.");

            outputPipe.close();
            FilterUtils.log(FormatFilter.class, "output pipe closed.");

        } catch (Exception Error) {
            FilterUtils.log(FormatFilter.class, "Error closing pipes.");
        }

    }
}
