package mbccjlkn.whatsonthemenu;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class CafeSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_selection);
    }


    @Override
    protected void onResume() {
        super.onResume();

        View vg = findViewById(android.R.id.content);
        ArrayList<View> allButtons = vg.getTouchables();

        for (View b: allButtons){
            if(b.getId() != R.id.search_btn && b.getId() != R.id.main_menu_btn && b.getId() != R.id.preferences) {
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

    public void Preference(View view) {
        Intent I = new Intent(this,Preference.class);
        startActivity(I);
    }

    public void openCafe(View view){
        int id = Integer.parseInt(view.getTag().toString());
        Intent I = new Intent(this, CafeDisplay.class);
        I.putExtra("id", id);
        startActivity(I);
    }
}