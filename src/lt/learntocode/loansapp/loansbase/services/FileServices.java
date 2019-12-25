package lt.learntocode.loansapp.loansbase.services;

import lt.learntocode.loansapp.loansbase.utils.FileUtil;
import lt.learntocode.loansapp.loansbase.model.Loan;
import lt.learntocode.loansapp.loansbase.helpers.LoansCalculatorHelper;
import lt.learntocode.loansapp.loansbase.model.LoansData;

import java.io.*;

public class FileServices {

    private static final String MAIN_FILE_NAME = "resources/loansData.txt";
    private static final File FILE_TO_SAVE = new File(MAIN_FILE_NAME);
    private static final File FILE_TO_LOAD = new File(MAIN_FILE_NAME);
    private static final String PAYMENTS_FILES_FOLDER = "resources/paymentsSchedules/";

    // method to save loansData array objects as CSV string lines into the file
    public boolean saveLoansData(LoansData loansData) {
        String loansDataString = loansData.toCSVStrings(); // get loan[] as CSV strings
        // save loan[] and paymentsSchedule[] to separate files inside resources and paymentsSchedules folders
        if (saveStringToFile(loansDataString, FILE_TO_SAVE) && savePaymentsScheduleToFile(loansData)) {
            System.out.println("New data successfully written to a FILE.");
            // if both operations true
            return true;
        }
        // return false if any of save operations fails to write data to file
        return false;
    }

    // method to save paymentsSchedule[] array objects as CSV string lines into the file
    public boolean savePaymentsScheduleToFile(LoansData loansData) {
        int loanIndex = loansData.getLoansDataRecordsCounter() - 1;
        File file = new File((PAYMENTS_FILES_FOLDER + "loan_" + (loanIndex + 1) + ".txt")); // recourses/paymentsSchedules/loan_1.txt
        Loan loan = loansData.getLoan(loanIndex);
        String paymentsScheduleString = loan.toCSVStrings();
        if (saveStringToFile(paymentsScheduleString, file)) {
            return true;
        }
        return false;
    }

    // method to save string lines into the file
    private boolean saveStringToFile(String string, File file) {
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    if (FileUtil.writeFile(string, file)) {
                        // return true if String data written to file successfully
                        return true;
                    } else {
                        System.err.println("ERROR: Failed to save data into a \"" + string + "\" FILE");
                        return false;
                    }
                } else {
                    System.err.println("ERROR: Failed to create and save data into a \"" + string + "\" FILE");
                    return false;
                }
            } catch (IOException e) {
                System.err.println("ERROR: Caught IOException while trying to create and save data into a \"" + string + "\" FILE: \n" + e.getMessage());
                return false;
            }
        } else {
            if (FileUtil.writeFile(string, file)) {
                // return true if String data written to file successfully
                return true;
            } else {
                System.err.println("ERROR: Failed to save data into a \"" + string + "\" FILE");
                return false;
            }
        }
    }

    public boolean loadLoansData(LoansData loansData) {
        System.out.print("Loading loans data from a FILE...");
        if (parseBufferedCSVStrings(loansData)) {
            if (loansData.getLoansDataRecordsCounter() > 0) {
                System.out.println("\tdata successfully loaded.");
            } else {
                System.out.println("\tdata FILE is empty.");
            }
            return true; // return true if data loading from a file is successful
        } else {
            System.err.println("\tERROR: Failed to load loans data from a FILE...");
            return false; // if data loading from a file failed return false
        }
    }

    // read CSV string lines from a file, convert to loan[] array objects
    private boolean parseBufferedCSVStrings(LoansData loansData) {
        // create loanHelper obj to calculate loan's payment schedule
        LoansCalculatorHelper loansCalculatorHelper = new LoansCalculatorHelper();
        // parse buffer returned from a readFile() method in FileUtils class
        try (BufferedReader bufferedCSVStrings = FileUtil.readFile(FILE_TO_LOAD)) {
            String CSVLine = null;
            Loan loan;
            do {
                if (bufferedCSVStrings != null) {
                    CSVLine = bufferedCSVStrings.readLine();
                } else {
                    return false;
                }
                if (CSVLine != null) {
                    loan = CSVlineToNewLoanObj(CSVLine);
                    if (loan == null) {
                        return false;
                    }
                    // try to insert new loan object into loansData array
                    if (loansData.insertNewLoan(loan) > -1) {
                        if (loan != null) {
                            // if inserted successfully calculate new loan's payment schedule
                            loansCalculatorHelper.calcPaymentsSchedule(loan);
                        } else {
                            System.err.println("ERROR: Reading data from a FILE got null instead of loan obj");
                        }
                    }
                }
            } while (CSVLine != null);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: Caught IOException while trying to read and loads data from a FILE: \n" + e.getMessage());
            return false;
        }
        return true;
    }

    private Loan CSVlineToNewLoanObj(String CSVLine) {
//        CSVLine = "Martynas Papartynas,1000.0,12,10.0,0.0,10,0.0"; // FORT TESTING PURPOSES ONLY //
//        CSVLine = null; // FORT TESTING PURPOSES ONLY //
        if (CSVLine == null) return null; //
        if (CSVLine.equals("")) return null;
        if (CSVLine.equals("null")) return null;
        String fullName = findNthValueInCSVLine(CSVLine, 1);
        double loanAmount = 0, interestRate = 0, administrationFee = 0, fixedPeriodPayment = 0;
        int loanId = 0, compoundRate = 0, loanTerm = 0;
        try {
            loanId = Integer.parseInt(findNthValueInCSVLine(CSVLine, 0));
            loanAmount = Double.parseDouble(findNthValueInCSVLine(CSVLine, 2));
            compoundRate = Integer.parseInt(findNthValueInCSVLine(CSVLine, 3));
            interestRate = Double.parseDouble(findNthValueInCSVLine(CSVLine, 4));
            administrationFee = Double.parseDouble(findNthValueInCSVLine(CSVLine, 5));
            loanTerm = Integer.parseInt(findNthValueInCSVLine(CSVLine, 6));
            fixedPeriodPayment = Double.parseDouble(findNthValueInCSVLine(CSVLine, 7));
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Caught NumberFormatException while trying to parse bufferedCSVStrings string type into int or double types.");
            return null;
        }
        return new Loan(loanId, fullName, loanAmount, compoundRate, interestRate, administrationFee, loanTerm, fixedPeriodPayment);
    }

    private String findNthValueInCSVLine(String CSVLine, int offSet) {
        int start = 0;
        int end = ((end = CSVLine.indexOf(',')) > -1) ? end : CSVLine.length();
        for (int i = 0; i < offSet; i++) {
            start = CSVLine.indexOf(',', end) + 1;
            end = (end = CSVLine.indexOf(',', start)) > 0 ? end : CSVLine.length();
        }
        return CSVLine.substring(start, end);
    }
}
