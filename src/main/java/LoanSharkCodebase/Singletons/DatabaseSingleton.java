package LoanSharkCodebase.Singletons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseSingleton {
    private static final DatabaseSingleton DBInstance = new DatabaseSingleton();
    private Connection databaseLink;

    private DatabaseSingleton(){}


    public static DatabaseSingleton  getInstance(){
        return DBInstance;
    }

    public Connection getConnection(){
        String databaseName = "LoanShark";
        String databaseUser = "root";
        String databasePassword = "DevPass";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser,databasePassword);
        } catch (Exception e){
            throw new RuntimeException("unhandled", e);

        } return databaseLink;
    }

    Connection DBConnection = getConnection();

    public ResultSet databaseQuery(String queryRequest){
        try{
            Statement s = DBConnection.createStatement();
            ResultSet queryOutput = s.executeQuery(queryRequest);
            return queryOutput;

        } catch(Exception e){
            throw new RuntimeException("unhandled",e);
        }
    }

    public void databaseInsert(String queryRequest) {
        try {
            Statement s = DBConnection.createStatement();
            s.executeUpdate(queryRequest);

        } catch (Exception e) {
            throw new RuntimeException("unhandled", e);
        }
    }
}
