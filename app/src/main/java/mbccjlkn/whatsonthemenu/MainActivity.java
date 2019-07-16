package mbccjlkn.whatsonthemenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MainActivity extends AppCompatActivity {

    public static DBAccess dba;
    boolean getMenus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dba = DBAccess.getInstance(this);
        dba.open();

        if (getMenus) {
            dba.removeDiningHallFood();
            new getDiningHallMenus().execute();
            getMenus = false;
        }
    }



    @Override
    protected void onDestroy() {
        dba.close();
        Log.d("Database", "Closed database connection");
        super.onDestroy();
    }

    public void diningHall(View view){
        Intent I = new Intent(this, DiningHallSelection.class);
        startActivity(I);
    }

    public void Search(View view) {
        Intent I = new Intent(this,Search.class);
        startActivity(I);
    }

    public void MainMenu(View view) {

    }

    public void Preference(View view) {
        Intent I = new Intent(this,Preference.class);
        startActivity(I);
    }

    public void cafe(View view){
        Intent I = new Intent(this, CafeSelection.class);
        startActivity(I);
    }

    public void foodTrucks(View view){
        Intent I = new Intent(this, FoodTruckSelection.class);
        startActivity(I);
    }

    public void favorites(View view){
        SharedPreferences sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        String spText = sp.getString("Info", "");
        ArrayList<Integer> Fav = new ArrayList<Integer>();

        String[] savedIds;
        if (spText.equals(""))
            savedIds = new String[0];
        else
            savedIds = spText.split("-");

        if(savedIds.length == 0)
            Toast.makeText(view.getContext(), "No Favorites To Display", Toast.LENGTH_LONG).show();
        else
            startActivity(new Intent(this, FavoritesSelection.class));
    }

    // getDiningHallMenus()
    // pre: the database has to be opened
    // post: updates the foods table in the database with current menu items
    private class getDiningHallMenus extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String DH[] = new String[5];
            String Name[] = new String[5];
            String name = "", tag = "";
            String filters = "SELECT * FROM Foods";

            Cursor cr = dba.database.rawQuery(filters, null);
            int i = 0, j = 1;
            while (cr.moveToNext()) i++;
            Log.d("size", i + "");

            for (int eateryId = 21; eateryId <= 25; eateryId++) {
                String url = "";

                switch (eateryId) {
                    case 21: // College 9/10
                        url = "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=40&locationName=Colleges+Nine+%26+Ten+Dining+Hall&naFlag=";
                        Log.d("Foods", "9/10");
                        break;
                    case 22: // Cowell/Stevenson
                        url = "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=05&locationName=Cowell+Stevenson+Dining+Hall&naFlag=";
                        Log.d("Foods", "Cowell/Stevenson");
                        break;
                    case 23: // Crown/Merill
                        url = "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=20&locationName=merrill+crown+Dining+Hall&sName=&naFlag=";
                        Log.d("Foods", "Crown/Merill");
                        break;
                    case 24: // Porter/Kresge
                        url = "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=25&locationName=Porter+Kresge+Dining+Hall&naFlag=";
                        Log.d("Foods", "Porter/Kresge");
                        break;
                    case 25: // College 8/Oaks
                        url = "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=30&locationName=Rachel+Carson+Oakes+Dining+Hall&naFlag=";
                        Log.d("Foods", "College 8/Oaks");
                        break;
                    default:
                        break;
                }

                try {
                    Document document = Jsoup.connect(url).get();
                    String category = "";

                    for (Element food : document.select("div.menusamprecipes,div.menusampmeals,img[src$=.gif]")) {
                        if (food.text().equals("Breakfast") || food.text().equals("Lunch") || food.text().equals("Dinner"))
                            category = food.text();
                        else
                            if (food.text().equals("")){  //finds the ingredients of the food
                                String temp = food.attr("src");
                                tag += temp.substring(temp.indexOf('/')+1,temp.indexOf('.')) + " ";
                            } else {
                                if (!name.equals("")){
                                    dba.addFood(eateryId, name, "", category, tag);
                                    Log.d("webscrape", j + ": " + tag);
                                    tag = "";
                                }
                                name = food.text();
                                Log.d("webscrape", j + ": " + name);
                                Log.d("Foods", name);
                            }
                        j++;
                    }
                } catch (Exception ex) {
                    Log.d("Foods", ex + "");
                    ex.printStackTrace();
                }
            }
            Log.d("Foods", "Done: downloading foods");

            cr = dba.database.rawQuery(filters, null);
            i = 0;
            while (cr.moveToNext()){
                i++;
                Log.d("Database", cr.getString(0) + " " + cr.getString(1) + " " + cr.getString(2) + " " + cr.getString(3));
            }
            Log.d("size", i + "");

            return null;

        }

    }
}