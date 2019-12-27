package lt.learntocode.loansapp.loansbase.services;


import lt.learntocode.loansapp.loansbase.database.ConnectionManager;
import lt.learntocode.loansapp.loansbase.database.DBType;
import lt.learntocode.loansapp.loansbase.database.beans.LoanBean;
import lt.learntocode.loansapp.loansbase.database.tables.LoansManager;
import lt.learntocode.loansapp.loansbase.helpers.LoansCalculatorHelper;
import lt.learntocode.loansapp.loansbase.model.Loan;
import lt.learntocode.loansapp.loansbase.model.LoansData;
import lt.learntocode.loansapp.loansbase.utils.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBServices {

    // Get reference to the ConnectionManager object
    private final ConnectionManager connMngr = ConnectionManager.getInstance();


    public DBServices() {
        // Setup Database type
        connMngr.setDBType(DBType.HSQLDB);
//        ConnectionManager.getInstance().setDBType(DBType.HSQLDB);

    }

    public boolean loadLoansData(LoansData loansData) {
        System.out.print("Loading loans data from a DATABASE...");
        try {
            if (parseResultSet(loansData)) {
                if (loansData.getLoansDataRecordsCounter() > 0) {
                    System.out.println("\tdata successfully loaded.");
                } else {
                    System.out.println("\tDATABASE is empty.");
                }
                return true; // return true if data loading from a database is successful
            } else {
                System.err.println("\tERROR: Failed to load loans data from a DATABASE...");
                return false; // if data loading from a database failed return false
            }
        } catch (SQLException e) {
            DBUtil.processException(e);
            return false;
        }
    }

    private boolean parseResultSet(LoansData loansData) throws SQLException {
        // create loanHelper obj to calculate loan's payment schedule
        LoansCalculatorHelper loansCalculatorHelper = new LoansCalculatorHelper();
        // get ResultSet obj of all rows in Loans table
        ResultSet rs = LoansManager.getAllRows();
        if (rs != null) {
            while (rs.next()) {
                // encapsulate row data into bean obj
                LoanBean bean = LoansManager.getRow(rs.getInt("loan_id"));
                // convert bean obj to loan obj
                Loan loan = loanBeanToLoanObj(bean);
                // try to insert new loan object into loansData array
                if (loansData.insertLoan(loan) > -1) {
                    if (loan != null) {
                        // if inserted successfully calculate new loan's payment schedule
                        loansCalculatorHelper.calcPaymentsSchedule(loan);
                    } else {
                        System.err.println("ERROR: reading data from a DATABASE got null instead of loan obj");
                    }
                }
            }
                rs.close(); // after all is done with ResultSet obj we shall close it (it come opened from other method)
        } else {
            System.err.println("ERROR: failed to read rows in Loans table from a DATABASE");
            return false; // return false if ResultSet is null
        }
        return true; // return true if ResultSet is ok
    }

    private Loan loanBeanToLoanObj(LoanBean bean) {
        // check if bean obj is not null before converting it to Loan obj
        if (bean == null) {
            System.err.println("LoanBean is null obj, can not covert it to Loan obj");
            return null;
        } else {
            return new Loan(
                    bean.getLoanId(),
                    bean.getFullName(),
                    bean.getLoanAmount(),
                    bean.getCompoundRate(),
                    bean.getInterestRate(),
                    bean.getAdministrationFee(),
                    bean.getLoanTerm(),
                    bean.getFixedPeriodPayment());
        }
    }

    private LoanBean LoanObjToLoanBean(Loan loan) {
        LoanBean bean = new LoanBean();
        // check if Loan obj is not null before converting it to bean obj
        if (loan == null) {
            System.err.println("Loan obj is null obj, can not covert it to LoanBean obj");
            return null;
        } else {
            bean.setLoanId(loan.getLoanId());
            bean.setFullName(loan.getFullName());
            bean.setLoanAmount(loan.getLoanAmount());
            bean.setCompoundRate(loan.getCompoundRate());
            bean.setInterestRate(loan.getInterestRate());
            bean.setAdministrationFee(loan.getAdministrationFee());
            bean.setLoanTerm(loan.getLoanTerm());
            bean.setFixedPeriodPayment(loan.getFixedPeriodPayment());
        }
        return bean;
    }

    public boolean saveNewLoanObj(Loan loan) {
        // Encapsulate Loan class object into Java bean data entity for easy manipulation
        LoanBean bean = LoanObjToLoanBean(loan);
        // Check if bean is not null and inserted into Database as new row successfully
        if (bean != null && LoansManager.insertRow(bean)) {
            System.out.println("New Loan data successfully saved into DATABASE");
            return true;
        }
        return false;
    }

//    public boolean deleteLoan(Loan loan) {
    public boolean deleteLoan(int loanId) {

        System.out.println("Cia bus logika istrinti is duomenu bazes");

        return false;
    }

}
