package mbccjlkn.whatsonthemenu;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ExpandableListDataPump {

    static DBAccess db = MainActivity.dba;

    public static HashMap<String, List<String>> getData( int id) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        ArrayList<String> categories;

        String temp;

        if(id < 21) {
            categories = db.getCategories(id);
        } else {
            categories = new ArrayList<String>();
            categories.add("Breakfast");
            categories.add("lunch");
            categories.add("Dinner");
        }

        for(int j = 0; j < categories.size(); j++){
            temp = categories.get(j);
            if(temp.equals("lunch")) temp = "Lunch";
            expandableListDetail.put(categories.get(j), db.viewFood(id, temp));
        }

        return expandableListDetail;
    }

    public static HashMap<String, List<String>> getFood(ArrayList<ArrayList<String>> food) {
        HashMap <String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        //HashSet <String> eateryNames = new HashSet<String>(food.get(2));
        ArrayList <ArrayList <String>> eateries = new ArrayList <ArrayList <String>>();

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

        for (int i = 0; i < 25; i++){
            eateries.add(new ArrayList <String>());
        }

        Log.d("Searching", "" + food.size());
        for(int j = 0; j < food.get(0).size(); j++){
            Log.d("Searching", food.get(1).get(j) + " " + food.get(0).get(j));
            eateries.get(Arrays.asList(eateryNames).indexOf(food.get(1).get(j))).add(food.get(0).get(j));
        }

        for (int i = 0; i < eateries.size(); i++){
            if (eateries.get(i).size() != 0) {
                expandableListDetail.put(eateryNames[i], eateries.get(i));
            }
        }

        return expandableListDetail;
    }

    public static HashMap<String, List<String>> getFavoriteFood(ArrayList<ArrayList<String>> meals) {
        HashMap <String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        ArrayList <ArrayList <String>> eateries = new ArrayList <ArrayList <String>>();

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

        for (int i = 0; i < 25; i++){
            eateries.add(new ArrayList <String>());
        }

        for (int j = 0; j < meals.size(); j++){
            Log.d("Favorites", meals.size() + "");

            if (meals.get(j).size() != 0) {
                String eatery = meals.get(j).get(0);
                String food = meals.get(j).get(1);
                Log.d("Favorites", "XLDP: " + meals.get(j).size());
                Log.d("Favorites", "XLDP: eatery:" + eatery);
                Log.d("Favorites", "XLDP: food:" + food);
                eateries.get(Arrays.asList(eateryNames).indexOf(eatery)).add(food);
            }
        }

        for (int i = 0; i < eateries.size(); i++){
            if (eateries.get(i).size() != 0) {
                expandableListDetail.put(eateryNames[i], eateries.get(i));
            }
        }

        return expandableListDetail;
    }
}