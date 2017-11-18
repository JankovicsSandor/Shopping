package sanyi.shop.com.shop.CartItem;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import sanyi.shop.com.shop.CartItem.ItemContract.CartEntry;

/**
 * Created by Sanyi on 14/10/2017.
 */

public class CartProvider extends ContentProvider {


    // code when want to see multiple cart items
    public static final int CART_ALL_ITEM = 100;

    // code when want to see only one cart item
    public static final int CART_ONE_ITEM = 101;

    //content URL to the corresponding code
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_CART, CART_ALL_ITEM);

        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_CART + "/#", CART_ONE_ITEM);
    }

    // Database helper object
    private CartDbHelper mDbhelper;
    private Integer itemNumber;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        mDbhelper = new CartDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        database = mDbhelper.getReadableDatabase();

        // This will hold the reuslt of the query
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            // Want to see all products
            case CART_ALL_ITEM:
                cursor = database.query(CartEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CART_ONE_ITEM:
                selection = CartEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(CartEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            // If neither of the case give a response
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }
        //If data at this uri changes, change on the screen as well
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CART_ALL_ITEM:
                return CartEntry.CONTENT_LIST_TYPE;
            case CART_ONE_ITEM:
                return CartEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown uri " + uri + " with match " + match);
        }
    }

    // No need to check for user input since no user input only button clicks
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        database = mDbhelper.getWritableDatabase();

        long id = database.insert(CartEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e("Not", "Handled");
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        database = mDbhelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CART_ALL_ITEM:
                rowsDeleted = database.delete(CartEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CART_ONE_ITEM:
                selection = CartEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(CartEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // IF any row is deleted change the UI of the element
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CART_ALL_ITEM:
                return UpdateCart(uri, contentValues, selection, selectionArgs);
            case CART_ONE_ITEM:
                selection = CartEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return UpdateCart(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int UpdateCart(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(CartEntry.COLUMN_CART_ITEM_QUANTITY)) {
            itemNumber = values.getAsInteger(CartEntry.COLUMN_CART_ITEM_QUANTITY);
            if (itemNumber == null || !CartEntry.isValidnumber(itemNumber)) {
                throw new IllegalArgumentException("Cart has invalid itemnumber");
            }
        }
        // If no values to be upgraded
        if (values.size() == 0) {
            return 0;
        }
        database = mDbhelper.getWritableDatabase();

        int rowsUpdated = database.update(CartEntry._ID, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
