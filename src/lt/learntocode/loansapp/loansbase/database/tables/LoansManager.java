package lt.learntocode.loansapp.loansbase.database.tables;

import lt.learntocode.loansapp.loansbase.database.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                stringBuffer.append(rs.getDouble("loanTerm") +", ");
                stringBuffer.append(rs.getDouble("fixedPeriodPayment"));
                System.out.println(stringBuffer.toString());
            }
        }
    }

//    public static Admin getRow(int adminId) throws SQLException {
//
//        String sql = "SELECT * FROM admin WHERE adminId = ?";
//        ResultSet rs = null;
//
//        try (
//                PreparedStatement stmt = conn.prepareStatement(sql);
//        ){
//            stmt.setInt(1, adminId);
//            rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                Admin bean = new Admin();
//                bean.setAdminId(adminId);
//                bean.setUserName(rs.getString("userName"));
//                bean.setPassword(rs.getString("password"));
//                return bean;
//            } else {
//                return null;
//            }
//
//        } catch (SQLException e) {
//            System.err.println(e);
//            return null;
//        } finally {
//            if (rs != null) {
//                rs.close();
//            }
//        }
//    }

}
