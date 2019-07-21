package mbccjlkn.whatsonthemenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity {

    private ProgressBar progressBar;
    DBAccess db = MainActivity.dba;
    SearchFoodByTag searchFoodByTag;
    boolean canSearch = true;
    ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_display);
        searchFoodByTag = new SearchFoodByTag(this);

        progressBar = findViewById(R.id.progressBar);

        for (int i = 1; i <= 5; i++){
            ImageButton button = findViewById(R.id.button + i);
            //button.bringToFront();
            buttons.add(button);
        }

        SearchView searchText = (SearchView) findViewById(R.id.Searching); // inititate a search view

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 1)
                    Searching(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0){
                    expandableListView = findViewById(R.id.expandableListView);
                    if (expandableListView.getVisibility() == View.VISIBLE){
                        expandableListView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (canSearch){
                        Searching(newText);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void Searching(View view){
        if (canSearch){
            Log.d("Search", "start searching");
            SearchView text = findViewById(R.id.Searching);
            searchFoodByTag = new SearchFoodByTag(this);
            searchFoodByTag.execute(text.getQuery().toString());
        }
    }

    public void Searching(String tag){
        if (canSearch){
            Log.d("Search", "start searching");
            searchFoodByTag = new SearchFoodByTag(this);
            searchFoodByTag.execute(tag);
        }
    }

    public void getFood(View view){
        if (canSearch){
            Log.d("Search", "start searching");
            SearchView text = findViewById(R.id.Searching);
            text.setQuery(view.getTag().toString(), false);
            searchFoodByTag = new SearchFoodByTag(this);
            searchFoodByTag.execute(view.getTag().toString());
        }
    }

    public void options(View view){
        expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setVisibility(View.INVISIBLE);
    }

    public void MainMenu(View view) {
        Intent I = new Intent(this, MainActivity.class);
        startActivity(I);
    }

    public void Search(View view) {
        Intent I = new Intent(this, Search.class);
        startActivity(I);
    }

    public void Preference(View view) {
        Intent I = new Intent(this, Preference.class);
        startActivity(I);
    }

    public void favorites(View view){
        SharedPreferences sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        String spText = sp.getString("Info", "");
        ArrayList<Integer> Fav = new ArrayList<Integer>();

        String[] savedIds;
        if (spText.equals(""))
            savedIds = new String[0];
        else
            savedIds = spText.split("-");

        if(savedIds.length == 0)
            Toast.makeText(view.getContext(), "No Favorites To Display", Toast.LENGTH_LONG).show();
        else
            startActivity(new Intent(this, FavoritesSelection.class));
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showFood(ArrayList<ArrayList <String>> food) {
        expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setVisibility(View.VISIBLE);
        expandableListDetail = ExpandableListDataPump.getFood(food);
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("SearchList", groupPosition + " " + childPosition);

                String eatery = expandableListTitle.get(groupPosition);
                String temp = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);

                String food = "";
                for (int i = 0; i < temp.length(); i++){
                    if (!temp.substring(i, i + 1).equals("\t"))
                        food += temp.substring(i, i + 1);
                    else
                        break;
                }

                SharedPreferences sp = Search.this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
                String text = sp.getString("Meals", "");

                text += "-" + eatery + "_" + food;

                Toast.makeText(getApplicationContext(), "Favorited " + food, Toast.LENGTH_SHORT).show();

                sp.edit().clear().commit();
                sp.edit().putString("Meals", text).apply();

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
                /*
                LinearLayout layout = findViewById(R.id.linearLayout);
                ViewGroup.LayoutParams params = layout.getLayoutParams();
                params.height = WRAP_CONTENT;
                layout.setLayoutParams(params);

                LinearLayout layout2 = findViewById(R.id.parentLinearLayout);
                ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
                params2.height = WRAP_CONTENT;
                layout2.setLayoutParams(params2);

                //setContentView(R.layout.activity_cafe_display);
                */
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
    }

    private class SearchFoodByTag extends AsyncTask<String, Void, ArrayList <ArrayList <String>>> {

        protected Search search;
        ArrayList <ArrayList <String>> food;

        public SearchFoodByTag(Search searchRef) {
            search = searchRef;
        }

        @Override
        protected ArrayList <ArrayList <String>> doInBackground(String... tag) {
            food = db.getFoodByTag(tag[0]);

            for (int i = 0; i < food.get(0).size(); i++){
                Log.d("Search", food.get(0).get(i));
            }

            return food;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (search != null) {
                search.showProgressBar();
                search.canSearch = false;
            }
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate();
        }

        @Override
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