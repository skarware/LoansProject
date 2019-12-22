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
                        System.err.println("Klaida: Nepavyko irasyti duomenu \"" + string + "\" i file");
                        return false;
                    }
                } else {
                    System.err.println("Klaida: Nepavyko sukurti failo ir irasyti \"" + string + "\" i file");
                    return false;
                }
            } catch (IOException e) {
                System.err.println("IOException: Nepavyko sukurti failo ir i ji irasyti duomenu  \"" + string + "\": " + e.getMessage());
                return false;
            }
        } else {
            if (FileUtil.writeFile(string, file)) {
                System.out.println("Nauji duomenys sėkminai įrašyti į failą.");
                return true;
            } else {
                System.err.println("Klaida: Nepavyko irasyti \"" + string + "\" i file");
                return false;
            }
        }
    }

    // method to read CSV string lines from a file, convert to loan[] array objects
    public boolean loadLoansData(LoansData loansData) {
        if (parseBufferedCSVStrings(loansData)) {
            System.out.print("Bandoma užkrauti paskolų duomenis iš failo...");
            if (loansData.getLoansDataRecordsCounter() > 0) {
                System.out.println("\tduomenys sėkmingai užkrauti.");
            } else {
                System.out.println("\tduomenų failas tuščias.");
            }
            return true; // return true if data loading from a file is successful
        } else {
            System.err.println("...KLAIDA: Nepavyko atkurti paskolų duomenų iš failo...");
            return false; // if data loading from a file failed return false
        }
    }

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
                    if (loansData.insertNewLoan(loan)) { // insert new loan object into loansData array
                        if (loan != null) {
                            loansCalculatorHelper.calcPaymentsSchedule(loan); // if inserted successfully calculate new loan's payment schedule
                        } else {
                            System.err.println("Klaida: skaitant duomenis is failo gautas 'null' loan objektas");
                        }
                    }
                }
            } while (CSVLine != null);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IOException: Ivyko klaida, nepavyko nuskaityti failo ir atkurti loansData duomenu: " + e.getMessage());
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
        String fullName = findNthValueInCSVLine(CSVLine, 0);
        double loanAmount = 0, interestRate = 0, administrationFee = 0, fixedPeriodPayment = 0;
        int compoundRate = 0, loanTerm = 0;
        try {
            loanAmount = Double.parseDouble(findNthValueInCSVLine(CSVLine, 1));
            compoundRate = Integer.parseInt(findNthValueInCSVLine(CSVLine, 2));
            interestRate = Double.parseDouble(findNthValueInCSVLine(CSVLine, 3));
            administrationFee = Double.parseDouble(findNthValueInCSVLine(CSVLine, 4));
            loanTerm = Integer.parseInt(findNthValueInCSVLine(CSVLine, 5));
            fixedPeriodPayment = Double.parseDouble(findNthValueInCSVLine(CSVLine, 6));
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException Klaida: Nepavyko isparsinti bufferedCSVStrings eilutes i int ar double tipa.");
            return null;
        }
        return new Loan(fullName, loanAmount, compoundRate, interestRate, administrationFee, loanTerm, fixedPeriodPayment);
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
