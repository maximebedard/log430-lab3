package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;

public class AdvancedStateFilter extends Filter {

    private final String state;
    private final EqualityComparator<String> comparator;

    public static final EqualityComparator<String> SameComparator = new EqualityComparator<String>() {
        @Override
        public boolean test(String lhs, String rhs) {
            return lhs.equals(rhs);
        }
    };

    public static final EqualityComparator<String> DifferentComparator = new EqualityComparator<String>() {
        @Override
        public boolean test(String lhs, String rhs) {
            return !lhs.equals(rhs);
        }
    };

    public AdvancedStateFilter(String state, EqualityComparator<String> comparator, PipedWriter inputPipe, PipedWriter outputPipe) {
        super(inputPipe, outputPipe);
        this.state = state;
        this.comparator = comparator;
    }

    @Override
    public void execute() {
        readAll(new FilterVisitor() {
            @Override
            public void onRead(String line) {
                if(comparator.test(state, getStateForLine(line)))
                    writeAll(line);
            }
        });
    }

    private static String getStateForLine(String line) {
        return line.split(" ")[5];
    }

}
