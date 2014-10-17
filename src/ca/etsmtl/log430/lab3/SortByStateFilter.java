package ca.etsmtl.log430.lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.*;

/**
 * User: maximebedard
 * Date: 2014-10-15
 * Time: 12:08 PM
 */
public class SortByStateFilter extends Filter {

    public SortByStateFilter(PipedWriter inputPipe, PipedWriter outputPipe) {
        super(inputPipe, outputPipe);
    }

    @Override
    public void execute() {
        final ArrayList<String> lines = new ArrayList<String>();

        readAll(new FilterVisitor() {
            @Override
            public void onRead(String line) {
                lines.add(line);
            }
        });

        Collections.sort(lines, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return getStateForLine(o1).compareTo(getStateForLine(o2));
            }
        });
        for(String line : lines)
            writeAll(line);
    }

    private static String getStateForLine(String line) {
        return line.split(" ")[5];
    }
}
