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

    private void exitProgram() {
        // Start the procedure to exit a program
        System.out.println("\n\tPrograma baigia darbą,\n\t\t\t iki...");
        // Explicitly close Database connection on application exit
        ConnectionManager.getInstance().close();
        // exit the application
        System.exit(0);
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

    private void updateLoan(Loan loan) {


        // if data source FILE save in memory data to a FILE (Then writing data to a file it is overwritten, not appended)
        if (this.dataSrc == DataSrc.FILE) {
            this.saveNewLoanData(null);
        }
    }

    private void deleteLoan(Loan loan) {
        // if data source DATABASE then Delete loan data from it
        if (this.dataSrc == DataSrc.DATABASE && loan != null) {
//            if (dbServices.deleteLoan(loan)) { // delete loan from database by loan obj
            if (dbServices.deleteLoan(loan.getLoanId())) { // delete loan from database directly by loanId
                System.out.println("Paskola sėkmingai ištrinta iš duomenų bazės");
            } else {
                System.err.println("ERROR: Failed to delete loan from DATABASE");
            }
        }
        // To delete loan obj change its reference to a null obj to mark it as deleted in LoansData obj
        if (loansData.removeLoan(loan) && loan != null) {
            System.out.println("Paskola sėkmingai ištrinta iš vidinės atminties");
        } else {
            System.err.println("ERROR: Failed to delete loan from memory");
        }
        // if data source FILE save modified in memory data to a FILE (Then writing data to a file it is overwritten, not appended)
        if (this.dataSrc == DataSrc.FILE && loan != null) {
            this.saveNewLoanData(null);
            System.out.println("Paskola sėkmingai ištrinta iš failo");
        }
    }

    private void initiateMainMenu() { // rename to getConsoleMainMenu?

        int loansDataRecordsCounter = loansData.getLoansDataRecordsCounter();
        // Get User Interface and follow the User
        int userOption = cli.getMainMenu();
        // useOption returns 3 different ways to follow
        if (userOption == 97 || userOption == 65) { // if key == 'a' or A
            Loan loan = cli.getAddLoanMenu(loansDataRecordsCounter); // get User Interface and new loan obj from add new loan menu
            this.createMonthlyScheduledLoan(loan); // and Add new loan into Loans Array
        } else if (userOption == 98 || userOption == 66) { // if key == 'b' or B
            cli.getLoansSummaryList(loansData); // View loans in loanArr
        } else if (userOption == 114 || userOption == 82) { // if key == 'r' or R
            Loan loan = cli.getModifyLoansMenu(loansData, 0); // get menu to Update loan
            this.updateLoan(loan); // Update loan data in LoansData obj and data source
        } else if (userOption == 100 || userOption == 68) { // if key == 'd' or D
            Loan loan = cli.getModifyLoansMenu(loansData, 1); // get menu to Delete loan
            this.deleteLoan(loan); // Remove loan from LoansData obj and data source
        } else if (userOption == 99 || userOption == 67) { // if key == 'c' or C
            Loan loan = cli.getAddLoanMenu(loansDataRecordsCounter);
            this.createFastMonthlyScheduledLoan(loan); // calculate new loan but dont save/insert into loanArr
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
