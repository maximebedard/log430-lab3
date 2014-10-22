package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gislain Armand on 22/10/2014.
 */
public class StatusFilter2 extends Filter {

    private String status;
    public  StatusFilter2(String status, PipedWriter inputPipe, PipedWriter outputPipe)
    {
        super(inputPipe, outputPipe);

        this.status = status;
    }

    @Override
    public void execute() {
        final ArrayList<String> lines = new ArrayList<String>();
        this.readAll(new FilterVisitor() {
            @Override
            public void onRead(String line) {
                lines.add(line);
            }
        });

        for(String line : lines)
        {
            if (StatusFilter2.getStatusForLine(line).equals(this.status)) {
                this.writeAll(line);
            }
        }
    }

    private static String getStatusForLine(String line) {
        return line.split(" ")[1];
    }
}
