package mbccjlkn.whatsonthemenu;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBHelper2 extends SQLiteAssetHelper {

    // the databases name
    private static final String DATABASE_NAME = "myDB";
    // the databases version number
    private static final int DATABASE_VERSION = 1;

    // establishes connection to the myDB database
    public DBHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}