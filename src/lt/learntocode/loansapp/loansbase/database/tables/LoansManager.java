package lt.learntocode.loansapp.loansbase.database.tables;

import lt.learntocode.loansapp.loansbase.database.ConnectionManager;
import lt.learntocode.loansapp.loansbase.database.beans.LoanBean;

import java.sql.*;

public class LoansManager {

    private static Connection conn = ConnectionManager.getInstance().getConnection();

    public static void displayAllRows() throws SQLException {

        String sql = "SELECT * FROM loans";
        try (
                Statement stmt = conn.createStatement(
//                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.TYPE_FORWARD_ONLY,
//                        ResultSet.CONCUR_UPDATABLE, // Updatable ResultSet which have live connection to underlying Database
                        ResultSet.CONCUR_READ_ONLY // Read only ResultSet
                );
                // ResultSet instance encapsulate data returned from Database
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            System.out.println("Loans Table:");
            while (rs.next()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(rs.getInt("loan_id") + ", ");
                stringBuffer.append(rs.getString("fullName") + ", ");
                stringBuffer.append(rs.getDouble("loanAmount") + ", ");
                stringBuffer.append(rs.getInt("compoundRate") + ", ");
                stringBuffer.append(rs.getDouble("interestRate") + ", ");
                stringBuffer.append(rs.getDouble("administrationFee") + ", ");
                stringBuffer.append(rs.getInt("loanTerm") + ", ");
                stringBuffer.append(rs.getDouble("fixedPeriodPayment"));
                System.out.println(stringBuffer.toString());
            }
        }
    }

    public static LoanBean getRow(int loanId) throws SQLException {

        String sql = "SELECT * FROM loans WHERE loan_id = ?";
        ResultSet rs = null;
        LoanBean bean = new LoanBean(); // create instance of Loan bean and instantiate with class' no args constructor method

        try (
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
//                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.TYPE_FORWARD_ONLY,
//                        ResultSet.CONCUR_UPDATABLE, // Updatable ResultSet which have live connection to underlying Database
                        ResultSet.CONCUR_READ_ONLY // Read only ResultSet
                );
        ) {
            // must be set params outside try with resources parenthesis because you can not modify the statement obj within try resources block.
            stmt.setInt(1, loanId);
            // must fill params and call setInt() method before executeQuery() method to have an effect on statement query
            rs = stmt.executeQuery();
            // Take the data received from the query and Wrap it into instance of Java bean (LoanBean)
            if (rs.next()) {
                bean.setLoanId(loanId);

                // Oldish Java JBDC way to get data from ResultSet obj
//                bean.setFullName(rs.getString("fullName"));
//                bean.setLoanAmount(rs.getDouble("loanAmount"));
//                bean.setCompoundRate(rs.getInt("compoundRate"));
//                bean.setInterestRate(rs.getDouble("interestRate"));
//                bean.setAdministrationFee(rs.getDouble("administrationFee"));
//                bean.setLoanTerm(rs.getInt("loanTerm"));
//                bean.setFixedPeriodPayment(rs.getDouble("fixedPeriodPayment"));

                // same functionality with newer JAVA version feature - Generic getters
                bean.setFullName(rs.getObject("fullName", String.class));
                bean.setLoanAmount(rs.getObject("loanAmount", Double.class));
                bean.setCompoundRate(rs.getObject("compoundRate", Integer.class));
                bean.setInterestRate(rs.getObject("interestRate", Double.class));
                bean.setAdministrationFee(rs.getObject("administrationFee", Double.class));
                bean.setLoanTerm(rs.getObject("loanTerm", Integer.class));
                bean.setFixedPeriodPayment(rs.getObject("fixedPeriodPayment", Double.class));
            } else {
                System.err.println("ERROR: No rows were found with loanId: " + loanId);
                return null;
            }
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            // Always close objects in reverse order to how they were created
            if (rs != null) {
                rs.close();
            }
        }
        // if bean oj was successfully instantiated and its values were set, then return the bean to the calling context
        return bean;
    }

    public static ResultSet getAllRows() {

        String sql = "SELECT * FROM loans";

        try (
                Statement stmt = conn.createStatement(
//                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.TYPE_FORWARD_ONLY,
//                        ResultSet.CONCUR_UPDATABLE, // Updatable ResultSet which have live connection to underlying Database
                        ResultSet.CONCUR_READ_ONLY // Read only ResultSet
                );
        ) {
            ResultSet rs = stmt.executeQuery(sql); // declared and initialized here because we need to close it later in returned method
            // ResultSet variable rs is closed in method where obj has been returned to, or we return closed obj with no access to data anymore
            return rs;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }
}
