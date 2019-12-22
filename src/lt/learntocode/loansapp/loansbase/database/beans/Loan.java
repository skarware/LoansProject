package lt.learntocode.loansapp.loansbase.database.beans;

public class Loan {
    private int loanId;
    private String fullName;
    private double loanAmount;
    private int compoundRate;
    private double interestRate;
    private double administrationFee;
    private int loanTerm;
    private double fixedPeriodPayment;

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getCompoundRate() {
        return compoundRate;
    }

    public void setCompoundRate(int compoundRate) {
        this.compoundRate = compoundRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getAdministrationFee() {
        return administrationFee;
    }

    public void setAdministrationFee(double administrationFee) {
        this.administrationFee = administrationFee;
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public double getFixedPeriodPayment() {
        return fixedPeriodPayment;
    }

    public void setFixedPeriodPayment(double fixedPeriodPayment) {
        this.fixedPeriodPayment = fixedPeriodPayment;
    }
}
