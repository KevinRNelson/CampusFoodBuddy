package mbccjlkn.whatsonthemenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Constraints;

import android.text.Layout;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoritesSelection extends AppCompatActivity {
    private static final String key = "id";
    DBAccess db = MainActivity.dba;
    public int i;
    GetFavorites getFavorites;

    LinearLayout HLL;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

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

        HLL = (LinearLayout) findViewById(R.id.HorizontalLL);

        SharedPreferences sp = this.getSharedPreferences("WOT", Context.MODE_PRIVATE);
        String spText = sp.getString("Meals", "");


        getFavorites = new GetFavorites(this);
        getFavorites.execute(spText);

        sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        spText = sp.getString("Info", "");
        ArrayList<Integer> Fav = new ArrayList<Integer>();
        String[] savedIds = spText.split("-");

        if (savedIds.length >= 1){
            if(savedIds[0] == "") {

            } else {
                for (int i = 0; i < savedIds.length; i++)
                    Fav.add(Integer.parseInt(savedIds[i]));

                CardView[] cards = new CardView[Fav.size()];
                Space[] spaces = new Space[Fav.size()];

                for (i = 0; i < cards.length; i++) {
                    int current = Fav.get(i);
                    // Initialize a new CardView
                    CardView card = new CardView(getApplicationContext());

                    // Set the CardView layoutParams
                    LayoutParams params = new LayoutParams(
                            700,
                            900
                    );

                    LayoutParams params_text = new LayoutParams(
                            700,
                            400
                    );

                    card.setLayoutParams(params);

                    // Set CardView corner radius
                    card.setRadius(9);

                    // Set cardView content padding
                    card.setContentPadding(15, 15, 15, 15);

                    // Set a background color for CardView
                    //card.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));

                    // Set the CardView maximum elevation
                    card.setMaxCardElevation(15);

                    // Set CardView elevation
                    card.setCardElevation(9);

                    //set CardView Tag
                    card.setTag(current);

                    //set CardView to be clickable
                    card.setClickable(true);

                    ImageView image = new ImageView(getApplicationContext());
                    image.setLayoutParams(params_text);
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                    AssetManager assetManager = getAssets();
                    String file = "images/img"+current+".jpg";

                    InputStream is = null;
                    try {
                        is = assetManager.open(file);
                        Drawable d = Drawable.createFromStream(is, null);

                        image.setImageDrawable(d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    card.addView(image);

                    // Initialize a new TextView to put in CardView
                    TextView tv = new TextView(getApplicationContext());
                    tv.setLayoutParams(params);
                    tv.setGravity(10000);
                    tv.setText(eateryNames[current - 1]);
                    tv.setTextAlignment(20);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    tv.setTextColor(getResources().getColor(R.color.colorDark));

                    // Put the TextView in CardView
                    card.addView(tv);

                    card.setOnClickListener(view -> {
                        //@Override
                        //public void OnClick(View view) {
                            Intent I = new Intent(FavoritesSelection.this, CafeDisplay.class);
                            I.putExtra("id", (int)view.getTag());
                            startActivity(I);
                        //}
                    });

                    // Finally, add the CardView in root layout

                    HLL.addView(card);
                }
            }
        }

        /*
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
                    }
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
        }*/
    }

    @Override
    public void onRestart() {
        super.onRestart();
/*
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
        finish();*/
    }

    public void MainMenu(View view) {
        Intent I = new Intent(this, mainMenu.class);
        I.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(I,0);
    }

    public void Search(View view) {
        Intent I = new Intent(this, Search.class);
        startActivity(I);
    }

    public void allLocation(View view) {
       /* Intent I = new Intent(this,Mapview.class);
        startActivity(I);
        int id = 22;
        I.putExtra("id", id);*/
        Intent I = new Intent(this, Mapview.class);
        Bundle k  = new Bundle();
        //final Bundle extras = getIntent().getExtras();
        int current = 60;
        k.putInt(key, current );
        I.putExtras(k);
        startActivity(I);
    }

    public void favorites(View view){

    }

    private void showFood(ArrayList<ArrayList <String>> food) {
        expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setVisibility(View.VISIBLE);
        expandableListDetail = ExpandableListDataPump.getFavoriteFood(food);
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("SearchList", groupPosition + " " + childPosition);

                String eatery = expandableListTitle.get(groupPosition);
                String tempFood = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);

                //gets just the food name
                String food = "";
                for (int i = 0; i < tempFood.length(); i++){
                    if (!tempFood.substring(i, i + 1).equals("\t")){
                        char c = tempFood.charAt(i);
                        if (Character.isLetter(c)){
                            food += c;
                        }
                    } else {
                        break;
                    }
                }

                //Intent I = new Intent(FavoritesSelection.this, Search.class);
                //startActivity(I);

                return false;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //Log.d("SearchList", parent + " " + v + " " + groupPosition + " " + id);

                return false;

            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
    }

    // GetFavorites
    // preExecute: nothing
    // doInBackground: get all accurate info on favorite dining halls and meals
    // postExecute: set the entire favorites tab display
    private class GetFavorites extends AsyncTask<String, Void, ArrayList <ArrayList <String>>> {

        protected FavoritesSelection favoritesSelection;

        public GetFavorites(FavoritesSelection favoritesSelectionRef) {
            favoritesSelection = favoritesSelectionRef;
        }

        @Override
        protected ArrayList <ArrayList <String>> doInBackground(String... food) {
            // Gets info to display for favorite meals
            ArrayList <ArrayList <String>> menu = new ArrayList <ArrayList <String>>();

            Log.d("Favorites", food[0]);
            String[] meals = food[0].split("-");
            String[] temp;

            //Log.d("Favorites", "Is db null?: " + (db == null));
            //Log.d("Favorites", "in here111");
            for (int i = 1; i < meals.length; i++){
                temp = meals[i].split("_");
                if (temp != null) {
                    menu.add( db.getFavoriteFood(temp[0], temp[1]) );
                    Log.d("Favorites", "FavoritesSelection: " + temp.length + "");
                    Log.d("Favorites", "FavoritesSelection: " + "eatery: " + temp[0]);
                    Log.d("Favorites", "FavoritesSelection: " + "  food: " + temp[1]);
                }
            }

            return menu;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*if (search != null) {
                search.showProgressBar();
                search.canSearch = false;
            }*/
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(ArrayList <ArrayList <String>> food) {
            super.onPostExecute(food);
            if (favoritesSelection != null) {
                favoritesSelection.showFood(food);
            }
        }

    }
}