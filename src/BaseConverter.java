import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Converts numbers from a range of base 2 to base 16 to a new number
 * @author Alex Ru
 * @version 12.02.23
 * extra at lines 19-37, 82-95, 128-132
 * Cameron Morris and https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html helped me with the extra
 */
public class BaseConverter {
    private final String NUMS = "0123456789ABCDEF";
    File file;
    PrintWriter newFile;

    private void fileChooserPrompt() {
        final JFileChooser fc = new JFileChooser();

        fc.setCurrentDirectory(new File(System.getProperty("user.home") + "/IdeaProjects/Project02.01-Base Converter"));
        fc.setDialogTitle("Choose a .dat file");
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .dat files", "dat");
        fc.setFileFilter(restrict);
        fc.addChoosableFileFilter(restrict);

        int r = fc.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }
    }

    private int againChooserPrompt() {
        JFrame frame = new JFrame("Again?");
        return JOptionPane.showConfirmDialog(frame, "Would you like to convert another one?");
    }

    /**
     * Convert a String num in fromBase to base-10 int
     * @param num      base fromBase number
     * @param fromBase starting base
     * @return base-10 number
     */
    public int strToInt(String num, String fromBase) {
        int res = 0;
        // go through num from right to left and multiply num by power
        for (int i = 0; i < num.length(); i++) {
            res += (int) (NUMS.indexOf(num.charAt(num.length() - i - 1)) * Math.pow(Integer.parseInt(fromBase), i));
        }
        return res;
    }

    /**
     * Convert a base-10 int to a String number of base toBase
     * @param num    base-10 number
     * @param toBase ending base
     * @return base toBase number
     */
    public String intToStr(int num, int toBase) {
        String res = "";
        // if num is 0
        if (num == 0) {
            return "0";
        }
        // mod num by base and divide num by base
        while (num > 0) {
            res = NUMS.charAt(num % toBase) + res;
            num /= toBase;
        }
        return res;
    }

    /**
     * Opens the file stream, inputs data one line at a time,
     * converts, prints the result to the console window
     * and writes data to the output stream.
     */
    public void inputConvertPrintWrite() {
        String startNum, startBase;
        int endBase;
        int again, count = 0;
        while (true) {
            fileChooserPrompt();
            again = againChooserPrompt();
            try {
                // if user presses cancel aka chose wrong file and doesn't want to convert
                if (again == 2)
                    continue;
                // first file shouldn't be called "converted0.dat"
                if (count == 0)
                    newFile = new PrintWriter("converted.dat");
                // after first file, called "converted1.dat", etc.
                else
                    newFile = new PrintWriter("converted" + count + ".dat");

                // convert file (lines 98-125)
                Scanner in = new Scanner(file);
                String[] temp;
                // for each line, convert and write
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    temp = line.split("\t");

                    // use temp to find numbers in each column
                    startNum = temp[0];
                    startBase = temp[1];
                    endBase = Integer.parseInt(temp[2]);

                    // if startBase is outside of base range
                    if (Integer.parseInt(startBase) < 2 || Integer.parseInt(startBase) > 16)
                        System.out.println("Invalid input base " + startBase);
                    // if endBase is outside of base range
                    else if (endBase < 2 || endBase > 16)
                        System.out.println("Invalid output base " + endBase);
                    // convert num to res, print out info, write to file
                    else {
                        String res = intToStr(strToInt(startNum, startBase), endBase);
                        System.out.println(startNum + " base " + startBase + " = " + res + " base " + endBase);
                        newFile.println(startNum + "\t" + startBase + "\t" + res + "\t" + endBase);
                    }
                }
                // close file and converted file
                in.close();
                newFile.close();

                // if user presses no aka don't convert another file
                if (again == 1)
                    break;

                // otherwise, user will continue to convert other files
                count++;
            } catch (Exception e) {
                System.out.println("Something went wrong.");
            }
        }
    }

    /**
     * Main entry point of BaseConverter class
     * @param args command line arguments if necessary
     */
    public static void main(String[] args) {
        BaseConverter app = new BaseConverter();
        app.inputConvertPrintWrite();
    }
}