package ca.etsmtl.log430.lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: maximebedard
 * Date: 2014-10-15
 * Time: 10:23 AM
 */
public class FormatFilter extends Filter {


    public FormatFilter(final PipedWriter inputPipe, final PipedWriter outputPipe) {
        super(inputPipe, outputPipe);
    }

    private String formatString(String input) {
        String[] arr = input.split(" ");
        String no = arr[0];
        String statut = arr[1];
        String taux = arr[4];
        String etat = arr[5];
        return String.format("%s %s %s %s", statut, etat, taux, no);
    }

    @Override
    public void execute() {

        readAll(new FilterVisitor() {
            @Override
            public void onRead(String line) {
                writeAll(formatString(line));
            }
        });

    }
}
