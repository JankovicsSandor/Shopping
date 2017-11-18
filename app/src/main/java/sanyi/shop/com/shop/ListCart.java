package sanyi.shop.com.shop;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import static sanyi.shop.com.shop.CartItem.ItemContract.CartEntry;

public class ListCart extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int ITEM_LOADER = 0;
    CartItemCursorAdapter mAdapter;
    TextView priceTag;
    int finalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cart);
        ListView cartInfoList = (ListView) findViewById(R.id.cartListView);
        View emptyViewOfCart = findViewById(R.id.emptyView);

        //Setting up the emptyView of the cartList so the user can see his cart is empty
        cartInfoList.setEmptyView(emptyViewOfCart);

        mAdapter = new CartItemCursorAdapter(this, null);
        cartInfoList.setAdapter(mAdapter);

        //TODO implement the detail screen of the item
        getSupportLoaderManager().initLoader(ITEM_LOADER, null, this);

        // Calling the method to set the pricetag of the cart based on SQLite database

    }

    //TODO fix bug adding price when delete
    private void getPriceTag(Cursor data) {
        priceTag = (TextView) findViewById(R.id.PriceTagInCart);
        while (data.moveToNext()) {
            finalPrice += data.getInt(data.getColumnIndex(CartEntry.COLUMN_CART_ITEM_PRICE));

            priceTag.setText(String.valueOf(finalPrice) + " FT");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Projections which we are interested in
        String[] projection = {
                CartEntry._ID,
                CartEntry.COLUMN_CART_IMAGE_PATH,
                CartEntry.COLUMN_CART_ITEM_NO,
                CartEntry.COLUMN_CART_ITEM_NAME,
                CartEntry.COLUMN_CART_ITEM_PRICE,
                CartEntry.COLUMN_CART_ITEM_DESC,
                CartEntry.COLUMN_CART_ITEM_QUANTITY};

        // This will call the oncreate method of the Provider
        return new CursorLoader(this,
                CartEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    //TODO implement a sum groupping function
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        getPriceTag(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
