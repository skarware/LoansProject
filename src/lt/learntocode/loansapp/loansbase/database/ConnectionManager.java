package lt.learntocode.loansapp.loansbase.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {    // Singelton for Database connection
    // ConnectionManager instance of itself initially set to null
    private static ConnectionManager instance = null;
    // credentials to connect to database
    private final String USERNAME = "loansuser";
    private final String PASSWORD = "loansuserpassword";
    private final String H_CONN_STRING =
            "jdbc:hsqldb:file:data/loansDB";
    private final String P_CONN_STRING =
            "jdbc:postgres://localhost/loansDB";
    // dbType not static so can be changed after this class instantiated
    private DBType dbType = DBType.HSQLDB;
    // connection object initially set to null
    private Connection conn = null;
    // Singelton has no public constructors, only one instance of the class can be created from within the class itself
    private ConnectionManager() {
    }
    // to get the reference to the one and only ConnectionManager object for the whole application, other parts of code will call this method
    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    // public method to let application to decide what type of database will be in use
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        System.out.println("Database type set to " + dbType);
    }

    // method will be called only if connection is not already opened
    private boolean openConnection() {
        try {
            switch (this.dbType) {
                case POSTGRES:
                    conn = DriverManager.getConnection(P_CONN_STRING, USERNAME, PASSWORD);
                    return true;
                case HSQLDB:
                    conn = DriverManager.getConnection(H_CONN_STRING, USERNAME, PASSWORD);
                    return true;
                default:
                    return false;
            }
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }
    // public method for opening single connection to a database for the whole application
    public Connection getConnection() {
        // if connection is not opened already then open it and return the reference
        if (conn == null) {
            if (openConnection()) {
                System.out.println("Connection to DB opened");
                return conn;
            } else {
                return null;
            }
        }
        // return already opened connection reference
        return conn;
    }

    // public method for explicitly closing connection
    public void close() {
        System.out.println("Closing connection to DB");
        try {
            // close and null the connection so we can explicitly manage the opening and closing of the connection from anywhere in the application but only by calling this method
            conn.close();
            conn = null;
        } catch (Exception e) {
            System.err.println(e);
        }
    }


}
