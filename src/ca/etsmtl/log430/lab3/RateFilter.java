package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gislain Armand on 22/10/2014.
 */
public class RateFilter extends Filter {

    public enum Compare {
        Upper,
        Lower,
        Equal
    }

    private Compare compare;
    private String rateValue;

    public RateFilter(final String rateValue, final Compare compare, PipedWriter inputPipe, PipedWriter outputPipe) {
        super(inputPipe, outputPipe);
        this.compare = compare;
        this.rateValue = rateValue;
    }

    @Override
    public void execute()
    {
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
                return RateFilter.getRateForLine(o1).compareTo(RateFilter.getRateForLine(o2));
            }
        });

        for(String line : lines)
        {
            String rate = RateFilter.getRateForLine(line);
            if (this.compare.equals(Compare.Equal) && rate.equals(this.rateValue))
            {
                this.writeAll(line);
            }
            else if (this.compare.equals(Compare.Lower) && rate.compareTo(this.rateValue) < 0)
            {
                this.writeAll(line);
            }
            else if (this.compare.equals(Compare.Upper) && rate.compareTo(this.rateValue) > 0)
            {
                this.writeAll(line);
            }
        }
    }

    private static String getRateForLine(String line) {
        return line.split(" ")[4];
    }
}
