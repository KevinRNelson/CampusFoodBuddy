package mbccjlkn.whatsonthemenu;

import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CafeDisplay extends AppCompatActivity {
    // Connection to the database
    DBAccess db = MainActivity.dba;
    // connection ti the map floating action button
    FloatingActionButton map;

    public int current;

    // Connections to UI components
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
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
    // pre: called when the CafeDisplay ACtivity is opened
    // post: sets the contents of the CafeDisplay page
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cafe_display);

        // sets the map Floating Action Button
        map = findViewById(R.id.map);

        // sets current equal to the current eateries id
        final Bundle extras = getIntent().getExtras();
        current = extras.getInt("id");

        // sets the size of the linear layout and parent linear layout
        LinearLayout layout = findViewById(R.id.linearLayout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = WRAP_CONTENT;
        layout.setLayoutParams(params);

        LinearLayout layout2 = findViewById(R.id.parentLinearLayout);
        ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
        params2.height = WRAP_CONTENT;
        layout2.setLayoutParams(params2);

        // sets the background image
        ImageButton image = findViewById(R.id.cafeImage);
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

        // gets the speficied eateries info
        ArrayList<String> list = db.viewEatery(current);

        // gets the users favorited eateries from Shared Preferences
        SharedPreferences sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        String spText = sp.getString("Info", "");

        // sets the favorite Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fab);
        ArrayList<Integer> Fav = new ArrayList<Integer>();

        // checks to see if the current eatery is one of the users favorite,
        // and if so colors the star in or leaves it blank
        String[] savedIds;
        if (spText.equals(""))
            savedIds = new String[0];
        else
            savedIds = spText.split("-");

        if (savedIds.length > 0)
            for (int i = 0; i < savedIds.length; i++)
                Fav.add(Integer.parseInt(savedIds[i]));

        if (Fav.contains((Integer) getIntent().getExtras().getInt("id"))) {
            fab.setImageResource(R.drawable.ic_star_favorited);
        } else {
            fab.setImageResource(R.drawable.ic_star_unfavorited);
        }

        // sets connection to the expandableListView
        expandableListView = findViewById(R.id.expandableListView);

        // gets the expandableListViews data, i.e meals
        expandableListDetail = ExpandableListDataPump.getData(current);

        // sets the expandableListViews data
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

        // creates and sets an expandableListAdapter to the expandableListView
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        // sets the OnChildClickListener of the expandableListView, allowing the user to favorite or unfavorite meals
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            // onChildClick
            // pre: gets called when the user clicks on a meal in the cafe display tab
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
                SharedPreferences sp = CafeDisplay.this.getSharedPreferences("WOT", Context.MODE_PRIVATE);
                String text = sp.getString("Meals", "");

                // if the food is not already favorited, favorite it
                if (!text.contains("-" + eateryNames[current - 1] + "_" + food)){
                    text += "-" + eateryNames[current - 1] + "_" + food;
                    Toast.makeText(getApplicationContext(), "Favorited " + food, Toast.LENGTH_SHORT).show();
                } else {
                    // if the food is favorited, unfavorite it
                    text = text.replace("-" + eateryNames[current - 1] + "_" + food, "");
                    Toast.makeText(getApplicationContext(), "Unfavorited " + food, Toast.LENGTH_SHORT).show();
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
                setListViewHeight(parent, groupPosition);
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
                LinearLayout layout = findViewById(R.id.linearLayout);
                ViewGroup.LayoutParams params = layout.getLayoutParams();
                params.height = WRAP_CONTENT;
                layout.setLayoutParams(params);

                LinearLayout layout2 = findViewById(R.id.parentLinearLayout);
                ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
                params2.height = WRAP_CONTENT;
                layout2.setLayoutParams(params2);
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

        // sets title text
        TextView title = findViewById(R.id.cafe_title);
        title.setText(list.get(0));

        // sets hours info
        TextView hours = findViewById(R.id.hour_details);
        hours.setText(list.get(1));

        // gets payment info
        String result = "";
        if (list.get(2).equals("1")) result = result.concat("Card ");
        if (list.get(3).equals("1")) result = result.concat("Flexis ");
        if (list.get(4).equals("1")) result = result.concat("Mealplan ");

        if (result.isEmpty()) result = "Cash Only";

        // displays payment info
        TextView payment = findViewById(R.id.payment_details);
        payment.setText(result);

        // if we display there menu set the header visible, invisible otherwise
        if (current != 7&& current != 8 && current != 11){
            findViewById(R.id.menu_header).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.menu_header).setVisibility(View.INVISIBLE);
        }

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
    // post: opens the Favorites Tab
    public void favorites(View view){
        startActivity(new Intent(this, FavoritesSelection.class));
    }

    // map()
    // pre: onClick for image and map Floating Action Button
    // post: opens Google Maps with directions to the locations
    public void map(View view){
        if (view.getId() == R.id.map){
            Intent intent = new Intent(this, Mapview.class);

            // specify the eateryID to set directions to
            Bundle k  = new Bundle();
            k.putInt("id", current );
            intent.putExtras(k);

            startActivity(intent);
        }
    }


    // favorite()
    // pre: none
    // post: if the eatery is unfavorited then this onClick will favorite it.
    //       if the eatery is favorited then this onClick will unfavorite it.
    public void favorite(View view) {
        // connects to the favorite FLoating Action Button
        FloatingActionButton fab = findViewById(R.id.fab);

        // gets users favorite eateries from Shared Preferences
        SharedPreferences sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        String spText = sp.getString("Info", "");

        // stores users favorites eateries ids
        ArrayList<Integer> Fav = new ArrayList<Integer>();

        // if there are any saved id split them by one id per index, nothing otherwise
        String[] savedIds;
        if (spText.equals(""))
            savedIds = new String[0];
        else {
            savedIds = spText.split("-");
            for (int i = 0; i < savedIds.length; i++)
                Fav.add(Integer.parseInt(savedIds[i]));
        }

        // favorite it if its not favorited
        if (savedIds.length == 0){
            Fav.add(getIntent().getExtras().getInt("id"));
            fab.setImageResource(R.drawable.ic_star_favorited);
            Toast.makeText(getApplicationContext(), "Favorited: " + FavoritesSelection.eateryNames[((Integer) getIntent().getExtras().getInt("id")) - 1], Toast.LENGTH_SHORT).show();
        } else {
            // if fav contains the eatery id than unfavorite the eatery, favorite otherwise
            if (Fav.contains(getIntent().getExtras().getInt("id"))) {
                for (int i = 0; i < Fav.size(); i++) {
                    if (Fav.get(i) == (Integer) getIntent().getExtras().getInt("id")) {
                        Fav.remove(i);
                        fab.setImageResource(R.drawable.ic_star_unfavorited);
                        break;
                    }
                }

                Toast.makeText(getApplicationContext(), "Unfavorited: " + FavoritesSelection.eateryNames[((Integer) getIntent().getExtras().getInt("id")) - 1], Toast.LENGTH_SHORT).show();
            } else {
                Boolean placed = false;

                for (int i = 0; i < Fav.size(); i++) {
                    if (Fav.get(i) > (Integer) getIntent().getExtras().getInt("id")) {
                        Fav.add(i, (Integer) getIntent().getExtras().getInt("id"));
                        placed = true;
                        break;
                    }
                }

                if (!placed) {
                    Fav.add((Integer) getIntent().getExtras().getInt("id"));
                }

                fab.setImageResource(R.drawable.ic_star_favorited);
                Toast.makeText(getApplicationContext(), "Favorited: " + FavoritesSelection.eateryNames[((Integer) getIntent().getExtras().getInt("id")) - 1], Toast.LENGTH_SHORT).show();
            }
        }

        String toAdd = "";
        for (int i = 0; i < Fav.size(); i++){
            if (i == (Fav.size() - 1))
                toAdd += Fav.get(i);
            else
                toAdd += (Fav.get(i) + "-");
        }

        sp.edit().clear().commit();
        sp.edit().putString("Info", toAdd).apply();
    }

    // setListViewHeight()
    // pre:
    // post:
    private void setListViewHeight(ExpandableListView listView, int group) { ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));

        if (height < 10)
            height = 100;

        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}

