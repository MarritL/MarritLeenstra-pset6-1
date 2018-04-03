package marrit.marritleenstra_pset6_1;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecipesHelper implements Response.Listener<JSONObject>, Response.ErrorListener{

    public interface FragmentCallback {
        void gotRecipes(ArrayList<Recipe> recipesArrayList);
        void gotError(String message);
    }



    // declare variables
    private final Context mContext;
    private FragmentCallback mCallback;
    private final String TAG = "RECIPESHELPER";

    // constructor
    RecipesHelper(Context context) {
        mContext = context;
    }

    // constructor
    /*public RecipesHelper(FragmentCallback fragmentCallback, Context context) {
        mCallback = fragmentCallback;
        mContext = context;
    }*/

    // request recipes from yummly API
    void getRecipes(FragmentCallback activity) {
        mCallback = activity;

        // create new queue
        RequestQueue recipesQueue = Volley.newRequestQueue(mContext);

        // create url
        String APIkey = "358842bcce6b938ba3d887ccba20a6a1";
        String APIid = "12ae1b10";
        String mUrl = "http://api.yummly.com/v1/api/recipes?_app_id=" + APIid + "&_app_key="
                + APIkey + "&allowedDiet[]=388^Lacto vegetarian&requirePictures=true";

        // create JSON array request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                mUrl, null, this, this);
        recipesQueue.add(jsonObjectRequest);

    }
    // Handle API response
    @Override
    public void onResponse(JSONObject response) {

        System.out.println(TAG + " response: " + response);

        ArrayList<Recipe> recipesArrayList = new ArrayList<>();

        // get the recipes
        try {
            JSONArray matches = response.getJSONArray("matches");
            System.out.println("matches :" + matches);

            for (int i = 0; i < matches.length(); i++) {
                ArrayList<String> imagesArray = new ArrayList<>();
                JSONObject object = matches.getJSONObject(i);
                String name = object.getString("recipeName");
                Double rating = object.getDouble("rating");
                JSONArray images =  object.getJSONArray("smallImageUrls");

                for (int j = 0; j < images.length(); j++) {
                    imagesArray.add(images.getString(j));
                }

                Recipe recipe = new Recipe(name, rating, imagesArray);
                recipesArrayList.add(recipe);
            }
        } catch (JSONException e) {
            System.out.println("JSONException: " + e.getMessage());

        }

        mCallback.gotRecipes(recipesArrayList);

    }

    // Handle error
    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.gotError(error.getMessage());

    }
}
