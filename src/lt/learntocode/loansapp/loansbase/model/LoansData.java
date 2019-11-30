package lt.learntocode.loansapp.loansbase.model;

public class LoansData {

    private static final int LOAN_ARRAY_LENGTH = 100;
    private final Loan[] loansDataArray = new Loan[LOAN_ARRAY_LENGTH];
    private int loansDataRecordsCounter = 0;

    public int getLoansDataRecordsCounter() {
        return this.loansDataRecordsCounter;
    }

    public Loan getLoan(int index) {
        if (loansDataArray[index] != null) {
            return loansDataArray[index];
        }
        System.err.println("Klaida: Nera tokios loan paskolos loanArr masyve, todel grazinti jos nepavyksta");
        return null;
    }

    public boolean insertNewLoan(Loan loan) {
        for (int i = 0; i < this.loansDataArray.length; i++) {
            if (this.loansDataArray[i] == null) {
                this.loansDataArray[i] = loan;
                this.loansDataRecordsCounter++; // increase loan records number with each new loan
                return true;
            }
        }
        System.err.println("Klaida: Nepavyko irasyti naujos pasolos, nes nepavyko iterpti naujo loan in loansDataArray masyva - nera laisvos (null) vietos masyve.");
        return false;
    }

    // method to convert loan[] array objects to CSV formatted String with multiple lines
    public String toCSVStrings() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < loansDataRecordsCounter; i++) {
            if (this.loansDataArray[i] != null) {
                stringBuilder.append(this.loansDataArray[i].toCSVStringLine()).append("\n");
            }
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }
}
