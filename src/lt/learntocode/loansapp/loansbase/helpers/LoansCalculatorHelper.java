package lt.learntocode.loansapp.loansbase.helpers;

import lt.learntocode.loansapp.loansbase.model.Loan;
import lt.learntocode.loansapp.loansbase.model.Payment;

public class LoansCalculatorHelper {

    public void calcPaymentsSchedule(Loan loan) {
        double periodPayment = loan.getPeriodPayment(); // need for a local copy to change locally periodPayment if last payment is less then initial periodPayment
        double endBalance = loan.getTotalLoanAmount();
        double periodInterest = 0;
        // Create payments and calc Payments Schedule Array
        for (int i = 0; i < loan.getPeriods(); i++) {
            if (endBalance - periodPayment < 0) {
                periodPayment = endBalance;
            }
            // calc period interest and end balance
            periodInterest = calcPeriodInterest(endBalance, loan);
            endBalance = calcEndBalance(endBalance, periodPayment);
            // create new payment obj and insert into loan obj
            Payment payment = new Payment(periodPayment, endBalance, periodInterest);
            // if new payment inserted successfully then do other calculations for a given payment
            if (loan.insertNewPayment(payment)) {
                // After new payment inserted increase loan's totals
                loan.increaseScheduleTotals(payment);
            }
        }
    }

    private double calcPeriodInterest(double balance, Loan loan) {
        return (double) Math.round(((balance * loan.getInterestRate()) / 100) / loan.getCompoundRate() * 100) / 100d;
    }

    private double calcEndBalance(double endBalance, double periodPayment) {
        return Math.round((endBalance - periodPayment) * 100000) / 100000d;
    }

}
