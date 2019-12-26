package lt.learntocode.loansapp;

import lt.learntocode.loansapp.loansbase.database.tables.LoansManager;
import lt.learntocode.loansapp.loansbase.DataSrc;
import lt.learntocode.loansapp.loansbase.services.LoanServices;


public class Main {
    private static LoanServices loanServices = new LoanServices();

    public static void main(String[] args) throws Exception {

        // Setup persistent data source to load from and save to
        loanServices.setDataSrc(DataSrc.FILE);

        // for Testing DB
//        LoansManager.displayAllRows();

        // To start loan calculator
        loanServices.start();
    }
}