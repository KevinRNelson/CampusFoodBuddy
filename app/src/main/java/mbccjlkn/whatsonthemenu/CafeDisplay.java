package mbccjlkn.whatsonthemenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CafeDisplay extends AppCompatActivity {
    private static final String key = "id";
    DBAccess db = MainActivity.dba;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cafe_display);

        final Bundle extras = getIntent().getExtras();
        int current = extras.getInt("id");

        // Update size of linear layouts
        LinearLayout layout = findViewById(R.id.linearLayout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = WRAP_CONTENT;
        layout.setLayoutParams(params);

        LinearLayout layout2 = findViewById(R.id.parentLinearLayout);
        ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
        params2.height = WRAP_CONTENT;
        layout2.setLayoutParams(params2);

        setContentView(R.layout.activity_cafe_display);

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

        ArrayList<String> list = db.viewEatery(current);

        SharedPreferences sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        String spText = sp.getString("Info", "");
        FloatingActionButton fab = findViewById(R.id.fab);
        ArrayList<Integer> Fav = new ArrayList<Integer>();

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

        expandableListView = findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData(current);
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

                SharedPreferences sp = CafeDisplay.this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
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
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);

                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
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

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        TextView title = findViewById(R.id.cafe_title);
        title.setText(list.get(0));

        TextView hours = findViewById(R.id.hour_details);

        hours.setText(list.get(1));

        String result = "";
        if (list.get(2).equals("1")) result = result.concat("Card ");
        if (list.get(3).equals("1")) result = result.concat("Flexis ");
        if (list.get(4).equals("1")) result = result.concat("Mealplan ");

        if (result.isEmpty()) result = "Cash Only";

        TextView payment = findViewById(R.id.payment_details);
        payment.setText(result);

        if (current != 7&& current != 8 && current != 11){
            findViewById(R.id.menu_header).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.menu_header).setVisibility(View.INVISIBLE);
        }

        //TextView loc = findViewById(R.id.location);
        //loc.setText(db.getLocation(extras.getInt("id")));
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

    public void Preference(View view) {
        Intent I = new Intent(this, Preference.class);
        startActivity(I);
    }

    public void map(View view){
        Intent intent = new Intent(this, Mapview.class);

        Bundle k  = new Bundle();
        final Bundle extras = getIntent().getExtras();
        int current = extras.getInt("id");

        k.putInt(key, current);
        intent.putExtras(k);

        startActivity(intent);
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

    // favorite()
    // pre: none
    // post: if the eatery is unfavorited then this onClick will favorite it.
    //       if the eatery is favorited then this onClick will unfavorite it.
    public void favorite(View view) {
        SharedPreferences sp = this.getSharedPreferences("WOTM", Context.MODE_PRIVATE);
        String spText = sp.getString("Info", "");
        FloatingActionButton fab = findViewById(R.id.fab);

        ArrayList<Integer> Fav = new ArrayList<Integer>();

        String[] savedIds;
        if (spText.equals(""))
            savedIds = new String[0];
        else {
            savedIds = spText.split("-");
            for (int i = 0; i < savedIds.length; i++)
                Fav.add(Integer.parseInt(savedIds[i]));
        }

        if (savedIds.length == 0){
            Fav.add((Integer) getIntent().getExtras().getInt("id"));
            fab.setImageResource(R.drawable.ic_star_favorited);
            Toast.makeText(getApplicationContext(), "Favorited: " + FavoritesSelection.eateryNames[((Integer) getIntent().getExtras().getInt("id")) - 1], Toast.LENGTH_SHORT).show();
        } else {
            // If this eatery is already favorited
            if (Fav.contains((Integer) getIntent().getExtras().getInt("id"))) {
                Toast.makeText(getApplicationContext(), "Unfavorited: " + FavoritesSelection.eateryNames[((Integer) getIntent().getExtras().getInt("id")) - 1], Toast.LENGTH_SHORT).show();
                for (int i = 0; i < Fav.size(); i++) {
                    if (Fav.get(i) == (Integer) getIntent().getExtras().getInt("id")) {
                        Fav.remove(i);
                        fab.setImageResource(R.drawable.ic_star_unfavorited);
                        break;
                    }
                }
                // If this eatery is not already favorited
            } else {
                Toast.makeText(getApplicationContext(), "Favorited: " + FavoritesSelection.eateryNames[((Integer) getIntent().getExtras().getInt("id")) - 1], Toast.LENGTH_SHORT).show();
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

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
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

