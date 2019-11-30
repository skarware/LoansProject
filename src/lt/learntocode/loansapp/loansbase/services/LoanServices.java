package lt.learntocode.loansapp.loansbase.services;

import lt.learntocode.loansapp.cli.CLIServices;
import lt.learntocode.loansapp.loansbase.helpers.LoanHelper;
import lt.learntocode.loansapp.loansbase.model.Loan;
import lt.learntocode.loansapp.loansbase.model.LoansData;

public class LoanServices {
    private static final LoanHelper loanHelper = new LoanHelper();
    private static final FileServices fileServices = new FileServices();
    private static final CLIServices cli = new CLIServices();
    //    private static final GUIServices gui = new GUIServices();
    private LoansData loansData = new LoansData(); // be static kiekvienam naujam LoanServisui sukuriama po nauja paskolu masyva...
    private static boolean firstProgramStart = true;

    void testingMode() {
        // insert some loans for testing purposes only
        this.createMonthlyScheduledLoan(new Loan("testas vienas", 1000, 12, 10, 0, 10, 0)); // FOR TESTING PURPOSES ONLY //
        this.createMonthlyScheduledLoan(new Loan("testas du", 1000, 12, 10, 100, 10, 0)); // FOR TESTING PURPOSES ONLY //
        this.createMonthlyScheduledLoan(new Loan("testas trys", 1000, 12, 10, 50, 0,100d)); // FOR TESTING PURPOSES ONLY //
        this.createMonthlyScheduledLoan(new Loan("testas keturi", 1000, 12, 10, 100, 0, 100d)); // FOR TESTING PURPOSES ONLY //
        // To test how toCSVString() output looks
//        for (int i = 0; i < 4; i++) {
//            System.out.println("toCSVString(): " + loansData.getLoan(i).toCSVString());
//        }
        // To test how toCSVStringBuilder() output looks
        System.out.println("toCSVStrings():\n" + loansData.toCSVStrings());
    }

    public void start() {
        // Start the procedure to start Interest Rate Calculator

//        // take arguments for a cli or gui user interface?
//        if (cli == 1){
//            //run CMD version (set some boolean CLI = true;?)
//        }

        // if first program start then load loansData from a file
        if (firstProgramStart) {
            // loansData obj to be filled with data from a CSV file
            fileServices.loadLoansDataFromFile(loansData);
            // set to false so then next time start() method is called we do not redundantly load data from file
            firstProgramStart = false;
        }
        // run the main menu on start
        this.initiateMainMenu();
    }

    private void exitProgram() {
        // Start the procedure to exit a program
        System.out.println("\n\tPrograma baigia darbą,\n\t\t\t iki...");
        // save data to files
        System.exit(0);
    }

    private void initiateMainMenu() { // rename to getConsoleMainMenu
        // Get User Interface and follow the User
        int userOption = cli.getMainMenu();
        // useOption returns 3 different ways to follow
        if (userOption == 97 || userOption == 65) { // if key == 'a' or A
            Loan loan = cli.getMenuAddLoan(); // get User Interface and new loan obj from add new loan menu
            this.createMonthlyScheduledLoan(loan); // and add new loan into Loan Array
        } else if (userOption == 98 || userOption == 66) { // if key == 'b' or B
            cli.getMenuLoansSummary(loansData); // view loans in loanArr
        } else if (userOption == 99 || userOption == 67) { // if key == 'c' or C
            Loan loan = cli.getMenuAddLoan();
            this.createFastMonthlyScheduledLoan(loan); // calculate ne loan but dont save/insert into loanArr
        } else if (userOption == 101 || userOption == 69) {
            this.exitProgram(); // Exit program if key == 'e' or E
        }
        // in Any case go back to Start
        this.start();
    }

    private void createFastMonthlyScheduledLoan(Loan loan) {
        // After new loan obj inserted calculate Payment Schedule
        loanHelper.calcPaymentsSchedule(loan);
        // print loan obj to console
        cli.printSchedule(loan);
        // Back to Start
    }

    private void createMonthlyScheduledLoan(Loan loan) {
        // if new Loan inserted successfully then do the calculations for a given loan
        if (loansData.insertNewLoan(loan)) {
            // After new loan obj inserted calculate Payment Schedule
            loanHelper.calcPaymentsSchedule(loan);
            // print loan obj to console
            cli.printSchedule(loan);
            // save new data from loansData to a file
            fileServices.saveLoansDataToFile(loansData);
        } else {
            System.err.println("Klaida: Nepavyko irasyti naujos paskolos i masyva."); // just in case it fails inform the user
        }
        // in Any case go back to Start
    }
}
