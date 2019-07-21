package mbccjlkn.whatsonthemenu;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MainActivity extends AppCompatActivity {

    public static DBAccess dba;
    boolean getMenus = true;
    private ProgressBar progressBar;
    GetDiningHallMenus getDiningHallMenus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDiningHallMenus = new GetDiningHallMenus(this);
        progressBar = findViewById(R.id.progressBar2);

        dba = DBAccess.getInstance(this);
        dba.open();

        if (getMenus) {
            dba.removeDiningHallFood();
            getDiningHallMenus.execute();
            getMenus = false;
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    // getDiningHallMenus()
    // pre: the database has to be opened
    // post: updates the foods table in the database with current menu items
    private class GetDiningHallMenus extends AsyncTask<Void, Void, Void> {

        protected MainActivity mainActivity;

        public GetDiningHallMenus(MainActivity mainActivityRef) {
            mainActivity = mainActivityRef;
        }

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
                        else if (food.text().equals("")) {  //finds the ingredients of the food
                            String temp = food.attr("src");
                            tag += temp.substring(temp.indexOf('/') + 1, temp.indexOf('.')) + " ";
                        } else {
                            if (!name.equals("")) {
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
            while (cr.moveToNext()) {
                i++;
                Log.d("Database", cr.getString(0) + " " + cr.getString(1) + " " + cr.getString(2) + " " + cr.getString(3));
            }
            Log.d("size", i + "");

            Intent I = new Intent(MainActivity.this, mainMenu.class);
            startActivity(I);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mainActivity != null) {
                mainActivity.showProgressBar();
            }
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mainActivity != null) {
                mainActivity.dismissProgressBar();
            }

            startActivity(new Intent(MainActivity.this, mainMenu.class));
        }
    }
}