package mbccjlkn.whatsonthemenu;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;

public class FavoritesSelection extends AppCompatActivity {
    // Connection to the database
    DBAccess db = MainActivity.dba;
    // Connection to GetFavorites background thread
    GetFavorites getFavorites;

    // Connections to UI components
    LinearLayout HLL;
    List<String> expandableListTitle;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    HashMap<String, List<String>> expandableListDetail;

    // stores all of the dining hall eatery names
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
    // onCreate()
    // pre: called when the user opens the Favorites Tab
    // post: sets components of the Favorites Tab - Card Views for favorite Eateries, ExpandableList for favorite Food
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_selection);

        // gets the users local favorited meals saved in Shared Preference and stores it into a string
        SharedPreferences sp = this.getSharedPreferences("WOT", Context.MODE_PRIVATE);
        String spText = sp.getString("Meals", "");

        // executes the GetFavorites background thread to display the users favorited food in the Expandable list
        getFavorites = new GetFavorites(this);
        getFavorites.execute(spText);

        // sets connection to to HorizalLinearLayout for the CardViews
        HLL = findViewById(R.id.HorizontalLL);

        // gets the users local favorited eateries saved in Shared Preference and stores it into a string
        sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        spText = sp.getString("Info", "");

        // splits the String gotten from Shared Preferences, so each index of this array refers to a specific eateryID
        String[] savedIds = spText.split("-");
        if (savedIds.length > 0 && savedIds[0] != ""){
            // stores the eateryIDs of each of the users favorited eateries
            ArrayList<Integer> Fav = new ArrayList<Integer>();
            for (int i = 0; i < savedIds.length; i++)
                Fav.add(Integer.parseInt(savedIds[i]));

            // cycles through the favorited Eatery Ids and sets the card view for that Eatery
            for (int i = 0; i < Fav.size(); i++) {
                // the current eateryId
                int id = Fav.get(i);
                Log.d("id", id + "");

                // Initialize a new CardView
                CardView card = new CardView(getApplicationContext());

                // Set the CardView and CardView texts layoutParameters
                LayoutParams params = new LayoutParams(
                        700,
                        900
                );
                card.setLayoutParams(params);

                LayoutParams params_text = new LayoutParams(
                        700,
                        400
                );

                // sets the CardView corner radius
                card.setRadius(9);

                // sets the cardViews content padding
                card.setContentPadding(15, 15, 15, 15);

                // sets the CardViews maximum elevation
                card.setMaxCardElevation(15);

                // sets the CardViews elevation
                card.setCardElevation(9);

                // sets the CardViews Tag, i.e the eateryID associated with the image
                card.setTag(id);

                // set the CardViews to be clickable
                card.setClickable(true);

                // creates a new image object
                ImageView image = new ImageView(getApplicationContext());
                image.setLayoutParams(params_text);
                image.setScaleType(ImageView.ScaleType.FIT_XY);

                // gets the file path to the needed eateries image
                AssetManager assetManager = getAssets();
                String file = "images/img"+id+".jpg";

                // sets the image objects image to the required eatery image
                try {
                    InputStream is = assetManager.open(file);
                    Drawable d = Drawable.createFromStream(is, null);

                    image.setImageDrawable(d);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // adds the image to the CardView
                card.addView(image);

                // initializes a new TextView to display the eateries name in the CardView
                TextView tv = new TextView(getApplicationContext());

                // sets the TextViews Layout Parameters
                tv.setLayoutParams(params);

                // sets the TextViews gravity
                tv.setGravity(10000);

                // set the TextViews text to be the required eateries name
                tv.setText(eateryNames[id - 1]);

                // sets the TextViews text alignment
                tv.setTextAlignment(20);

                // sets the TextViews text sixe
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                // sets the TextViews text color
                tv.setTextColor(getResources().getColor(R.color.colorDark));

                // adds the TextView to the CardView
                card.addView(tv);

                // sets the CardViews onClickListener to send the user to the required eateries display page
                card.setOnClickListener(view -> {
                        Intent I = new Intent(FavoritesSelection.this, CafeDisplay.class);
                        I.putExtra("id", (int)view.getTag());
                        startActivity(I);
                });

                // add the CardView to the Horizontal Linear Layout
                HLL.addView(card);
            }

        }
    }

    @Override
    // onRestart()
    // pre: gets called when this Activity restarts
    // post: redisplays the info
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

    // MainMenu()
    // pre: onClick for the Navigation Bars Menu button
    // post: does nothing because we are already at the MainMenu
    public void MainMenu(View view) {
        Intent I = new Intent(this, mainMenu.class);
        I.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(I,0);
    }

    // onCreate()
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
    // post: does nothing because we are already in the favorites tab
    public void favorites(View view){
        // do nothing
    }

    // showFood()
    // pre: gets called after the GetFavoriteFood Background thread finishes execution
    // post: displays users favorited meals that are currently available
    private void showFood(ArrayList<ArrayList <String>> food) {
        // sets connection to the expandableViewList
        expandableListView = findViewById(R.id.expandableListView);

        // makes the expandableViewList visible
        expandableListView.setVisibility(View.VISIBLE);

        // gets the expandableViewLists data, i.e meals
        expandableListDetail = ExpandableListDataPump.getFavoriteFood(food);

        // sets the expandableViewLists data
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

        // creates and sets an expandableListAdapter to the expandableViewList
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        // sets the OnChildClickListener of the expandableViewList, allowing the user to favorite or unfavorite meals
        expandableListView.setOnChildClickListener( new ExpandableListView.OnChildClickListener() {

            @Override
            // onChildClick
            // pre: gets called when the user clicks on a meal in the favorite foods tab
            // post: either favorites or unfavorited food based on if the user has favorited it or not
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // gets the eatery name that the food belongs too
                String eatery = expandableListTitle.get(groupPosition);

                // gets the food that the user clicked on
                String tempFood = expandableListDetail.get(eatery).get(childPosition);

                // takes the food string and siphons just food name out of it
                String food = "";
                for (int i = 0; i < tempFood.length(); i++){
                    //  the name is stored before two /t, so when you get there stop searching
                    if (!tempFood.substring(i, i + 1).equals("\t")){
                        // cycles through each letter of name of the food
                        char c = tempFood.charAt(i);

                        // if the character is a letter or space add it to the food String , no ' , ... allowed
                        if (Character.isLetter(c) || c == ' '){
                            food += c;
                        }
                    } else {
                        break;
                    }
                }

                // gets the users favorited meals from Shared Prefereces
                SharedPreferences sp = FavoritesSelection.this.getSharedPreferences("WOT", Context.MODE_PRIVATE);
                String text = sp.getString("Meals", "");

                // if the food is not already favorited, favorite it
                if (!text.contains("-" + eatery + "_" + food)){
                    text += "-" + eatery + "_" + food;
                    Toast.makeText(getApplicationContext(), "Favorited " + tempFood.substring(0, food.length()), Toast.LENGTH_SHORT).show();
                } else {
                    // if the food is favorited, unfavorite it
                    text = text.replace("-" + eatery + "_" + food, "");
                    Toast.makeText(getApplicationContext(), "Unfavorited " + tempFood.substring(0, food.length()), Toast.LENGTH_SHORT).show();
                }

                // make chanhes to Shared Preferences
                sp.edit().clear().commit();
                sp.edit().putString("Meals", text).apply();

                return false;
            }
        });

        // sets the expandableViewLists onGroupClick
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            // onGroupClick
            // pre: called when the user clicks on an eatery name
            // post: displays what that eatery has
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        // sets the expandableViewLists onGroupExpand
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            // onGroupExpand
            // pre: called when the user clicks on an eatery name
            // post: expands the clicked on eateries section
            public void onGroupExpand(int groupPosition) {

            }
        });

        // sets the expandableViewLists onGroupCollapse
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            // onGroupCollapse
            // pre: called when the user clicks on an eatery name
            // post: collapses the clicked on eateries section
            public void onGroupCollapse(int groupPosition) {

            }
        });
    }

    // GetFavorites
    // preExecute: nothing
    // doInBackground: get all accurate info on favorite dining halls and meals
    // postExecute: set the entire favorites tab display
    private class GetFavorites extends AsyncTask<String, Void, ArrayList <ArrayList <String>>> {

        // FavoritesSelection reference
        protected FavoritesSelection favoritesSelection;

        // getFavorites()
        // pre: given a reference to the FavoritesSelection Activity
        // post: establishes a connection to the FavoritesSelection reference
        public GetFavorites(FavoritesSelection favoritesSelectionRef) {
            favoritesSelection = favoritesSelectionRef;
        }

        @Override
        // onPreExecute()
        // pre: called before doInBackground is Executed()
        // post: does nothing
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        // onProgressUpdate()
        // pre: called when the progress is updated
        // post: does nothing
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate();
        }

        @Override
        // doInBackground()
        // pre: called when GetFavorites gets executed
        // post: gets the users favorited meals that are available today and saves it to be displayed on the pagr
        protected ArrayList <ArrayList <String>> doInBackground(String... food) {
            // Gets info to display for favorite meals
            ArrayList <ArrayList <String>> menu = new ArrayList <ArrayList <String>>();

            // gets each meal from the given Shared Preference string
            String[] meals = food[0].split("-");
            String[] temp;

            /// gets each favorite meal that is available today and adds it to the ArrayList menu, to be displayed
            for (int i = 1; i < meals.length; i++){
                temp = meals[i].split("_");
                if (temp != null) {
                    menu.add( db.getFavoriteFood(temp[0], temp[1]) );
                }
            }

            return menu;
        }

        @Override
        // onPostExecute()
        // pre: called after doInBackground has been executed
        // post: calls showFood to display the info in the expandableList
        protected void onPostExecute(ArrayList <ArrayList <String>> food) {
            super.onPostExecute(food);
            if (favoritesSelection != null) {
                favoritesSelection.showFood(food);
            }
        }

    }
}