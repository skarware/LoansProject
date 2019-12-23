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
        ){

            System.out.println("Loans Table:");
            while (rs.next()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(rs.getInt("loan_id") + ": ");
                stringBuffer.append(rs.getString("fullName") +", ");
                stringBuffer.append(rs.getDouble("loanAmount") +", ");
                stringBuffer.append(rs.getInt("compoundRate") +", ");
                stringBuffer.append(rs.getDouble("interestRate") +", ");
                stringBuffer.append(rs.getDouble("administrationFee") +", ");
                stringBuffer.append(rs.getInt("loanTerm") +", ");
                stringBuffer.append(rs.getDouble("fixedPeriodPayment"));
                System.out.println(stringBuffer.toString());
            }
        }
    }

    public static LoanBean getRow(int loanId) throws SQLException {

        String sql = "SELECT * FROM loans WHERE loan_id = ?";
        ResultSet rs = null;

        try (
                PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            stmt.setInt(1, loanId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                LoanBean bean = new LoanBean();
                bean.setLoanId(loanId);
                bean.setFullName(rs.getString("fullName"));
                bean.setLoanAmount(rs.getDouble("loanAmount"));
                bean.setCompoundRate(rs.getInt("compoundRate"));
                bean.setInterestRate(rs.getDouble("interestRate"));
                bean.setAdministrationFee(rs.getDouble("administrationFee"));
                bean.setLoanTerm(rs.getInt("loanTerm"));
                bean.setLoanAmount(rs.getDouble("fixedPeriodPayment"));
                return bean;
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
    }
}
