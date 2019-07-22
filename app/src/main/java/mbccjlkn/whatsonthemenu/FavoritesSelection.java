package mbccjlkn.whatsonthemenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoritesSelection extends AppCompatActivity {
    private static final String key = "id";
    public int i;
    public static String[] eateryNames = {
            "Cruz N' Gourmet",
            "Drunk Monkey",
            "Raymond's Catering",
            "Banana Joe's (Crown)",
            "Bowls (Porter)",
            "College 8 Cafe",
            "Cowell Coffee Shop",
            "Express Store (Quarry)",
            "Global Village Cafe (Mchenry)",
            "Iveta (Quarry)",
            "Kresge Co-op",
            "Oakes Cafe",
            "Owl's Nest (Kresge)",
            "Perk Coffee (J Baskin)",
            "Perk Coffee (Earth and Marine)",
            "Perk Coffee (Physical Sciences)",
            "Perk Coffee (Terra Fresca)",
            "Stevenson Coffee House",
            "Terra Fresca (College 9/10)",
            "Vivas (Merrill)",
            "College 9/10",
            "Cowell/Stevenson",
            "Crown/Merrill",
            "Porter/Kresge",
            "Rachel Carson/Oakes" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_selection);

        LinearLayout ll = (LinearLayout)findViewById(R.id.FavLL);

        SharedPreferences sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        String spText = sp.getString("Info", "");
        ArrayList<Integer> Fav = new ArrayList<Integer>();

        String[] savedIds = spText.split("-");

        for (int i = 0; i < savedIds.length; i++)
            Fav.add(Integer.parseInt(savedIds[i]));

        Button[] buttons = new Button[Fav.size()];
        Space[] spaces = new Space[Fav.size()];

        for (i = 0; i < buttons.length; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200);

            buttons[i] = new Button(this);
            buttons[i].setText(eateryNames[(Fav.get(i) - 1)]);
            buttons[i].setTextSize(17);
            buttons[i].setTextColor(getResources().getColor(R.color.colorYellow));
            buttons[i].setBackgroundResource(R.drawable.rounded_button);
            buttons[i].setId(Fav.get(i));
            buttons[i].setTag(savedIds[i]);
            ll.addView(buttons[i], params);

            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //if ((int)view.getId() < 21){
                        Intent I = new Intent(FavoritesSelection.this, CafeDisplay.class);
                        I.putExtra("id", (int)view.getId());
                        startActivity(I);
                        //startActivity(new Intent(FavoritesSelection.this, FavoritesSelection.class));
                    /*} else {
                        Intent I = new Intent(FavoritesSelection.this, DiningHallDisplayPage.class);
                        I.putExtra("id", (int)view.getId());
                        startActivity(I);
                        //startActivity(new Intent(FavoritesSelection.this, FavoritesSelection.class));
                    }*/
                }
            });

            spaces[i] = new Space(this);
            spaces[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40));

            ll.addView(spaces[i]);


            // color buttons
            View vg = findViewById(android.R.id.content);
            ArrayList<View> allButtons = vg.getTouchables();

            for (View b: allButtons){
                if(b.getId() != R.id.search_btn && b.getId() != R.id.main_menu_btn && b.getId() != R.id.preferences && b.getId() != R.id.favorite ) {
                    OpenClosedBehavior.colorClosed((Button) b);
                }
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        SharedPreferences sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        String spText = sp.getString("Info", "");
        ArrayList<Integer> Fav = new ArrayList<Integer>();

        String[] savedIds;
        if (spText.equals(""))
            savedIds = new String[0];
        else
            savedIds = spText.split("-");

        if (savedIds.length != 0)
            startActivity(new Intent(FavoritesSelection.this, FavoritesSelection.class));
        finish();
    }

    public void MainMenu(View view) {
        Intent I = new Intent(this,mainMenu.class);
        I.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(I,0);
    }

    public void Search(View view) {
        Intent I = new Intent(this,Search.class);
        startActivity(I);
    }

    public void allLocation(View view) {
       /* Intent I = new Intent(this,Mapview.class);
        startActivity(I);
        int id = 22;
        I.putExtra("id", id);*/
        Intent I = new Intent(this,Mapview.class);
        Bundle k  = new Bundle();
        //final Bundle extras = getIntent().getExtras();
        int current = 60;
        k.putInt(key, current );
        I.putExtras(k);
        startActivity(I);
    }

    public void favorites(View view){

    }
}