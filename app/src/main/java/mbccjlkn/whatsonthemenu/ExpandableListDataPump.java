package mbccjlkn.whatsonthemenu;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ExpandableListDataPump {
    // Connection to the database
    static DBAccess db = MainActivity.dba;

    // getData()
    // pre: called when the cafeDisplay Activity is opened
    // post: puts correct meal display info into the expandableViewList
    public static HashMap<String, List<String>> getData( int id) {
        // holds the expandableViewLists data
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        // holds each of the eateries meal categories
        ArrayList<String> categories;

        // if the eatery is not a Dining Hall
        if(id < 21) {
            // get the categories from the database
            categories = db.getCategories(id);
        } else {
            // if it is a Dining Hall the categories are Breakfast, lunch, Dinner
            categories = new ArrayList<String>();
            categories.add("Breakfast");
            categories.add("lunch");
            categories.add("Dinner");
        }

        // code to fix a bug
        // BUG: categories show displayed as Breakfast, Dinner, Lunch
        String temp;
        for(int j = 0; j < categories.size(); j++){
            temp = categories.get(j);
            if(temp.equals("lunch")) temp = "Lunch";
            expandableListDetail.put(categories.get(j), db.viewFood(id, temp));
        }

        return expandableListDetail;
    }

    // getFood()
    // pre: called when the user searches in the Search Activity
    // post: puts correct meal display info into the expandableViewList
    public static HashMap<String, List<String>> getFood(ArrayList<ArrayList<String>> food) {
        // holds the expandableViewLists data
        HashMap <String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        // stores each eateryName, indexed by eateryID offset by -1
        String[] eateryNames = {
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

        // adds an index corresponding to one of the 25 eateries
        ArrayList <ArrayList <String>> eateries = new ArrayList <ArrayList <String>>();
        for (int i = 0; i < 25; i++){
            eateries.add(new ArrayList <String>());
        }

        // stores into each eatery meals that applied to the users given tag when they searched
        for (int j = 0; j < food.get(0).size(); j++){
            eateries.get(Arrays.asList(eateryNames).indexOf(food.get(1).get(j))).add(food.get(0).get(j));
        }

        // for each eatery that has at least one applicable meal, put each of its meals onto the expandableViewlist
        for (int i = 0; i < eateries.size(); i++){
            if (eateries.get(i).size() != 0) {
                expandableListDetail.put(eateryNames[i], eateries.get(i));
            }
        }

        return expandableListDetail;
    }

    // getFavoriteFood()
    // pre: called when the FavoritesSelection Activity is opened
    // post: puts correct meal display info into the expandableViewList
    public static HashMap<String, List<String>> getFavoriteFood(ArrayList<ArrayList<String>> meals) {
        HashMap <String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        // stores each eateryName, indexed by eateryID offset by -1
        String[] eateryNames = {
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

        // adds an index corresponding to one of the 25 eateries
        ArrayList <ArrayList <String>> eateries = new ArrayList <ArrayList <String>>();
        for (int i = 0; i < 25; i++){
            eateries.add(new ArrayList <String>());
        }

        // store each favorited meal into its corresponding eatery
        for (int j = 0; j < meals.size(); j++){
            if (meals.get(j).size() != 0) {
                // gets the eatery name
                String eatery = meals.get(j).get(0);

                // gets the meal name
                String food = meals.get(j).get(1);

                // stores it
                eateries.get(Arrays.asList(eateryNames).indexOf(eatery)).add(food);
            }
        }

        // for each eatery that has at least one favorited, put each of its meals onto the expandableViewlist
        for (int i = 0; i < eateries.size(); i++){
            if (eateries.get(i).size() != 0) {
                expandableListDetail.put(eateryNames[i], eateries.get(i));
            }
        }

        return expandableListDetail;
    }
}