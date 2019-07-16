package mbccjlkn.whatsonthemenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.SearchEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.ArrayList;

public class Search extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_display);
        SearchView myseach = (SearchView) findViewById(R.id.search_food);
        myseach.setQueryHint("Enter food:");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void Search (View view) {

    }

    public void MainMenu(View view) {
        Intent I = new Intent(this,MainActivity.class);
        I.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(I,0);
    }

    public void Preference(View view) {
        Intent I = new Intent(this,Preference.class);
        startActivity(I);
    }

    public void clicked(View view) {
        SearchView myseach = (SearchView) findViewById(R.id.search_food);
        myseach.onActionViewExpanded();
    }
}
