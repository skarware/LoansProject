package lt.learntocode.loansapp.loansbase.model;

import java.time.LocalDate;

public class Loan {
    private int loanId;
    private final String fullName;
    private final double loanAmount;
    private final int compoundRate;
    private final double interestRate;
    private final double administrationFee;
    private final int loanTerm;
    private final double fixedPeriodPayment;
    private final LocalDate date;
    private final double totalLoanAmount;
    private final int periods;
    private final double periodPayment;
    private double totalInterest;
    private double totalPayment;
    private Payment[] paymentsSchedule;

    public int getLoanId() {
        return this.loanId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public double getLoanAmount() {
        return this.loanAmount;
    }

    public int getCompoundRate() {
        return this.compoundRate;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public double getAdministrationFee() {
        return administrationFee;
    }

    public int getLoanTerm() {
        return this.loanTerm;
    }

    public double getFixedPeriodPayment() {
        return this.fixedPeriodPayment;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getTotalLoanAmount() {
        return this.totalLoanAmount;
    }

    public int getPeriods() {
        return this.periods;
    }

    public double getPeriodPayment() {
        return this.periodPayment;
    }

    public double getTotalInterest() {
        return this.totalInterest;
    }

    public double getTotalPayment() {
        return this.totalPayment;
    }

    // Main Constructor
    public Loan(int loanId, String fullName, double loanAmount, int compoundRate, double interestRate, double administrationFee, int loanTerm, double fixedPeriodPayment) {
        this.loanId = loanId;
        this.fullName = fullName;
        this.loanAmount = loanAmount;
        this.compoundRate = compoundRate; // for now its calculated monthly
        this.interestRate = interestRate;
        this.administrationFee = administrationFee;
        this.loanTerm = loanTerm;
        this.fixedPeriodPayment = fixedPeriodPayment;
        // fields above is all passed as arguments;
        this.date = LocalDate.now();
        // fields below is derived
        this.totalLoanAmount = loanAmount + administrationFee;
        this.periodPayment = loanTerm == 0 ? fixedPeriodPayment : (this.totalLoanAmount / (double) loanTerm); // cast to (double) to avoid any misscalcs
        this.periods = (int) Math.ceil(this.totalLoanAmount / this.periodPayment); // ceiling isa must for this to work as intended
        this.totalInterest = 0;
        this.totalPayment = 0;
        // create payments array
        this.paymentsSchedule = new Payment[this.periods]; // Array of payments
    }

    public boolean insertNewPayment(Payment payment) {
        for (int i = 0; i < this.paymentsSchedule.length; i++) {
            if (this.paymentsSchedule[i] == null) {
                this.paymentsSchedule[i] = payment;
                return true;
            }
        }
        System.err.println("Klaida: nepavyko iterpti naujo payment i loan masyva, nes nerado laisvos vietos masyve.");
        return false;
    }

    public Payment getPayment(int index) {
        if (this.paymentsSchedule[index] != null) {
            return this.paymentsSchedule[index];
        }
        System.err.println("Klaida: Nera tokio paymento paymentsSchedule masyve, todel grazinti jo nepavyksta");
        return null;
    }

    public void increaseScheduleTotals(Payment payment) {
        this.totalPayment += payment.getPeriodTotalPayment();
        this.totalInterest += payment.getPerInterest();
    }

    // method to convert a loan object to a CSV formatted String line
    public String toCSVStringLine() {
        return this.getLoanId() + "," +
                this.getFullName() + "," +
                this.getLoanAmount() + "," +
                this.getCompoundRate() + "," +
                this.getInterestRate() + "," +
                this.getAdministrationFee() + "," +
                this.getLoanTerm() + "," +
                this.getFixedPeriodPayment() + "," +
                this.getDate();
    }

    // method to convert paymentsSchedule[] array objects to CSV formatted String with multiple lines
    public String toCSVStrings() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.periods; i++) {
            if (this.paymentsSchedule[i] != null) {
                stringBuilder.append(i + 1 + ",").append(this.paymentsSchedule[i].toCSVStringLine()).append("\n");
            }
        }
        // total sums for columns
        String totalSums = "totals," + this.getTotalLoanAmount() + ",-," + this.getTotalInterest() + "," + this.getTotalPayment();
        // after all is done, return StringBuilder as String
        return stringBuilder.append(totalSums).toString();
    }

    @Override
    public String toString() {
        return "Loan{" +
                "fullName='" + fullName + '\'' +
                ", loanAmount=" + loanAmount +
                ", compoundRate=" + compoundRate +
                ", interestRate=" + interestRate +
                ", administrationFee=" + administrationFee +
                ", loanTerm=" + loanTerm +
                ", fixedPeriodPayment=" + fixedPeriodPayment +
//                ", totalLoanAmount=" + totalLoanAmount +
//                ", periods=" + periods +
//                ", periodPayment=" + periodPayment +
//                ", totalInterest=" + totalInterest +
//                ", totalPayment=" + totalPayment +
//                ", paymentsSchedule=" + Arrays.toString(paymentsSchedule) +
                '}';
    }
}
