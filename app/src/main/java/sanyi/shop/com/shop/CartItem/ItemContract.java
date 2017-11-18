package sanyi.shop.com.shop.CartItem;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sanyi on 14/10/2017.
 */

public class ItemContract {
    private ItemContract(){}

    public static final String CONTENT_AUTHORITY ="sanyi.shop.com.shop";

    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CART ="cart";

    public static final class CartEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_CART);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + PATH_CART;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_CART;

        // Database name for cart items
        public final static String TABLE_NAME="cart";

        public final static String _ID=BaseColumns._ID;

        public final static String COLUMN_CART_ITEM_NO="cikkszam";

        public final static String COLUMN_CART_ITEM_NAME="megnevezes";

        public final static String COLUMN_CART_ITEM_PRICE="ar";

        public final static String COLUMN_CART_ITEM_QUANTITY="darabszam";

        public final static String COLUMN_CART_IMAGE_PATH ="kepUrl";
        public final static String COLUMN_CART_ITEM_DESC ="leiras";

        public final static String COLUMN_NAME_PRICE="sum";

        public static boolean isValidnumber(int price){
            boolean valid=false;
            if (price>0){
                valid=true;
            }
            return valid;
        }
    }
}
