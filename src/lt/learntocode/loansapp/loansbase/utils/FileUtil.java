package lt.learntocode.loansapp.loansbase.utils;

import java.io.*;

public class FileUtil {

    public static boolean writeFile(String string, File file) { // could pass file instead of String of FILENAME
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)))) {
            out.println(string);
            return true;
        } catch (IOException e) {
            System.err.println("ERROR: Caught IOException while trying to write a CSV formatted FILE with loans data: \n" + e.getMessage());
            return false;
        }
    }

    public static BufferedReader readFile(File file) { // could pass file instead of String of FILENAME
        try {
//            return new BufferedReader(new InputStreamReader(new FileInputStream(file))); // kuo skiriasi nuo apatinio varianto?
            return new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            System.err.println("ERROR: Caught IOException while trying to read CSV formatted FILE with loans data: \n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
