package sanyi.shop.com.shop;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Sanyi on 14/10/2017.
 */

public class ShopItemsQuery {

    private static final String LOG_TAG = ShopItemsQuery.class.getSimpleName();

    private ShopItemsQuery() {
    }

    private static URL createURl(String requestUrl) {
        Log.e(LOG_TAG, "Started Creating ");
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating url", e);
        }
        Log.e(LOG_TAG, "Finished Creating ");
        return url;
    }

    //Gathering informations needed for the query
    public static ArrayList<ShopItem> fetchShopData(String requestUrl) {
        Log.e(LOG_TAG, "Started fetching ");
        URL url = createURl(requestUrl);
        String response = null;
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Want a loader for categories TODO make it clear

        ArrayList<ShopItem> categories = extractFeatures(response);

        Log.e(LOG_TAG, "Finished Fetching ");
        return categories;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        Log.e(LOG_TAG, "Started HTTPRequest ");
        String response = "";
        if (url == null) {
            return response;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Succesfull connection
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Log.e(LOG_TAG, "Finished Httprequ ");
        return response;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        Log.e(LOG_TAG, "Started reading");
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        Log.e(LOG_TAG, "Finished reading ");
        return output.toString();
    }


    private static ArrayList<ShopItem> extractFeatures(String json) {

        Log.e(LOG_TAG, "Started extracting ");
        ArrayList<ShopItem> shopItemList = new ArrayList<ShopItem>();
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONObject baseJson = new JSONObject(json);
            JSONArray shopItems = baseJson.getJSONArray("result");
            int ITEMNO;
            String PICTURE;
            String NAME;
            String DETAIL;
            int PRICE;
            byte isAvailable;
            String CATEGORY;
            for (int i = 0; i < shopItems.length(); i++) {
                JSONObject current = shopItems.getJSONObject(i);
                //Testing if json has the proper fields
                if (current.has("Cikkszam")) {
                    ITEMNO = current.getInt("Cikkszam");
                } else {
                    ITEMNO = 0;
                }
                if (current.has("Kep")) {
                    PICTURE = current.getString("Kep");
                } else {
                    PICTURE = "";
                }
                if (current.has("Megnevezes")) {
                    NAME = current.getString("Megnevezes");
                } else {
                    NAME = "";
                }
                if (current.has("Leiras")) {
                    DETAIL = current.getString("Leiras");
                } else {
                    DETAIL = "";
                }
                if (current.has("Ar")) {
                    PRICE = current.getInt("Ar");
                } else {
                    PRICE = 0;
                }
                if (current.has("Raktaron")) {
                    isAvailable = (byte) current.getInt("Raktaron");
                } else {
                    isAvailable = 0;
                }
                if (current.has("Category")) {
                    CATEGORY = current.getString("Category");
                } else {
                    CATEGORY = "";
                }
                shopItemList.add(new ShopItem(ITEMNO, PICTURE, NAME, DETAIL, PRICE, isAvailable, CATEGORY));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(LOG_TAG, "Finished extractingcat ");
        return shopItemList;
    }
}
