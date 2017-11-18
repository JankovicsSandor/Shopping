package sanyi.shop.com.shop;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by Sanyi on 15/10/2017.
 */

public class ItemLoader extends AsyncTaskLoader<ArrayList<ShopItem>> {

    private String url;

    public ItemLoader(Context context,String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        if(ShopFragment.mItemData!=null){
            deliverResult(ShopFragment.mItemData);
        }else{
            forceLoad();
        }
    }

    @Override
    public ArrayList<ShopItem> loadInBackground() {
        ArrayList<ShopItem> shopList=ShopItemsQuery.fetchShopData(url);
        return shopList;
    }

    @Override
    public void deliverResult(ArrayList<ShopItem> data) {
        ShopFragment.mItemData=data;
        super.deliverResult(data);
    }
}
