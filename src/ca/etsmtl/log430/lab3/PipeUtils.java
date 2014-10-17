package ca.etsmtl.log430.lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: maximebedard
 * Date: 2014-10-15
 * Time: 2:39 PM
 */
public final class PipeUtils {

    public interface ReadVisitor{
        void onRead(String line);
    }

    public static Collection<String> readAllLinesToContainer(PipedReader pipedReader, Class<?> classInfo) {

        final Collection<String> lines = new ArrayList<String>();

        readLines(pipedReader, new ReadVisitor() {
            @Override
            public void onRead(String line) {
                lines.add(line);
            }
        }, classInfo);

        return lines;
    }

    public static void readLines(PipedReader pipedReader, ReadVisitor visitor, Class<?> classInfo) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(pipedReader);

            String line;
            while ((line = reader.readLine()) != null) {
                FilterUtils.log(classInfo, "receiving " + line + ".");
                visitor.onRead(line);
            }
        } catch (IOException ex) {
            FilterUtils.log(classInfo, "Error receiving on pipe.");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                FilterUtils.log(classInfo, "Error closing buffered reader.");
            }
        }

    }

    public static void writeLines(PipedWriter pipedWriter, Collection<String> lines) {
        writeLines(pipedWriter, lines, null);
    }

    public static void writeLines(PipedWriter pipedWriter, Collection<String> lines, Class<?> classInfo)
    {
        for(String line: lines) {
            FilterUtils.log(classInfo, "sending " + line + ".");
            String lineWithCr = line + '\n';
            try {
                pipedWriter.write(lineWithCr, 0, lineWithCr.length());
                pipedWriter.flush();
            } catch (IOException e) {
                FilterUtils.log(classInfo, "Error sending on pipe.");
            }
        }
    }
}
