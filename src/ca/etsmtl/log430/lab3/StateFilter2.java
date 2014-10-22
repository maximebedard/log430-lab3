package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gislain Armand on 22/10/2014.
 */
public class StateFilter2 extends Filter {

    public enum Compare {
        Different,
        Equal
    }

    private Compare compare;
    private String stateValue;

    public StateFilter2(final String stateValue, final Compare compare, PipedWriter inputPipe, PipedWriter outputPipe)
    {
        super(inputPipe, outputPipe);
        this.compare = compare;
        this.stateValue = stateValue;
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

        for (String line : lines) {

            String state = StateFilter2.getStateForLine(line);

            if (this.compare.equals(Compare.Equal) && state.equals(this.stateValue)) {
                this.writeAll(line);
            } else if (this.compare.equals(Compare.Different) && !state.equals(this.stateValue)) {
                this.writeAll(line);
            }
        }
    }

    private static String getStateForLine(String line) {
        return line.split(" ")[5];
    }
}
