package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gislain Armand on 22/10/2014.
 */
public class SplitPipeFilter extends Filter {

    public SplitPipeFilter(final PipedWriter inputPipe, final PipedWriter outputPipe1, final PipedWriter outputPipe2)
    {
        super(new ArrayList<PipedWriter>() {{
            add(inputPipe);
        }},new ArrayList<PipedWriter>() {{
            add(outputPipe1);
            add(outputPipe2);
        }});
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
