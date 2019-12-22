package lt.learntocode.loansapp.loansbase.services;

import lt.learntocode.loansapp.loansbase.model.Loan;
import lt.learntocode.loansapp.loansbase.model.LoansData;
import lt.learntocode.loansapp.loansbase.model.Payment;
import lt.learntocode.loansapp.loansbase.utils.ValidationUtil;

import java.util.Scanner;

public class CLIServices {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String sepLine = "----------------------------------------------------------------------------";

    private int getValidIntInput(String inputQuestion, int from, int to) {
        int input = -1;
        do {
            System.out.print(inputQuestion);
            try {
                input = new Scanner(System.in).nextInt(); // if not new Scanner, this might not going to work as you want it to
            } catch (Exception e) {
                System.err.println("Šis meniu nepriima raidžių simbolių");
            }
        } while (!ValidationUtil.intInputRange(input, from, to));
        return input;
    }

    private double getValidDoubleInput(String inputQuestion, double from, double to) {
        double input = -1;
        do {
            System.out.print(inputQuestion);
            try {
                input = new Scanner(System.in).nextDouble(); // if not new Scanner, this might not going to work as you want it to
            } catch (Exception e) {
                System.err.println("Šis meniu nepriima raidžių simbolių");
            }
        } while (!ValidationUtil.doubleInputRange(input, from, to));
        return input;
    }

    private String getValidStringInput(String inputQuestion, int minWords, int maxLength) {
        String input = "";
        do {
            System.out.print(inputQuestion);
            try {
                input = new Scanner(System.in).nextLine(); // if not new Scanner, this might not going to work as you want it to
            } catch (Exception e) {
                System.err.println("KLAIDA getValidStringInput() metode");
            }
        } while (!ValidationUtil.stringInputLength(input, minWords, maxLength));
        return input;
    }

    private char getValidCharKeyInput(String inputQuestion, char... validKeyOptions) {
        char key = '-';
        do {
            System.out.print(inputQuestion);
            try {
                key = new Scanner(System.in).next().charAt(0); // if not new Scanner, this might not going to work as you want it to
            } catch (Exception e) {
                System.err.println("KLAIDA getValidCharKeyInput() metode");
            }
        } while (!ValidationUtil.charKeyInput(key, (char[]) validKeyOptions));
        return key;
    }

    private int getCompoundRateInput(int compoundRate) {
        char key = getValidCharKeyInput("Kas kiek laiko   perskaičiuoti palūkanas?:\n\t" +
                "kas mėnesį (įvesti m)\n\t" +
                "kas metus (įvesti M)\n\t" +
                "\tįvesti: ", 'm', 'M');
        // next the logic to assign monthly or yearly compoundRate
        if (key == 'm') {
            compoundRate = 12;
        }
        if (key == 'M') {
            compoundRate = 1;
        }
        return compoundRate;
    }

    public Loan getMenuAddLoan() {
        String fullName;
        double loanAmount;
        int compoundRate = 12; // Kol kas constanta, jog kas menesi perskaicuoti, visg gali buti ir metams ar savaitemis
        double interestRate;
        double administrationFee;
        int loanTerm = 0;
        double fixedPeriodPayment = 0;
        char key;
        // print Add Loan Menu Interface and scanner the inputs
        fullName = getValidStringInput("Paskolos gavėjo Vardas Pavardė (Vardenis Pavardenis)\nįvesti: ", 2, 32);
        loanAmount = getValidDoubleInput("Įveskite pageidaujamą paskolos sumą: ", 1, 10000000);
//        compoundRate = getCompoundRateInput(compoundRate); // for future expansion of the program
        interestRate = getValidDoubleInput("Įveskite paskolos palūkanas procentais: ", 0, 100);
        administrationFee = getValidDoubleInput("Įveskite paskolos sutarties/administracinį mokestį: ", 0, 1000);
        // get the user choice for the loan term - fixed monthly payment or term in months to repay.
        key = getValidCharKeyInput("Norite gražinti paskolą:\n\t" +
                "išmokant dalimis per numatytą terminą mėnesiais (įvesti t arba T)\n\t" +
                "išmokant fiksuoto dydžio mėn. įmokas + palūkanas (įvesti f arba F)\n\t" +
                "\tįvesti: ", 't', 'f');
        // next the logic how we going to form the new loan and calc in the next method for loan obj
        if (key == 't') {
            loanTerm = getValidIntInput("Įveskite terminą mėnesiais per kurį pageidaujate gražinti paskolą: ", 1, 360);
        } else if (key == 'f') {
            fixedPeriodPayment = getValidDoubleInput("Įveskite fiksuoto dydžio mėnesinę įmoką (neįskaičiuojant palūkanų): ", 1, loanAmount);
        }
        return new Loan(fullName, loanAmount, compoundRate, interestRate, administrationFee, loanTerm, fixedPeriodPayment);
    }

    public int getLoansSummaryMenu() {
        int userOption = -1; // -1 is returned in case of NumberFormatException
        this.printLoansSummaryMenu();
        try {
            userOption = Integer.parseInt(scanner.next());
        } catch (NumberFormatException e) {
            System.err.println("Šis meniu nepriima raidžių.");
        }
        return userOption;
    }

    private void printLoansSummaryMenu() {
        System.out.println(sepLine);
        System.out.println("Norėdami Grįžti į pagrindinį meniu įveskite 0;");
        System.out.println("Norėdami peržiūrėti detalią ataskaitą pasirinkite paskolos numerį;");
        System.out.println(sepLine);
        System.out.print("Įveskite skaičių: ");
    }

    private void printMainMenu() {
        System.out.println(sepLine);
        System.out.println("A - Naujas skaičiavimas;");
        System.out.println("B - Peržiūrėti ankstesnius skaičiavimus;");
        System.out.println("C - Greitas skaičiavimas");
        System.out.println("E - baigti darbą");
        System.out.println(sepLine);
    }

    public int getMainMenu() {
        // print User Menu Interface
        this.printMainMenu();
//        // get user keypress and check Key Validity
        char key = getValidCharKeyInput("", 'a', 'b', 'c', 'e', 'A', 'B', 'C', 'E');
        // print user choice and return it OR exit program
        System.out.printf("Jūsų pasirinkimas: %s\n", ((Character) key).toString().toUpperCase());
        return key;
    }

    // Print Loans Summary Menu and get User Interface
    public void getMenuLoansSummary(LoansData loansData) {
        Loan loan;
        boolean isEmpty = true;
        int loanRecordsInArr = loansData.getLoansDataRecordsCounter();

        System.out.println(sepLine);
        System.out.printf("Vartotojas atliko %d %s\n", loanRecordsInArr, (loanRecordsInArr > 9 || loanRecordsInArr < 2) ? "skaičavimų" : "skaičavimus");
        System.out.println(sepLine);
        for (int i = 0; i < loanRecordsInArr; i++) {
            loan = loansData.getLoan(i);
            if (loan != null) {
                System.out.printf("Paskola Nr. %d: %s\n", i + 1, this.loanInfo(loan));
                isEmpty = false;
            } else if (loan == null) {
                System.out.printf("Paskola Nr. %d: --- ištrinta --- \n", i + 1);
            } else {
                System.err.println("Įvyko klaida atvaizduojant paskolų masyvą!");
            }
        }
        if (isEmpty) {
            System.out.println("Nėra duomenų apie paskolas");
        } else {
            int userOption = this.getLoansSummaryMenu();
            if (userOption > 0 || userOption == -1) { // -1 for returned invalid userOption to start loans Summary menu again.
                if (userOption <= loanRecordsInArr && userOption > 0) {
                    this.printSchedule(loansData.getLoan(userOption - 1)); // because index in array starts from zero
                } else {
                    System.err.printf("Tokio paskolos numerio nėra.\n" +
                            "\t\t\tĮveskite skaičių tarp 1 ir %d \n" +
                            "\t\t\t\t\t\t arba 0 norėdami grįžti į Pagrindinį Meniu.\n", loanRecordsInArr);
                }
                // Print Loans Summary Menu, again.
                this.getMenuLoansSummary(loansData);
            }
        }
        // Back to start then we reach this point
    }

    public void printSchedule(Loan loan) {
        String line = "----------------------------------------------------------------------------";
        System.out.println(line);
        System.out.printf("|%-10s|%-15s|%-15s|%-15s|%-15s|\n", "   Mėn.", "", "    Paskolos", "    Įmokos", "    Bendra");
        System.out.printf("|%-10s|%-15s|%-15s|%-15s|%-15s|\n", "  įmokos", "    Gržinta", "    likutis", "   Palūkanos", "    įmokos");
        System.out.printf("|%-10s|%-15s|%-15s|%-15s|%-15s|\n", "   Nr.", "     dalis", "  (po įmokos)", "   (einamos)", "     suma");
        System.out.println(line);
        // for loop to print all payents from a given loan
        Payment payment;
        for (int i = 0; i < loan.getPeriods(); i++) {
            payment = loan.getPayment(i);
            System.out.printf("|%8d  |%12.2f € |%12.2f € |%12.2f € |%12.2f € |\n", i + 1, payment.getPerPayment(), payment.getEndBalance(), payment.getPerInterest(), payment.getPeriodTotalPayment());
        }
        // total sums for columns
        System.out.println("============================================================================");
        System.out.printf("|%8s  |%12.2f € |%8s       |%12.2f € |%12.2f € |\n", "Viso", loan.getTotalLoanAmount(), "—", loan.getTotalInterest(), loan.getTotalPayment());
        System.out.println(line);
    }

    private String loanInfo(Loan loan) {
        return String.format("%s, Suma %.2f €, palūkanos %.0f %%, terminas %d mėn.;", loan.getFullName(), loan.getTotalLoanAmount(), loan.getInterestRate(), loan.getPeriods());
    }

}
