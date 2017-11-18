package sanyi.shop.com.shop.CartItem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import sanyi.shop.com.shop.CartItem.ItemContract.CartEntry;
/**
 * Created by Sanyi on 14/10/2017.
 */

public class CartDbHelper extends SQLiteOpenHelper {

    // Name of database file
    private static final String DATABASE_NAME ="cart.db";

    // Database version.
    private static final int DATABASE_VERSION=1;
    public CartDbHelper(Context context) {super(context,DATABASE_NAME,null, DATABASE_VERSION);}

    //Creating database for SQLITE
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("Created","database");
        String SQL_CREATE_CART_TABLE="CREATE TABLE "+ CartEntry.TABLE_NAME +" ("
                +CartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +CartEntry.COLUMN_CART_IMAGE_PATH + " TEXT, "
                +CartEntry.COLUMN_CART_ITEM_NO + " INTEGER NOT NULL, "
                +CartEntry.COLUMN_CART_ITEM_NAME + " TEXT NOT NULL, "
                +CartEntry.COLUMN_CART_ITEM_PRICE + " INTEGER NOT NULL, "
                +CartEntry.COLUMN_CART_ITEM_DESC+ " TEXT, "
                +CartEntry.COLUMN_CART_ITEM_QUANTITY + " INTEGER NOT NULL);";
        Log.e("Value of db",SQL_CREATE_CART_TABLE);
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
