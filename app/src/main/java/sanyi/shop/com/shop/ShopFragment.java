package sanyi.shop.com.shop;


import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static sanyi.shop.com.shop.CartItem.ItemContract.CartEntry;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment implements
        LoaderManager.LoaderCallbacks,
        ItemAdapterOnClickHandler {
    private static final String REQUEST_URL = "http://agnesguide.hu/shop.php";
    private static final String QUERY_CATEGORY = "category";
    private static final int CATEGROY_LOADER = 1;
    private static final int ITEMS_LOADER = 2;
    private static final String QUERY_ITEMS = "items";
    public static int numOfNots = 0;
    Toast mToast = null;
    private RecyclerView mRecyclerView;
    private TextView numOfItemsInCartTextView;
    public static ArrayList<ShopItem> mItemData;
    public static List<String> mCategory;
    private ItemAdapter mItemAdapter;

    public ShopFragment() {
        // Required empty public constructor
    }

    public static int numOfItemInCart = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        // Inflate the layout for this fragment
        //Start the loader for the category
        getActivity().getSupportLoaderManager().initLoader(CATEGROY_LOADER, null, this);
        //Finding views by id
        //TODO find out number of elements in the cart badge from the shop
        mRecyclerView = rootView.findViewById(R.id.itemsRecycleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mItemAdapter = new ItemAdapter(this);
        mRecyclerView.setAdapter(mItemAdapter);
        Log.e("Started", "Shop Items");
        getActivity().getSupportLoaderManager().initLoader(ITEMS_LOADER, null, this);
        return rootView;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(REQUEST_URL);
        Uri.Builder builder = uri.buildUpon();
        switch (id) {
            case ITEMS_LOADER:
                builder.appendQueryParameter("q", QUERY_ITEMS);
                return new ItemLoader(getActivity(), builder.toString());
            default:
                return new CategoryLoader(getActivity(), builder.toString());
        }

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case ITEMS_LOADER:
                mItemAdapter.setItemData((ArrayList<ShopItem>) data);
                break;
            default:
                LinearLayout linearLayout = getActivity().findViewById(R.id.shopScrollBar);
                for (final String count : (ArrayList<String>) data) {
                    if (count != null) {
                        // Create new button and add to the layout
                        Button button = new Button(getContext());
                        button.setLayoutParams(new ViewGroup.LayoutParams((int) (getResources().getDimension(R.dimen.scroll_button_shop_width)),
                                (int) (getResources().getDimension(R.dimen.progressBar_height))));
                        button.setText(count);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Delete the excisting toast if shown and replace with the new one
                                if (mToast != null) {
                                    mToast.cancel();
                                }
                                mToast = Toast.makeText(getContext(), count, Toast.LENGTH_SHORT);
                                mToast.show();
                            }
                        });
                        linearLayout.addView(button);
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    //TODO implement proper onclicks
    @Override
    public void onClick(View item) {
        Log.e("item",String.valueOf(mRecyclerView.indexOfChild(item)));
        addtoCart(mRecyclerView.indexOfChild(item));
    }

    // Add the item to the cart
    //TODO implement the proper item adder
    private void addtoCart(int pos) {
        ContentValues values = new ContentValues();
        values.put(CartEntry.COLUMN_CART_IMAGE_PATH,mItemData.get(pos).getPicture());
        values.put(CartEntry.COLUMN_CART_ITEM_NO, mItemData.get(pos).getProductNo());
        values.put(CartEntry.COLUMN_CART_ITEM_NAME, mItemData.get(pos).getName());
        values.put(CartEntry.COLUMN_CART_ITEM_PRICE, mItemData.get(pos).getPrice());
        values.put(CartEntry.COLUMN_CART_ITEM_DESC,mItemData.get(pos).getDetail());
        values.put(CartEntry.COLUMN_CART_ITEM_QUANTITY, 1);
        Uri uri = getContext().getContentResolver().insert(CartEntry.CONTENT_URI, values);
        Log.e("URI WATCHER",CartEntry.CONTENT_URI.toString());
        Log.e("values",values.toString());
        if (uri == null) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }

    }
}
