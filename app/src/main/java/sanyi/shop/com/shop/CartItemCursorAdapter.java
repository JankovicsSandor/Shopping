package sanyi.shop.com.shop;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static sanyi.shop.com.shop.CartItem.ItemContract.CartEntry;

/**
 * Created by sando on 20/10/2017.
 */

public class CartItemCursorAdapter extends CursorAdapter {
    //Views for the layout
    ImageView productImage;
    TextView productNameTextView;
    TextView productDescriptionTextView;
    ImageButton deleteButton;
    TextView priceTag;

    String productImagePath;
    String productName;
    String productDescription;
    String decription;

    int price;


    // TODO add + and - buttons to increase and decrease the quantity
    public CartItemCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.cart_one_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        InitialiseAttributes(view,context,cursor);
    }

    private void InitialiseAttributes(View view, final Context context, final Cursor cursor) {

        productImage=view.findViewById(R.id.cartOneItemPicture);
        productNameTextView = view.findViewById(R.id.relativeLayoutNameText);
        productDescriptionTextView =view.findViewById(R.id.relativeLayoutDescriptionText);
        deleteButton=view.findViewById(R.id.deleteFromCartImageButton);
        priceTag=view.findViewById(R.id.finalPriceOfOneItem);


        //TODO implement price tag and quantity as well :) / change XML as well
        //Fetching the values from the SQLITE database into the cartlayout

        productImagePath=cursor.getString(cursor.getColumnIndex(CartEntry.COLUMN_CART_IMAGE_PATH));
        productName=cursor.getString(cursor.getColumnIndex(CartEntry.COLUMN_CART_ITEM_NAME));
        productDescription=cursor.getString(cursor.getColumnIndex(CartEntry.COLUMN_CART_ITEM_DESC));
        decription =cursor.getString(cursor.getColumnIndex(CartEntry.COLUMN_CART_ITEM_DESC));
        price=cursor.getInt(cursor.getColumnIndex(CartEntry.COLUMN_CART_ITEM_PRICE));
        final int positionOfCursor = cursor.getPosition();
        Log.e("image",productImagePath);
        //Setting up the delete action of the item in the cart
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(positionOfCursor);
                final int idOfItem=cursor.getInt(cursor.getColumnIndex(CartEntry._ID));
                //TODO implement an alert dialog(are you sure you want to delete this item from your cart)
                Uri deletedItemUri= ContentUris.withAppendedId(CartEntry.CONTENT_URI,idOfItem);
                // No need for where clause since already made a query for its id
                int rowsAffected=context.getContentResolver().delete(deletedItemUri,null,null);
                if(rowsAffected!=1){
                    Toast.makeText(context,"More than one line was effected",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"One line was effected",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //TODO Set values properly
       // int counter=0;
        productDescriptionTextView.setText(decription);
        productNameTextView.setText(productName);
        priceTag.setText(String.valueOf(price));
        Context context1= productImage.getContext();
        if(context1!=null){
            Picasso.with(context1).load(productImagePath).into(productImage);
        }
    }
}
