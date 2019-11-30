package lt.learntocode.loansapp.loansbase.model;

public class Payment {

    private double periodPayment;
    private double endBalance;
    private double periodInterest;
    private double periodTotalPayment;

    public Payment(double periodPayment, double endBalance, double periodInterest) {
        this.periodPayment = periodPayment;
        this.endBalance = endBalance;
        this.periodInterest = periodInterest;
        this.periodTotalPayment = periodPayment + periodInterest;
    }

    public Payment(Payment payment) { // will be used to convert CSV files to Obj?
        this.periodPayment = payment.getPerPayment();
        this.endBalance = payment.getEndBalance();
        this.periodInterest = payment.getPerInterest();
        this.periodTotalPayment = payment.getPerPayment() + payment.getPerInterest();
    }

    public double getPerPayment() {
        return periodPayment;
    }

    public double getEndBalance() {
        return endBalance;
    }

    public double getPerInterest() {
        return periodInterest;
    }

    public double getPeriodTotalPayment() {
        return periodTotalPayment;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "periodPayment=" + periodPayment +
                ", endBalance=" + endBalance +
                ", periodInterest=" + periodInterest +
                ", periodTotalPayment=" + periodTotalPayment +
                '}';
    }

    // method to convert a payment object to a CSV formatted String line
    public String toCSVStringLine() {
        return this.getPerPayment() + "," +
                this.getEndBalance() + "," +
                this.getPerInterest() + "," +
                this.getPeriodTotalPayment();
    }
}
