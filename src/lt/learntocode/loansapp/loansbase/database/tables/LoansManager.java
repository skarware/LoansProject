package lt.learntocode.loansapp.loansbase.database.tables;

import lt.learntocode.loansapp.loansbase.database.ConnectionManager;
import lt.learntocode.loansapp.loansbase.database.beans.LoanBean;

import java.sql.*;

public class LoansManager {

    private static Connection conn = ConnectionManager.getInstance().getConnection();

    public static void displayAllRows() throws SQLException {

        String sql = "SELECT * FROM loans";
        try (
                Statement stmt = conn.createStatement();
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
        LoanBean bean = new LoanBean();

        try (
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, loanId);
            rs = stmt.executeQuery();

            if (rs.next()) {
//                bean.setLoanId(loanId);
                bean.setLoanId(rs.getInt("loan_id"));
                bean.setFullName(rs.getString("fullName"));
                bean.setLoanAmount(rs.getDouble("loanAmount"));
                bean.setCompoundRate(rs.getInt("compoundRate"));
                bean.setInterestRate(rs.getDouble("interestRate"));
                bean.setAdministrationFee(rs.getDouble("administrationFee"));
                bean.setLoanTerm(rs.getInt("loanTerm"));
                bean.setFixedPeriodPayment(rs.getDouble("fixedPeriodPayment"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return bean; // if all was good return the bean
    }

    public static ResultSet getAllRows() {

        String sql = "SELECT * FROM loans";

        try (
                Statement stmt = conn.createStatement();
        ) {
            ResultSet rs = stmt.executeQuery(sql);
            // ResultSet is closed in method where obj has been returned to, or we return closed obj with no access to data anymore
            return rs;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }
}
