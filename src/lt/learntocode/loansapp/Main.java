package lt.learntocode.loansapp;

import lt.learntocode.loansapp.loansbase.database.tables.LoansManager;
import lt.learntocode.loansapp.loansbase.services.DataSrc;
import lt.learntocode.loansapp.loansbase.services.LoanServices;


public class Main {
    private static LoanServices loanServices = new LoanServices();

    public static void main(String[] args) throws Exception {

        // Setup persistent data source to load from and save to
        loanServices.setDataSrc(DataSrc.FILE);

        // Testing Mode for TESTING PURPOSES ONLY
//        loanServices.testingMode();

        // for Testing DB
        LoansManager.displayAllRows();

        // To start loan calculator
        loanServices.start();
    }
}