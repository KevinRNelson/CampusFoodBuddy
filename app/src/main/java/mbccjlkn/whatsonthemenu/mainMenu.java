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
    DBAccess db = MainActivity.dba;
    private static final String key = "id";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        db.open();
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

    //show all cafe/dinning hall locations
    public void allLocation(View view) {
        Intent I = new Intent(this,Mapview.class);
        Bundle k  = new Bundle();
        //set the key to 60 and save it for next Activities
        int current = 60;
        k.putInt(key, current );
        I.putExtras(k);
        startActivity(I);
    }

    public void openCafe(View view){
        int id = Integer.parseInt(view.getTag().toString());
        Intent I = new Intent(this, CafeDisplay.class);
        I.putExtra("id", id);
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
        startActivity(new Intent(this, FavoritesSelection.class));
    }
}
