package mbccjlkn.whatsonthemenu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;


public class DBAccess {

    private SQLiteOpenHelper openHelper;
    public SQLiteDatabase database;
    private static DBAccess instance;

    // DBAccess()
    // pre: none
    // post: opens connection to the database
    private DBAccess(Context context) {
        this.openHelper = new DBHelper2(context);
    }

    // isOpen()
    // pre: none
    // post: returns true if the database has a steady connection
    public boolean isOpen(){
        return (this.openHelper != null);
    }

    // getInstance()
    // pre: none
    // post: returns a current DBAccess
    public static DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    // open()
    // pre: none
    // post: establishes connection to the database
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }


    // close()
    // pre: none
    // post: closes connection to the database
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    // viewFood()
    // pre: none
    // post: returns all the food from the given eatery under the given category
    public ArrayList<String> viewFood(int location, String cat) {
        // holds the menu items
        ArrayList<String> menu = new ArrayList<>();

        // sql statement to access database
        String filters = "SELECT * FROM Foods WHERE eateryID = " + location + " AND category = '" + cat + "';";

        // cursor executes sql and holds what has been returned
        Cursor cr = database.rawQuery(filters, null);

        // while the cursor still has data
        while(cr.moveToNext()) {
            // temporary String to store the name and price of the food
            String tempString = "";

            // gets the name and price of each food
            for (int j = 2; j < 4; j++) {
                tempString += cr.getString(j);

                if (j == 2) tempString += "\t\t";
            }

            // adds the food to menu
            menu.add(tempString);
        }
        cr.close();

        return menu;
    }

    // getCategories()
    // pre: none
    // post: returns all meal categories of the specified eateryID
    public ArrayList<String> getCategories(int location){
        // holds all meal categories
        ArrayList<String> cats = new ArrayList<>();

        // sql statement to access database
        String filters = "SELECT DISTINCT category FROM Foods WHERE eateryID = "+location+";";

        // cursor executes sql and holds what has been returned
        Cursor cr = database.rawQuery(filters, null);

        // while the cursor still has data, add the categories to cats
        while(cr.moveToNext()) {
            cats.add(cr.getString(0));
        }
        cr.close();
        return cats;
    }

    // viewEatery()
    // pre: none
    // post: returns the specified eateries info from the eateries table
    public ArrayList<String> viewEatery(int id) {
        // holds the eateries data
        ArrayList<String> eatery = new ArrayList<>();

        // sql statement to access database
        String filters = "SELECT * FROM Eateries WHERE id = " + id + ";";

        // cursor executes sql and holds what has been returned
        Cursor cr = database.rawQuery(filters, null);
        cr.moveToNext();

        // add eatery data into the eatery ArrayList
        for (int i = 1; i < 8; i++) {
            eatery.add(cr.getString(i));
        }
        cr.close();
        return eatery;
    }

    // isClosed()
    // pre: none
    // post: returns true id the eatery is closed
    public boolean isClosed(int eateryID) {
        SimpleDateFormat timeFormat;
        timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(Calendar.getInstance().getTime());

        SimpleDateFormat dayFormat; // the day of the week abbreviated
        dayFormat = new SimpleDateFormat("E");
        String day = dayFormat.format(new Date());

        String filters = "SELECT 1 FROM Hours WHERE eateryID = " + eateryID +
                " AND day LIKE '%" + day + "%'" +
                " AND '" + time + "' > open" +
                " AND '" + time + "' < closed;";

        Cursor cr = database.rawQuery(filters, null);

        return !cr.moveToNext();
    }

    // getLocation()
    // pre: none
    // post: returns the specified eateries coordinates
    public String getLocation(int row) {
        // sql statement to access database
        String filters = "SELECT location FROM Eateries WHERE id = " + row + ";";

        // cursor executes sql and holds what has been returned
        Cursor cr = database.rawQuery(filters, null);

        // gets the cursors data
        cr.moveToNext();

        // returns the corrdinates
        return cr.getString(0);
    }

    // addFood()
    // pre:  database must be in swritable state
    // post: adds the specified food to the menu items database table
    public void addFood(int eateryId, String name, String price, String category, String tag){
        // replaces any ' with nothing to not mess up the sql statement
        name = name.replace("'", "");

        // add each value to content values to insert into the database
        ContentValues values = new ContentValues();
        values.put("eateryID", eateryId);
        values.put("name", name);
        values.put("price", price);
        values.put("category", category);
        values.put("tag", tag);

        // insert the values into the Foods table
        database.insert("Foods", null, values);
    }


    // removeDiningHallFood()
    // pre:  database must be in writable state
    // post: removes all dining hall food from the Foods table
    public void removeDiningHallFood(){
        database.delete("Foods", "eateryID > 20", null);
    }

    // getFoodByTag()
    // pre: tag keyword must be given
    // post: searches database for any food that might be similar to the tag keyword
    public ArrayList<ArrayList <String>> getFoodByTag(String keyword){
        // holds the menu item
        ArrayList<ArrayList <String>> menu = new ArrayList<>();

        // holds each eateries name
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

        // sql statement to access database
        String filters = "SELECT name, price, eateryID FROM Foods WHERE name LIKE '%" +keyword+ "%' OR tag LIKE '%" +keyword.replace(" ","")+ "%' OR category LIKE '%" +keyword+ "%';";

        // cursor executes sql and holds what has been returned
        Cursor cr = database.rawQuery(filters, null);

        // holds the names and prices of each meal item
        ArrayList <String> nameAndPrice = new ArrayList <>();

        // holds the eatery names
        ArrayList <String> location = new ArrayList <>();

        // add each menu items name and price and location into their respective ArrayLists
        while(cr.moveToNext()) {
            nameAndPrice.add(cr.getString(0) + "\t\t" + cr.getString(1));
            location.add(eateryNames[Integer.parseInt(cr.getString(2)) - 1]);
        }

        menu.add(nameAndPrice);
        menu.add(location);
        cr.close();
        return menu;
    }

    // getFavoriteFood()
    // pre: given an eatery and food
    // post: checks to see if the eatery is currently serving that food
    public ArrayList<String> getFavoriteFood(String eatery, String food){
        // holds the menu item
        ArrayList <String> menu = new ArrayList <>();

        // holds each eateries name
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

        // converts the given eatery name into its eateryID
        int id = Arrays.asList(eateryNames).indexOf(eatery) + 1;

        // sql statement to access database
        String filters = "SELECT name, price, eateryID FROM Foods WHERE name LIKE '%" +food+ "%' AND eateryID LIKE '" +id+ "';";

        // cursor executes sql and holds what has been returned
        Cursor cr = database.rawQuery(filters, null);

        // if something was returned, i.e its available today, add it to menu
        if (cr.moveToNext()) {
            // adds the eatery and meal name to menu
            menu.add((eateryNames[Integer.parseInt(cr.getString(2)) - 1]));
            menu.add((cr.getString(0) + "\t\t" + cr.getString(1)));
        } else {
            // Logs saying there was no favorite food today
            Log.d("Favorites", "DBAccess: " + "no " + food + " today");
        }
        cr.close();
        return menu;
    }
}