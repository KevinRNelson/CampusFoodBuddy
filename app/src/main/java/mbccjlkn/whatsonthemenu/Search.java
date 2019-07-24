package mbccjlkn.whatsonthemenu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Search extends AppCompatActivity {
    // Connection to the database
    DBAccess db = MainActivity.dba;
    // Connection to the SearchFoodByTag Background thread
    SearchFoodByTag searchFoodByTag;
    // connection to the progress bar
    private ProgressBar progressBar;
    // holds value to tell if we can are done searching and can search again
    boolean canSearch = true;

    // Connections to UI components
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    // onCreate()
    // pre: called when the user opens the Search Activity
    // post: sets components of the Search tab
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_display);

        // executes the SearchFoodByTag background thread to display the meals that apply to the desired tag
        searchFoodByTag = new SearchFoodByTag(this);

        // sets up a connection to the progress bar
        progressBar = findViewById(R.id.progressBar);

        // sets up a connection to the SearchView
        SearchView searchText = findViewById(R.id.Searching); // inititate a search view

        // sets the SearchViews onQueryTextSubmit
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            // onQueryTextSubmit()
            // pre: user enters text
            // post: search database for desired tag
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 1)
                    Searching(query);
                return false;
            }

            @Override
            // onQueryTextChange()
            // pre: user changes text
            // post: if there is no more text set the expandableListView invisible
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0){
                    // sets connection to the expandableListView
                    expandableListView = findViewById(R.id.expandableListView);

                    // set invisible
                    if (expandableListView.getVisibility() == View.VISIBLE){
                        expandableListView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    // allows continuous searching as you are typing
                    if (canSearch){
                        Searching(newText);
                    }
                }
                return false;
            }
        });
    }

    // Searching()
    // pre: takes in a desired tag to search by
    // post: searches the database by the tag
    public void Searching(String tag){
        if (canSearch){
            searchFoodByTag = new SearchFoodByTag(this);
            searchFoodByTag.execute(tag);
        }
    }

    // getFood()
    // pre: onClick for the 5 preset images
    // post: searches the specified tag
    public void getFood(View view){
        if (canSearch){
            // sets the SearchViews text the image name
            SearchView text = findViewById(R.id.Searching);
            text.setQuery(view.getTag().toString(), false);

            // executes the SearchFoodByTag Background Thread
            searchFoodByTag = new SearchFoodByTag(this);
            searchFoodByTag.execute(view.getTag().toString());
        }
    }

    // options()
    // pre: onClock for the search bar
    // post: sets the expandableListView to invisible
    public void options(View view){
        expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setVisibility(View.INVISIBLE);
    }

    // MainMenu()
    // pre: onClick for the Navigation Bars Menu button
    // post: does nothing because we are already at the MainMenu
    public void MainMenu(View view) {
        Intent I = new Intent(this, mainMenu.class);
        I.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(I);
    }

    // onCreate()
    // pre: onClick for the Navigation Bars Search Tab button
    // post: does nothing, because the user is already at the Search Activity
    public void Search(View view) {

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
        startActivity(new Intent(this, FavoritesSelection.class));
    }

    // showProgressBar()
    // pre: called during the SearchFoodByTag onPreExecute
    // post: sets the progress bar to visible
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    // dismissProgressBar()
    // pre: called during the SearchFoodByTag onPostExecute
    // post: sets the progress bar to invisible
    private void dismissProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    // showFood()
    // pre: gets called after the SearchFoodByTag Background thread finishes execution
    // post: displays all food that applied to the given tag
    private void showFood(ArrayList<ArrayList <String>> food) {
        // sets connection to the expandableViewList
        expandableListView = findViewById(R.id.expandableListView);

        // makes the expandableViewList visible
        expandableListView.setVisibility(View.VISIBLE);

        // gets the expandableViewLists data, i.e meals
        expandableListDetail = ExpandableListDataPump.getFood(food);

        // sets the expandableViewLists data
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

        // creates and sets an expandableListAdapter to the expandableViewList
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        // sets the OnChildClickListener of the expandableViewList, allowing the user to favorite or unfavorite meals
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            // onChildClick
            // pre: gets called when the user clicks on a meal
            // post: either favorites or unfavorited food based on if the user has favorited it or not
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // gets the eatery name that the food belongs too
                String eatery = expandableListTitle.get(groupPosition);

                // gets the food that the user clicked on
                String tempFood = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);

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
                        } else {
                            food += "$";
                        }
                    } else {
                        break;
                    }
                }

                // gets the users favorited meals from Shared Prefereces
                SharedPreferences sp = Search.this.getSharedPreferences("WOT", Context.MODE_PRIVATE);
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

    private class SearchFoodByTag extends AsyncTask<String, Void, ArrayList <ArrayList <String>>> {

        protected Search search;
        ArrayList <ArrayList <String>> food;

        // SearchFoodByTag()
        // pre: given a reference to the Search Activity
        // post: establishes a connection to the Search reference
        public SearchFoodByTag(Search searchRef) {
            search = searchRef;
        }

        @Override
        // onPreExecute()
        // pre: called before doInBackground is Executed()
        // post: does nothing
        protected void onPreExecute() {
            super.onPreExecute();
            if (search != null) {
                search.showProgressBar();
                search.canSearch = false;
            }
        }

        @Override
        // onProgressUpdate()
        // pre: called when the progress is updated
        // post: updates the progress bar
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate();
        }

        @Override
        // doInBackground()
        // pre: called when SearchByFoodTag gets executed
        // post: gets the meals that are applicable to the specified tag
        protected ArrayList <ArrayList <String>> doInBackground(String... tag) {
            food = db.getFoodByTag(tag[0]);

            return food;
        }

        @Override
        // onPostExecute()
        // pre: called after doInBackground has been executed
        // post: calls showFood to display the info in the expandableList, and dismisses the progress bar
        protected void onPostExecute(ArrayList <ArrayList <String>> food) {
            super.onPostExecute(food);
            if (search != null) {
                search.dismissProgressBar();
                search.showFood(food);
                search.canSearch = true;
            }
        }

    }
}
