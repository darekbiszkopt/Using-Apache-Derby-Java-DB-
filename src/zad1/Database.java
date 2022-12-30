package zad1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection;
    private TravelData travelData;
    private static String driverName = "org.apache.derby.jdbc.EmbeddedDriver";
    private String url;
    private Integer id = 1;

    public Database(String url, TravelData travelData) {
        this.url = url;
        this.travelData = travelData;
    }

    public void create() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url);
            if(connection != null) {
                System.out.println();
                System.out.println("Connected to derby!");
            }
        } catch (SQLException e) {
            System.err.println("Connection with DB exe");
            System.exit(2);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver Class not found");
            e.printStackTrace();
            System.exit(2);
        }

        try {
            connection.createStatement()
                    .execute("CREATE TABLE Oferta("
                    + "id int PRIMARY KEY, "
                    + "kraj varchar(40), "
                    + "data_wyjazdu Date, "
                    + "data_powrotu Date, "
                    + "miejsce varchar(20), "
                    + "cena varchar(20), "
                    + "symbol_waluty varchar(10))"
            );

            PreparedStatement pstmt = connection
                    .prepareStatement("INSERT INTO Oferta VALUES(?,?,?,?,?,?,?)");
            for (String line : travelData.getResList()) {
                String[] tokens = line.split("\\t");

                pstmt.setInt(1, id);
                id++;
                pstmt.setString(2, tokens[0]);
                pstmt.setString(3, tokens[1]);
                pstmt.setString(4, tokens[2]);
                pstmt.setString(5, tokens[3]);
                pstmt.setString(6, tokens[4]);
                pstmt.setString(7, tokens[5]);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("DDL&DML exe");
            e.printStackTrace();
        }
    }

    public void showGui() {
        Statement statment;

        try {
            statment = connection.createStatement();
            ResultSet resSet = statment.executeQuery("SELECT * FROM Oferta");
            ResultSetMetaData resMeta = resSet.getMetaData();
            int columnCount = resMeta.getColumnCount();

            for (int i = 1; i <= columnCount; i++)
                System.out.format("%20s", resMeta.getColumnName(i) + " | ");

            while(resSet.next()) {
                System.out.println("");
                for (int i = 1; i <= columnCount; i++)
                    System.out.format("%20s", resSet.getString(i) + " | ");
            }

            statment.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Show data exe");
            e.printStackTrace();
            System.exit(2);
        }
    }
}
