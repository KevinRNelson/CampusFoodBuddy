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
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class mainMenu extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

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
}