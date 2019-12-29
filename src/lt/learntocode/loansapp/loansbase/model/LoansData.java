package lt.learntocode.loansapp.loansbase.model;

public class LoansData {

    private static final int LOAN_ARRAY_LENGTH = 100;
    private final Loan[] loansDataArray = new Loan[LOAN_ARRAY_LENGTH];
    private int loansDataRecordsCounter = 0; // Counter is increased only and by only by insertNewLoan method after new loan is created or on program data initialization on start
    private int nextLoanId = 0;

    public void setNextLoanId(int nextLoanId) {
        this.nextLoanId = nextLoanId;
    }

    public int getNextLoanId() {
        return nextLoanId;
    }

    public int getLoansDataRecordsCounter() {
        return this.loansDataRecordsCounter;
    }

    public Loan getLoan(int index) {
        if (loansDataArray[index] != null) {
            return loansDataArray[index];
        }
        return null;
    }

    public int insertLoan(Loan loan) {
        if (this.loansDataArray.length > this.loansDataRecordsCounter) {
            this.loansDataArray[this.loansDataRecordsCounter] = loan;
            // update lastLoanId field
            this.nextLoanId = loan.getLoanId() + 1; // get the last loanId and add one to get next loanId
            // return inserted position and increase loan records number with each new loan;
            return ++this.loansDataRecordsCounter;
        } else {
            System.err.println("Error: Failed to insert new Loan obj, there is no more space inside loans array");
            return -1;
        }
    }

    public int removeLoan(Loan loan) { // return deleted index from an array
        // ! Do not modify loansDataRecordsCounter after removing loan or program fail to work correctly !
        for (int i = 0; i < loansDataArray.length; i++) {
            if (loansDataArray[i] == loan) {
                loansDataArray[i] = null;
                return i;
            }
        }
        return -1; // return -1 if no obj was deleted from an array
    }

    public boolean updateLoan(Loan loan, Loan newLoan) {
        int updateIndex = removeLoan(loan);
        if (updateIndex > -1) {
            loansDataArray[updateIndex] = newLoan;
            return true;
        }
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
        if (stringBuilder.length() > 0) {
            return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        }
        return ""; // return empty string if stringBuilder.length() == 0 to avoid out of bound exception
    }

    public boolean isLoansDataArrayEmpty() {
        for (int i = 0; i < loansDataArray.length; i++) {
            if (loansDataArray[i] != null) {
                return false;
            }
        }
        return true;
    }
}
