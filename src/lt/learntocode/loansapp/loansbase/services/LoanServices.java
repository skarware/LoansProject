package lt.learntocode.loansapp.loansbase.services;

import lt.learntocode.loansapp.loansbase.DataSrc;
import lt.learntocode.loansapp.loansbase.database.ConnectionManager;
import lt.learntocode.loansapp.loansbase.helpers.LoansCalculatorHelper;
import lt.learntocode.loansapp.loansbase.model.Loan;
import lt.learntocode.loansapp.loansbase.model.LoansData;

public class LoanServices {
    private LoansData loansData = new LoansData();
    private final LoansCalculatorHelper loansCalculatorHelper = new LoansCalculatorHelper();
    private final FileServices fileServices = new FileServices();
    private final DBServices dbServices = new DBServices();
    private final CLIServices cli = new CLIServices();
    private boolean firstProgramStart = true;
    private DataSrc dataSrc = DataSrc.FILE;

    // public method to chose data source to load loansData obj from and save to
    public void setDataSrc(DataSrc dataSrc) {
        this.dataSrc = dataSrc;
        System.out.println("Data source: " + dataSrc);
    }

    public void start() {
        // Start the procedure to start Interest Rate Calculator

//        // take arguments for a cli or gui user interface?
//        if (cli == 1){
//            //run CMD version (set some boolean CLI = true;?)
//        }

        // If first program start then Initialize loansData obj with data from a file or Database
        if (firstProgramStart) {
            this.initializeLoansDataObj();
            // Set to false so then next time start() method is called we do not redundantly load data from file
            firstProgramStart = false;
        }
        // run the main menu on start
        this.initiateMainMenu();
    }

    private void initializeLoansDataObj() {
        boolean isDataLoadedSuccessfully = true;
        switch (this.dataSrc) {
            case DATABASE:
                // Load loansData obj from database
                isDataLoadedSuccessfully = dbServices.loadLoansData(loansData);
                break;
            case FILE:
                // Load loansData obj to be filled with data from a CSV file
                isDataLoadedSuccessfully = fileServices.loadLoansData(loansData);
                break;
            default:
                System.err.println("ERROR: Not valid option for Loading initial data");
                break;
        }
        // If data Loading from a chosen source fails then create and reassign new empty LoansData obj
        if (!isDataLoadedSuccessfully) {
            loansData = new LoansData();
            System.err.println("ERROR: Initial Data Loading from a chosen source failed - initializing program with empty LoansData obj");
        }
    }

    private void saveNewLoanData(Loan loan) {
        boolean isDataSavedSuccessfully = true;
        switch (this.dataSrc) {
            case DATABASE:
                // Save loansData obj to DATABASE
                isDataSavedSuccessfully = dbServices.saveNewLoanObj(loan);
                break;
            case FILE:
                // Save loansData obj to FILE
                isDataSavedSuccessfully = fileServices.saveLoansData(this.loansData);
                break;
            default:
                System.err.println("ERROR: Not valid option for Saving data");
                break;
        }
        // If data Saving to a chosen source fails then inform the user
        if (!isDataSavedSuccessfully) {
            System.err.println("ERROR: Data Saving to a chosen source failed - LoansData obj data might be lost on data source");
        }
    }

    private void exitProgram() {
        // Start the procedure to exit a program
        System.out.println("\n\tPrograma baigia darbą,\n\t\t\t iki...");
        // close Database connection on application exit
        ConnectionManager.getInstance().close();
        // exit the application
        System.exit(0);
    }

    private void initiateMainMenu() { // rename to getConsoleMainMenu
        // Get User Interface and follow the User
        int userOption = cli.getMainMenu();
        // useOption returns 3 different ways to follow
        if (userOption == 97 || userOption == 65) { // if key == 'a' or A
            Loan loan = cli.getMenuAddLoan(loansData.getLoansDataRecordsCounter()); // get User Interface and new loan obj from add new loan menu
            this.createMonthlyScheduledLoan(loan); // and add new loan into Loans Array
        } else if (userOption == 98 || userOption == 66) { // if key == 'b' or B
            cli.getLoansSummaryList(loansData); // view loans in loanArr
        } else if (userOption == 114 || userOption == 82) { // if key == 'r' or R
            cli.getUpdateLoansMenu(loansData); // get menu to Update loan
//        } else if (userOption == 100 || userOption == 68) { // if key == 'd' or D
//            cli.getDeleteLoansMenu(loansData); // get menu to Delete loan
        } else if (userOption == 99 || userOption == 67) { // if key == 'c' or C
            Loan loan = cli.getMenuAddLoan(loansData.getLoansDataRecordsCounter());
            this.createFastMonthlyScheduledLoan(loan); // calculate ne loan but dont save/insert into loanArr
        } else if (userOption == 101 || userOption == 69) {
            this.exitProgram(); // Exit program if key == 'e' or E
        }
        // in Any case go back to Start
        this.start();
    }

    private void createFastMonthlyScheduledLoan(Loan loan) {
        // After new loan obj inserted calculate Payment Schedule
        loansCalculatorHelper.calcPaymentsSchedule(loan);
        // print loan obj to console
        cli.printSchedule(loan);
        // Back to Start
    }

    private void createMonthlyScheduledLoan(Loan loan) {
        // if new Loan inserted successfully then do the calculations for a given loan
        if (loansData.insertNewLoan(loan) > -1) {
            // After new loan obj inserted calculate Payment Schedule
            loansCalculatorHelper.calcPaymentsSchedule(loan);
            // print loan obj to console
            cli.printSchedule(loan);
            // save new data to a FILE or DATABASE
            this.saveNewLoanData(loan);
        } else {
            System.err.println("ERROR: Failed to save new loan obj into loans array."); // just in case it fails inform the user
        }
        // in Any case go back to Start
    }
}
