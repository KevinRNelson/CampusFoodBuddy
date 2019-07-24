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
    private ProgressBar progressBar;
    GetDiningHallMenus getDiningHallMenus;

    @Override
    // onCreate()
    // pre: the app gets opened
    // post: opens connection to the database and starts downloading the dining hall menus in the backgorund
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // opens connection to the database
        dba = DBAccess.getInstance(this);
        dba.open();

        // sets up a connection to the progress bar
        progressBar = findViewById(R.id.progressBar2);
        // sets up a new background thread to get dining hall menus
        getDiningHallMenus = new GetDiningHallMenus(this);

        //startActivity(new Intent(MainActivity.this, mainMenu.class));
        // removes all dining hall food and downloads current days dining hall foods in the background
        dba.removeDiningHallFood();
        getDiningHallMenus.execute();
    }

    // showProgressBar()
    // pre: called on the preExecute of getDiningHallMenus background thread
    // post: sets the progress bar to be visible
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    // dismissProgressBar()
    // pre: called on the postExecute of getDiningHallMenus background thread
    // post: sets the progress bar to be invisible
    private void dismissProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private class GetDiningHallMenus extends AsyncTask<Void, Void, Void> {

        private MainActivity mainActivity;

        // GetDiningHallMenus()
        // pre: given a reference to the MainActivity
        // post: sets up a connection with the MainActivity
        private GetDiningHallMenus(MainActivity mainActivityRef) {
            mainActivity = mainActivityRef;
        }

        @Override
        // onPreExecute()
        // pre: called before doInBackground is called
        // post: sets the progress bar to be visible
        protected void onPreExecute() {
            super.onPreExecute();
            if (mainActivity != null) {
                mainActivity.showProgressBar();
            }
        }

        @Override
        // onProgressUpdate()
        // pre: called while doInBackground is being executed
        // post: shows progress in the progress bar
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate();
        }

        @Override
        // doInBackground()
        // pre: called when the thread is executed
        // post: downloads all dining hall menus, and stores required info in the database
        protected Void doInBackground(Void... voids) {
            String name , tag = "" ;
            String filters = "SELECT * FROM Foods";

            Cursor cr = dba.database.rawQuery(filters, null);
            int i = 0, j = 1;
            while (cr.moveToNext()) i++;
            Log.d("size", i + "");
            cr.close();
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

                    //seperates the scraped document and stores it in the database
                    for (Element food : document.select("div.menusamprecipes,div.menusampmeals,img[src$=.gif]")) {
                        if (food.text().equals("Breakfast") || food.text().equals("Lunch") || food.text().equals("Dinner"))
                            category = food.text();
                        else if (food.text().equals("")) {  //finds the ingredients of the food
                            String temp = food.attr("src");
                            tag += temp.substring(temp.indexOf('/') + 1, temp.indexOf('.')) + " ";
                        } else {
                            name = food.text();
                            dba.addFood(eateryId, name, "", category, tag);
                            Log.d("webscrape", j + ": " + tag);
                            tag = "";
                            Log.d("webscrape", j + ": " + name);
                            Log.d("Foods", name);
                        }
                        j++;
                    }
                } catch (Exception ex) {
                    Log.d("Foods", ex + "");
                    ex.printStackTrace();
                    //used when there is an exception, and we try to scrape the url again
                    //eateryId --;
                }
            }
            Log.d("Foods", "Done: downloading foods");

            cr = dba.database.rawQuery(filters, null);
            i = 0;
            while (cr.moveToNext()) {
                i++;
                //Log.d("Database", cr.getString(0) + " " + cr.getString(1) + " " + cr.getString(2) + " " + cr.getString(3));
            }
            Log.d("size", i + "");
            cr.close();

            Intent I = new Intent(MainActivity.this, mainMenu.class);
            startActivity(I);

            return null;
        }

        @Override
        // onPostExecute()
        // pre: called after doInBackground is executed
        // post: sets the progress bar to be invisible
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mainActivity != null) {
                mainActivity.dismissProgressBar();
            }

            startActivity(new Intent(MainActivity.this, mainMenu.class));
        }
    }
}