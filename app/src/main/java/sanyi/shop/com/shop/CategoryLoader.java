package sanyi.shop.com.shop;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sanyi on 14/10/2017.
 */

public class CategoryLoader extends AsyncTaskLoader<List<String>> {

    private String url;

    /*
    * category loader type
    *
    * 1 - Loading Categories
    * 2 - Loading Items
    */
    public CategoryLoader(Context context,String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        if(ShopFragment.mCategory!=null){
            deliverResult(ShopFragment.mCategory);
        }else{
            forceLoad();
        }
    }

    @Override
    public List<String> loadInBackground() {
        if (url == null) {
            return null;
        }
        ArrayList<String> categoryList = ShopTableQuery.fetchShopData(url);
        return categoryList;
    }

    @Override
    public void deliverResult(List<String> data) {
        ShopFragment.mCategory=data;
        super.deliverResult(ShopFragment.mCategory);
    }
}
