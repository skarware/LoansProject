package lt.learntocode.loansapp;

import lt.learntocode.loansapp.loansbase.services.LoanServices;

public class Main {
    private static LoanServices loanServices = new LoanServices();

    public static void main(String[] args) {

        // Testing Mode for TESTING PURPOSES ONLY
//        loanServices.testingMode();

        // To start loan calculator
        loanServices.start();
    }
}

