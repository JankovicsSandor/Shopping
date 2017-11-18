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

public class ShopTableQuery {

    private static final String LOG_TAG = ShopTableQuery.class.getSimpleName();

    private ShopTableQuery() {
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
    public static ArrayList<String> fetchShopData(String requestUrl) {
        Log.e(LOG_TAG, "Started fetching ");
        URL url = createURl(requestUrl);
        String response = null;
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> categories = extractFeatures(response);

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

    //TODO convert it to simpe array reader from php file
    private static ArrayList<String> extractFeatures(String json) {
        Log.e(LOG_TAG, "Started extracting ");
        ArrayList<String> category = new ArrayList<String>();
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONObject baseJson = new JSONObject(json);
            JSONArray categories = baseJson.getJSONArray("result");

            for (int i = 0; i < categories.length(); i++) {
                JSONObject current = categories.getJSONObject(i);
                if (current.has("Category")) {
                    category.add(current.getString("Category"));
                } else {
                    category.add("NO");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(LOG_TAG, "Finished extracting ");
        return category;
    }
}
