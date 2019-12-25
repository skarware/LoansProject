package lt.learntocode.loansapp.loansbase.database.tables;

import lt.learntocode.loansapp.loansbase.database.ConnectionManager;
import lt.learntocode.loansapp.loansbase.database.beans.LoanBean;
import lt.learntocode.loansapp.loansbase.utils.DBUtil;

import java.sql.*;

public class LoansManager {

    // Open and get reference to a connection to the Database from ConnectionManager singleton.
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

    public static boolean insertRow(LoanBean bean) {
        // SQL command to insert data
        String sql = "INSERT INTO loans (fullName, loanAmount, compoundRate, interestRate, administrationFee, loanTerm, fixedPeriodPayment)" +
                " VALUES (?,?,?,?,?,?,?)"; // for JBDC "?" marking place holders
        // To get back inserted row auto incremented ID/Primary Key value we declare ResultSet obj
        ResultSet genKey = null;

        try (
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS // Explicitly asking to return auto incremented incremented ID/Primary Key from Database (no matter what Database management system in use, please return if possible)
                );
        ) {
            // Populating place holders with values from LoanBean obj
            stmt.setString(1, bean.getFullName());
            stmt.setDouble(2, bean.getLoanAmount());
            stmt.setInt(3, bean.getCompoundRate());
            stmt.setDouble(4, bean.getInterestRate());
            stmt.setDouble(5, bean.getAdministrationFee());
            stmt.setInt(6, bean.getLoanTerm());
            stmt.setDouble(7, bean.getFixedPeriodPayment());
            // To know if insert statement was successful we assign returned integer value - a number of rows were affected by the insert statement
            int rowsAffected = stmt.executeUpdate();
            // for this particular insert statement only 1 row should be affected if execution was successful and a row was inserted into Database
            if (rowsAffected == 1) {
                // Get generated primary key value
                genKey = stmt.getGeneratedKeys();
                // returned value is a ResultSet so the cursor starts before first row of data, we call next() method to move cursor forward the one and only row of data
                genKey.next();
                // from getGeneratedKeys() method we always get a ResultSet of single column and single row, which we get by calling .getInt(1); 1 - for the first column
                // returned value we pass to the bean obj so it can be accessible from the calling context later if needed after insertRow method returned
                bean.setLoanId(genKey.getInt(1));
            } else {
                // if we get here insert statement was not successful
                System.err.println("ERROR: No rows were affected, inserting new data into DATABASE failed");
                return false;
            }
        } catch (SQLException e) {
            DBUtil.processException(e);
            return false;
        } finally {
            // as with all ResultSets we need explicitly close then you done with it
            if (genKey != null) {
                try {
                    genKey.close();
                } catch (SQLException e) {
                    DBUtil.processException(e);
                }
            }
        }
        // if we get here insert statement was successful
        return true;
    }
}
