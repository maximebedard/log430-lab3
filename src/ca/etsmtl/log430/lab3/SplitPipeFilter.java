package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Gislain Armand on 22/10/2014.
 */
public class SplitPipeFilter extends Filter {

    public SplitPipeFilter(final PipedWriter inputPipe, final PipedWriter outputPipe1, final PipedWriter outputPipe2)
    {
        super(Arrays.asList(inputPipe),Arrays.asList(outputPipe1, outputPipe2));
    }

    @Override
    public void execute() {

        readAll(new FilterVisitor() {
            @Override
            public void onRead(String line) {
                writeAll(line);
            }
        });
    }
}
