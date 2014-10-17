package ca.etsmtl.log430.lab3;

/**
 * User: maximebedard
 * Date: 2014-10-15
 * Time: 3:14 PM
 */
public class FilterUtils {
    protected static void log(Class<?> caller, String msg) {
        if(caller != null)
            System.out.println(String.format("%s::%s", caller.getSimpleName(), msg));
    }
}
