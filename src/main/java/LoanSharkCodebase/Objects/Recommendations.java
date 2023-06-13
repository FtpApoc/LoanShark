package LoanSharkCodebase.Objects;

import LoanSharkCodebase.Singletons.DatabaseSingleton;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Recommendations {
    private ArrayList<String> itemNames = new ArrayList<>();

    public Recommendations() {}


    public Recommendations(ArrayList<String> itemNames) {
        this.itemNames = itemNames;
    }

    public String recommend() throws SQLException {
        DatabaseSingleton databaseInstance = DatabaseSingleton.getInstance();

        for (String item : itemNames) {
            String itemQuery = """
                    Select GenreTag1,GenreTag2,GenreTag3,IsFiction,ReleaseYear from loanshark.item where ItemName = 
                    """ + "\"" + item + "\"";

            ResultSet queryResult = databaseInstance.databaseQuery(itemQuery);
            ArrayList<String> Genres = new ArrayList<>();
            while (queryResult.next()) {

                Genres.add(queryResult.getString("GenreTag1"));
                Genres.add(queryResult.getString("GenreTag2"));
                Genres.add(queryResult.getString("GenreTag3"));
            }

            String comparisonQuery = "select * from loanshark.item";
            ResultSet comparisonOutput = databaseInstance.databaseQuery(comparisonQuery);

            while (comparisonOutput.next()) {
                for(Integer i = 1; i < 3; i++){
                    String compareGenre = comparisonOutput.getString("GenreTag"+i);
                    if (Genres.contains(compareGenre) && !item.equals(comparisonOutput.getString("ItemName"))) {
                        return (comparisonOutput.getString("ItemName"));
                }

                 }
            }
        } return "No Result";
    }
}
