package lt.learntocode.loansapp.loansbase.services;


import lt.learntocode.loansapp.loansbase.database.ConnectionManager;
import lt.learntocode.loansapp.loansbase.database.DBType;
import lt.learntocode.loansapp.loansbase.database.beans.LoanBean;
import lt.learntocode.loansapp.loansbase.model.Loan;
import lt.learntocode.loansapp.loansbase.model.LoansData;

public class DBServices {

    // Get reference to the ConnectionManager object
    private final ConnectionManager connMngr = ConnectionManager.getInstance();


    public DBServices() {
        // Setup Database type
        connMngr.setDBType(DBType.HSQLDB);
//        ConnectionManager.getInstance().setDBType(DBType.HSQLDB);


    }


    public boolean loadLoansData(LoansData loansData) {
        return false;
    }

    public boolean saveLoansData(LoansData loansData) {
        return false;
    }

    private Loan loanBeanToLoanObj(LoanBean bean) {
        // check if bean obj is not null before converting it to Loan obj
        if (bean == null) {
            System.err.println("LoanBean is null obj, can not covert it to Loan obj");
            return null;
        } else {
            System.out.println("Converting LoanBean to LoanObj");
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
        // check if Loan obj is not null before converting it to bean obj
        if (loan == null) {
            System.err.println("Loan obj is null obj, can not covert it to LoanBean obj");
            return null;
        } else {
            System.out.println("Converting LoanObj to LoanBean");
            LoanBean bean = new LoanBean();
            bean.setLoanId(loan.getLoanId());
            bean.setFullName(loan.getFullName());
            bean.setLoanAmount(loan.getLoanAmount());
            bean.setCompoundRate(loan.getCompoundRate());
            bean.setInterestRate(loan.getInterestRate());
            bean.setAdministrationFee(loan.getAdministrationFee());
            bean.setLoanTerm(loan.getLoanTerm());
            bean.setFixedPeriodPayment(loan.getFixedPeriodPayment());
            return bean;
        }
    }
}
