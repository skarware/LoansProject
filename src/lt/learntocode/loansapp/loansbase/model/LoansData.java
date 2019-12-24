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
        System.err.println("ERROR: loan obj in loans array for a given index not found. Be aware null is returned instead");
        return null;
    }

    public int insertNewLoan(Loan loan) {
        if (this.loansDataArray.length > this.loansDataRecordsCounter) {
            this.loansDataArray[this.loansDataRecordsCounter] = loan;
            // return inserted position and increase loan records number with each new loan;
            return ++this.loansDataRecordsCounter;
        } else {
            System.err.println("Error: Failed to insert new Loan obj, there is no more space inside loans array");
            return -1;
        }
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
