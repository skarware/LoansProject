package lt.learntocode.loansapp;

import lt.learntocode.loansapp.loansbase.DataSrc;
import lt.learntocode.loansapp.loansbase.services.LoanServices;


public class Main {
    private static LoanServices loanServices = new LoanServices();

    public static void main(String[] args) {

        // Setup persistent data source to load from and save to
        loanServices.setDataSrc(DataSrc.DATABASE);

        // To start loan calculator
        loanServices.start();
    }
}