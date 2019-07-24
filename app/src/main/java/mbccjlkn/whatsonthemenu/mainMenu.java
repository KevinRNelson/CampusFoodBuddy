package mbccjlkn.whatsonthemenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class mainMenu extends AppCompatActivity{
    // Connection to the database
    DBAccess db = MainActivity.dba;

    // onCreate()
    // pre: called when the activity is started
    // post: if the database is not open it is opened
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        if (!db.isOpen()){
            db.open();
        }
    }

    // MainMenu()
    // pre: onClick for the Navigation Bars Menu button
    // post: does nothing because we are already at the MainMenu
    public void MainMenu(View view) {
        // Do nothing
    }

    // Search()
    // pre: onClick for the Navigation Bars Search Tab button
    // post: takes the user to the Search Tab
    public void Search(View view) {
        startActivity(new Intent(this,Search.class));
    }

    // allLocation()
    // pre: onClick for the Navigation Bars Map Tab
    // post: opens Google Maps
    public void allLocation(View view) {
        Intent I = new Intent(this,Mapview.class);
        Bundle k  = new Bundle();
        k.putInt("id", 60);
        I.putExtras(k);
        startActivity(I);
    }

    // favorites()
    // pre: onClick for the Navigation Bars Favorites Tab button
    // post: takes the user to the Favorites Tab
    public void favorites(View view){
        startActivity(new Intent(this, FavoritesSelection.class));
    }


    // openCafe()
    // pre: onClick for each of the Eateries Card View Buttons
    // post: takes the user to the specific Eateries display page
    public void openCafe(View view){
        Intent I = new Intent(this, CafeDisplay.class);

        // add the id to be accesed in the activity
        I.putExtra("id", Integer.parseInt(view.getTag().toString()));

        startActivity(I);
    }
}
