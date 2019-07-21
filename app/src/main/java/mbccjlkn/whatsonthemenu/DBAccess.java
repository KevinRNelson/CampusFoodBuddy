package mbccjlkn.whatsonthemenu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBAccess {
    private SQLiteOpenHelper openHelper;
    public SQLiteDatabase database;
    private static DBAccess instance;


    private DBAccess(Context context) {
        this.openHelper = new DBHelper2(context);
    }


    public static DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }


    public void open() {
        this.database = openHelper.getWritableDatabase();
    }


    public void close() {
        if (database != null) {
            this.database.close();
        }
    }


    public ArrayList<String> viewFood(int location, String cat) {
        ArrayList<String> menu = new ArrayList<String>();
        String filters = "SELECT * FROM Foods WHERE eateryID = " + location + " AND category = '" + cat + "';";

        Cursor cr = database.rawQuery(filters, null);

        int i = 0; // Variable to count where we
        while(cr.moveToNext()) {
            String tempString = "";
            for (int j = 2; j < 4; j++) {
                tempString += cr.getString(j);

                if (j == 2) tempString += "\t\t";
            }
            menu.add(tempString);
            i++;
        }

        return menu;
    }

    public ArrayList<String> getCategories(int location){
        ArrayList<String> cats = new ArrayList<String>();
        String filters = "SELECT DISTINCT category FROM Foods WHERE eateryID = "+location+";";
        Cursor cr = database.rawQuery(filters, null);

        while(cr.moveToNext()) {
            cats.add(cr.getString(0));
        }

        return cats;
    }

    public ArrayList<String> viewEatery(int row) {
        ArrayList<String> rowData = new ArrayList<String>();
        String filters = "SELECT * FROM Eateries WHERE id = " + row + ";";
        Cursor cr = database.rawQuery(filters, null);

        cr.moveToNext();

        for (int i = 1; i < 8; i++) {
            rowData.add(cr.getString(i));
        }
        return rowData;
    }

    public String getURL(int row) {
        String url;
        String filters = "SELECT URL FROM Eateries WHERE id = " + row + ";";
        Cursor cr = database.rawQuery(filters, null);
        cr.moveToNext();
        url = cr.getString(0);
        return url;
    }

    public boolean isClosed(int eateryID) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(Calendar.getInstance().getTime());

        SimpleDateFormat dayFormat = new SimpleDateFormat("E"); // the day of the week abbreviated
        String day = dayFormat.format(new Date());

        String filters = "SELECT 1 FROM Hours WHERE eateryID = " + eateryID +
                " AND day LIKE '%" + day + "%'" +
                " AND '" + time + "' > open" +
                " AND '" + time + "' < closed;";

        Cursor cr = database.rawQuery(filters, null);

        return !cr.moveToNext();
    }

    public String getLocation(int row) {
        String location;
        String filters = "SELECT location FROM Eateries WHERE id = " + row + ";";
        Cursor cr = database.rawQuery(filters, null);
        cr.moveToNext();
        location = cr.getString(0);
        return location;
    }

    // addFood()
    // pre:  database must be in swritable state
    // post: adds the specified food to the menu items database table
    public void addFood(int eateryId, String name, String price, String category, String tag){
        name = name.replace("'", "");

        ContentValues values = new ContentValues();

        values.put("eateryID", eateryId);
        values.put("name", name);
        values.put("price", price);
        values.put("category", category);
        values.put("tag", tag);

        database.insert("Foods", null, values);
    }


    // removeDiningHallFood()
    // pre:  database must be in writable state
    // post: removes all dining hall food from the Foods table
    public void removeDiningHallFood(){
        database.delete("Foods", "eateryID > 20", null);
    }

    // getFoodByTag()
    // pre:
    // post:
    public ArrayList<ArrayList <String>> getFoodByTag(String keyword){
        ArrayList<ArrayList <String>> menu = new ArrayList<ArrayList <String>>();

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

        Log.d("Search", keyword);

        String filters = "SELECT name, price, eateryID FROM Foods WHERE name LIKE '%" +keyword+ "%' OR tag LIKE '%" +keyword.replace(" ","")+ "%' OR category LIKE '%" +keyword+ "%';";

        Cursor cr = database.rawQuery(filters, null);

        ArrayList <String> nameAndPrice = new ArrayList <String>();
        ArrayList <String> location = new ArrayList <String>();

        int i = 0;
        while(cr.moveToNext()) {
            nameAndPrice.add(cr.getString(0) + "\t\t" + cr.getString(1));
            location.add(eateryNames[Integer.parseInt(cr.getString(2)) - 1]);
        }

        menu.add(nameAndPrice);
        menu.add(location);

        return menu;
    }
}