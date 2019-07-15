package mbccjlkn.whatsonthemenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;

public class Preference extends  AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_display);
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    }
}
