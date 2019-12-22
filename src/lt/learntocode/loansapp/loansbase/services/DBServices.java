package lt.learntocode.loansapp.loansbase.services;


import lt.learntocode.loansapp.loansbase.database.ConnectionManager;
import lt.learntocode.loansapp.loansbase.database.DBType;
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
}
