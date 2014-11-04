package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;
import java.util.ArrayList;

/**
 * User: xavier
 * Date: 2014-10-15
 * Time: 12:08 PM
 */
public class SelectedProjectsFilter extends Filter {

    private PipedWriter outputSelectedProjectPipe  = new PipedWriter();
    private PipedWriter outputNotSelectedProjectPipe  = new PipedWriter();

    public SelectedProjectsFilter(final PipedWriter orignalPipe, final PipedWriter filteredPipe,
                                  final PipedWriter outputSelectedProjectPipe, final PipedWriter outputNotSelectedProjectPipe) {

        super(new ArrayList<PipedWriter>() {{
                  add(orignalPipe);
                  add(filteredPipe);
              }},new ArrayList<PipedWriter>() {{
                  add(outputSelectedProjectPipe);
                  add(outputNotSelectedProjectPipe);
              }});


        this.outputSelectedProjectPipe = outputSelectedProjectPipe;
        this.outputNotSelectedProjectPipe = outputNotSelectedProjectPipe;

    }

    @Override
    public void execute() {

        final ArrayList<String> lines = new ArrayList<String>();
        ArrayList<String> selectedProjects = new ArrayList<String>();
        this.readAll(new FilterVisitor() {
            @Override
            public void onRead(String line) {
                lines.add(line);
            }
        });

        int findCount = 0;
        for(String line : lines)
        {
            for(String line2 : lines)
            {
                if(getProjectIdForLine(line).compareToIgnoreCase(getProjectIdForLine(line2)) == 0){
                    findCount++;
                }
            }

            if(findCount < 2){
                this.write(outputNotSelectedProjectPipe,line);
            } else if(!selectedProjects.contains(line)) {
                selectedProjects.add(line);
                this.write(outputSelectedProjectPipe,line);
            }
            findCount = 0;
        }


    }

    private static String getProjectIdForLine(String line) {
        return line.split(" ")[0];
    }
}
