package mbccjlkn.whatsonthemenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DiningHallSelection extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_hall_selection);
    }

    @Override
    protected void onResume() {
        super.onResume();

        View vg = findViewById(android.R.id.content);
        //View notButton = findViewById(R.id.search_btn);
        ArrayList<View> allButtons = vg.getTouchables();

        for (View b: allButtons){
            if(b.getId() != R.id.search_btn && b.getId() != R.id.main_menu_btn) {
                OpenClosedBehavior.colorClosed((Button) b);
            }
        }
    }

    public void MainMenu(View view) {
        Intent I = new Intent(this,MainActivity.class);
        startActivity(I);
    }

    public void Search(View view) {
        Intent I = new Intent(this,Search.class);
        startActivity(I);
    }

    public void openDH(View view){
        int id = Integer.parseInt(view.getTag().toString());
        Intent I = new Intent(this, DiningHallDisplayPage.class);
        I.putExtra("id", id);
        startActivity(I);
    }
}
