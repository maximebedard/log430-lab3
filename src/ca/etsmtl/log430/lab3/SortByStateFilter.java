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
public class SortByStateFilter extends Filter {

    public SortByStateFilter(PipedWriter inputPipe, PipedWriter outputPipe) {
        super(inputPipe, outputPipe);
    }

    @Override
    public void execute() {

        final TreeMap<String, String> treeMap = new TreeMap<String, String>();

        readAll(new FilterVisitor() {
            @Override
            public void onRead(String line) {
                treeMap.put(getStateForLine(line), line);
            }
        });

        for(String val : treeMap.values())
            writeAll(val);

    }

    private static String getStateForLine(String line) {
        return line.split(" ")[5];
    }

}
