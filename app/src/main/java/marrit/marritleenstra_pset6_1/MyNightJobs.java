package marrit.marritleenstra_pset6_1;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobCallback;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static marrit.marritleenstra_pset6_1.MainActivity.PREFS_NAME;

//public class MyNightJobs extends JobService implements RecipesHelper.FragmentCallback {
    public class MyNightJobs extends BroadcastReceiver implements RecipesHelper.Callback {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("MYNIGHTJOBS", "alarm received");

        RecipesHelper recipesHelper = new RecipesHelper(context);
        recipesHelper.getRecipes(this);

    }

    @Override
    public void gotRecipes(ArrayList<Recipe> recipesArrayList, Context context) {

        Log.d("MYNIGHTJOBS", "got recipes");
        saveToSharedPrefs(context, recipesArrayList);

    }

    @Override
    public void gotError(String message) {

        System.out.println("MYNIGHTJOBS gotERROR: " + message);

    }



    /*@Override
    public boolean onStartJob(JobParameters job) {

        // get recipes from Yummly API
        RecipesHelper recipesHelper = new RecipesHelper(getApplicationContext());
        recipesHelper.getRecipes(this);

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true; // Answers the question: "Should this job be retried?"
    }*/


   /* @Override
    public void gotRecipes(ArrayList<Recipe> recipesArrayList) {

        // save to shared prefs
        System.out.println("MYNIGHTJOBS: Got recipes");
        saveToSharedPrefs(getApplicationContext(), recipesArrayList);

    }

    @Override
    public void gotError(String message) {
        System.out.println("Error in retrieving recipes: " + message);
    }*/

    // save ArrayList of recipes to the shared preferences to make them persistent and retrieve
    // them in the HomeFragment. method source:
    // https://stackoverflow.com/questions/22984696/storing-array-list-object-in-sharedpreferences
    public static void saveToSharedPrefs(Context context, ArrayList<Recipe> recipesArrayList) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String jsonRecipes = gson.toJson(recipesArrayList);
        editor.putString("SAVEDRECIPES", jsonRecipes);
        editor.putBoolean("SAVED", true);
        editor.apply();
        System.out.println("MYNIGHTJOBS: saved recipes in arraylist");
    }

    public static ArrayList<Recipe>  retrieveArrayFromSharedPrefs(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

        Gson gson = new Gson();
        String jsonRecipes = settings.getString("SAVEDRECIPES", null);

        if (jsonRecipes != null) {
            Type type = new TypeToken<ArrayList<Recipe>>() {}.getType();
            ArrayList<Recipe> mSavedRecipes = gson.fromJson(jsonRecipes, type);

            return mSavedRecipes;
        }

        return null;
    }

}
