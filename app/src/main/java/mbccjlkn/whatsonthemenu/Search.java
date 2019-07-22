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
    private static final String key = "id";
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
        Intent I = new Intent(this, mainMenu.class);
        I.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(I);
    }

    public void Search(View view) {
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
                String tempFood = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);

                //gets just the food name
                String food = "";
                for (int i = 0; i < tempFood.length(); i++){
                    if (!tempFood.substring(i, i + 1).equals("\t")){
                        char c = tempFood.charAt(i);
                        if (Character.isLetter(c) || c == ' '){
                            food += c;
                        } else {
                            food += "$";
                        }
                    } else {
                        break;
                    }
                }

                SharedPreferences sp = Search.this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
                String text = sp.getString("Meals", "");

                if (!text.contains("-" + eatery + "_" + food)){
                    text += "-" + eatery + "_" + food;
                    Toast.makeText(getApplicationContext(), "Favorited " + tempFood.substring(0, food.length()), Toast.LENGTH_SHORT).show();
                } else {
                    text = text.replace("-" + eatery + "_" + food, "");
                    Toast.makeText(getApplicationContext(), "Unfavorited " + tempFood.substring(0, food.length()), Toast.LENGTH_SHORT).show();
                }

                Log.d("Favorites", "" + (text.length() - text.replace("-", "").length()));

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
            Log.d("Favorites", "tag:" + tag[0]);
            Log.d("Favorites", "food:" + food);
            food = db.getFoodByTag(tag[0]);

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
