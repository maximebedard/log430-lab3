package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gislain Armand on 22/10/2014.
 */
public class RateFilter extends Filter {

    private final int rate;
    private EqualityComparator<Integer> compare;

    public static final EqualityComparator<Integer> Equals = new EqualityComparator<Integer>() {
        @Override
        public boolean test(Integer lhs, Integer rhs) {
            return lhs.equals(rhs);
        }
    };

    public static final EqualityComparator<Integer> Lower = new EqualityComparator<Integer>() {
        @Override
        public boolean test(Integer lhs, Integer rhs) {
            return lhs < rhs;
        }
    };

    public static final EqualityComparator<Integer> Greater = new EqualityComparator<Integer>() {
        @Override
        public boolean test(Integer lhs, Integer rhs) {
            return lhs > rhs;
        }
    };

    public static final EqualityComparator<Integer> LowerEqual = new EqualityComparator<Integer>() {
        @Override
        public boolean test(Integer lhs, Integer rhs) {
            return lhs <= rhs;
        }
    };

    public static final EqualityComparator<Integer> GreaterEqual = new EqualityComparator<Integer>() {
        @Override
        public boolean test(Integer lhs, Integer rhs) {
            return lhs >= rhs;
        }
    };


    public RateFilter(int rate, EqualityComparator<Integer> compare, PipedWriter inputPipe, PipedWriter outputPipe) {
        super(inputPipe, outputPipe);
        this.rate = rate;
        this.compare = compare;

    }

    @Override
    public void execute()
    {
        readAll(new FilterVisitor() {
            @Override
            public void onRead(String line) {
                if(compare.test(getRateForLine(line), rate))
                    writeAll(line);
            }
        });
    }

    private static int getRateForLine(String line) {
        return Integer.parseInt(line.split(" ")[4]);
    }
}
