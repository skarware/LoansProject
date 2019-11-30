package lt.learntocode.loansapp.loansbase.services;

import lt.learntocode.loansapp.loansbase.utils.FileUtils;
import lt.learntocode.loansapp.loansbase.model.Loan;
import lt.learntocode.loansapp.loansbase.helpers.LoanHelper;
import lt.learntocode.loansapp.loansbase.model.LoansData;

import java.io.*;

public class FileServices {

    private static final String MAIN_FILE_NAME = "recourses/loansData.txt";
    private static final File FILE_TO_SAVE = new File(MAIN_FILE_NAME);
    private static final File FILE_TO_LOAD = new File(MAIN_FILE_NAME);
    private static final String PAYMENTS_FILES_FOLDER = "recourses/paymentsSchedules/";

    //method to save paymentsSchedule[] array objects as CSV string lines into the file
    public void savePaymentsScheduleToFile(LoansData loansData) {
        int loanIndex = loansData.getLoansDataRecordsCounter() - 1;
        File file = new File((PAYMENTS_FILES_FOLDER + "loan_" + (loanIndex + 1) + ".txt")); // recourses/paymentsSchedules/loan_1.txt
        Loan loan = loansData.getLoan(loanIndex);
        String paymentsScheduleString = loan.toCSVStrings();
        saveStringToFile(paymentsScheduleString, file);
    }

    //method to save loansData array objects as CSV string lines into the file
    public void saveLoansDataToFile(LoansData loansData) {
        String loansDataString = loansData.toCSVStrings(); // get loan[] as CSV strings
        saveStringToFile(loansDataString, FILE_TO_SAVE);    // save loan[]
        savePaymentsScheduleToFile(loansData);  // save paymentsSchedule[] to separate files inside paymentsSchedules folder
    }

    //method to save string lines into the file
    private void saveStringToFile(String string, File file) {
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    if (FileUtils.writeFile(string, file)) {
//                        System.out.println("Nauji duomenys sėkminai įrašyti į failą."); // jei uncommented the messege will come twice in a row
                    } else {
                        System.err.println("Klaida: Nepavyko irasyti duomenu \"" + string + "\" i file");
                    }
                } else {
                    System.err.println("Klaida: Nepavyko sukurti failo ir irasyti \"" + string + "\" i file");
                }
            } catch (IOException e) {
                System.err.println("IOException: Nepavyko sukurti failo ir i ji irasyti duomenu  \"" + string + "\": " + e.getMessage());
            }
        } else {
            if (FileUtils.writeFile(string, file)) {
                System.out.println("Nauji duomenys sėkminai įrašyti į failą.");
            } else {
                System.err.println("Klaida: Nepavyko irasyti \"" + string + "\" i file");
            }
        }
    }

    //method to read CSV string lines from a file, convert to loan[] array objects and return LoansData object;
    public LoansData loadLoansDataFromFile(LoansData loansData) {
        if (parseBufferedCSVStrings(loansData)) {
            System.out.print("Bandoma užkrauti paskolų duomenis iš failo...");
            if (loansData.getLoansDataRecordsCounter() > 0) {
                System.out.println("\tduomenys sėkmingai užkrauti.");
            } else {
                System.out.println("\tduomenų failas tuščias.");
            }
            return loansData; // in case load is successful give data from a file
        } else {
            System.err.println("...KLAIDA: Nepavyko atkurti paskolų duomenų iš failo...");
            return new LoansData(); // if file parsing fails then create and return new empty LoansData obj
        }
    }

    private boolean parseBufferedCSVStrings(LoansData loansData) {
        // create loanHelper obj to calculate loan's payment schedule
        LoanHelper loanHelper = new LoanHelper();
        // parse buffer returned from a readFile() method in FileUtils class
        try (BufferedReader bufferedCSVStrings = FileUtils.readFile(FILE_TO_LOAD)) {
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
                            loanHelper.calcPaymentsSchedule(loan); // if inserted successfully calculate new loan's payment schedule
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
