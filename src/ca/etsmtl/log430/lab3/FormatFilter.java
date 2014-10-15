package ca.etsmtl.log430.lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: maximebedard
 * Date: 2014-10-15
 * Time: 10:23 AM
 */
public class FormatFilter extends Thread {


    private final PipedReader inputPipe = new PipedReader();
    private PipedWriter outputPipe = new PipedWriter();

    public FormatFilter(PipedWriter inputPipe, PipedWriter outputPipe) {
        try {
            this.inputPipe.connect(inputPipe);
            this.outputPipe = outputPipe;
        } catch (Exception ex) {
            System.out.println("FormatFilter:: Error connecting to other filters.");
        }
    }


    @Override
    public void run() {


        // Declarations

        char[] characterValue = new char[1];
        // char array is required to turn char into a string
        String lineOfText = "";
        // string is required to look for the status code
        int integerCharacter; // the integer value read from the pipe

        try {

            while (true) {

                integerCharacter = inputPipe.read();
                characterValue[0] = (char) integerCharacter;

                if (integerCharacter == -1)
                    break;


                if (integerCharacter == '\n') { // end of line

                    System.out.println("FormatFilter:: received: " + lineOfText + ".");


                    String formatted = formatString(lineOfText) + '\n';


                    System.out.println("FormatFilter:: sending: " + formatted.substring(0, formatted.length() - 2) + ".");

                    outputPipe.write(formatted, 0, formatted.length());
                    outputPipe.flush();

                    lineOfText = "";

                }
                else {
                    lineOfText += new String(characterValue);
                }

            }
        }
        catch (IOException ex) {
            System.out.println("FormatFilter:: IO Exception.");
        }
        catch (Exception ex) {
            System.out.println("FormatFilter:: Unexpected Exception.");
        }

        closePipes();

    }

    private String formatString(String input) {

        String[] arr = input.split(" ");

           /*
        Numéro de projet nnnn Désignation du projet (ex : 0001)
        Statut aaa Statut du projet (REG : régulier ou CRI : critique)
        Système aaannn Système concerné par le projet (exemple : SYS001)
        Version nn.nn Version du système concerné (exemple : 01.01)
        Taux nn Taux de progression du projet (exemple : 90%)
        État aaa État du projet (PRO : le projet progresse PRO; DIF : le projet
        connait des difficultés; RIS : le projet présente des risques)
        Description
        */

        String no = arr[0];
        String statut = arr[1];
        String systeme = arr[2];
        String version = arr[3];
        String taux = arr[4];
        String etat = arr[5];


        String[] desc = Arrays.copyOfRange(arr, 6, arr.length - 1);
        String descStr = "";
        for(String s : desc) { descStr += s; }

        return String.format("%s %s %s %s", statut, etat, taux, no);
    }

    private void closePipes() {
        try {
            inputPipe.close();
            System.out.println("FormatFilter:: input pipe closed.");
            outputPipe.close();
            System.out.println("FormatFilter:: output pipes closed.");

        } catch (Exception Error) {
            System.out.println("FormatFilter:: Error closing pipes.");
        }

    }
}
