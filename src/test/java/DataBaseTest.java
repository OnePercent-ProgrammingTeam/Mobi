import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import gr.aueb.dmst.onepercent.programming.DataBase;

public class DataBaseTest {
    DataBase obj = new DataBase();
    @Test
    public void getDateTimeTest() {
        String result = obj.getDateTime();
        assertNotNull(result);
        String current = LocalDate.now() + " " + LocalTime.now().toString().substring(0, 8);
        assertEquals(result, current);
    }
    @Test 
    public void getImageForSearchTest() {
        ArrayList <String> arr = obj.getImageForSearch();
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(obj.getUrl()); 
            Statement statement = connection.createStatement(); 
                
            String query = "SELECT DISTINCT NAME "
                        + "FROM (SELECT NAME, ID "
                        +        "FROM Image "
                        +        "ORDER BY ID DESC "
                        +        "LIMIT 5)";   
            ResultSet result = statement.executeQuery(query);
            int i = 0;
        
            while (result.next()) {
                String imname = result.getString("NAME");
                assertEquals(arr.get(i), imname);
                i = i + 1;
            }
            statement.close(); 
            connection.close(); 
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        }
    }
}
