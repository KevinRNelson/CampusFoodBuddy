import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;

public class databaseHelper{
    public static Connection con;

    public static void main(String[] args){
        try {
            con = DriverManager.getConnection("jdbc:sqlite:myDB.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sql = "SELECT * FROM Foods";

        try{
            Statement stmt = con.createStatement(/*ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY*/);
            ResultSet rs = stmt.executeQuery(sql);
            print(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void print(ResultSet rs){
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter("Foods.txt"));
            while (rs.next())
                writer.write(rs.getInt(0) + " " + rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3) + " " + rs.getInt(4) + "\n");

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
