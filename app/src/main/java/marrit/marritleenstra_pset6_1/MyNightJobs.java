package marrit.marritleenstra_pset6_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static marrit.marritleenstra_pset6_1.MainActivity.PREFS_NAME;

//public class MyNightJobs extends JobService implements RecipesHelper.FragmentCallback {
    public class MyNightJobs extends BroadcastReceiver implements RecipesHelper.Callback {

        // variables


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("MYNIGHTJOBS", "alarm received");

        RecipesHelper recipesHelper = new RecipesHelper(context);
        recipesHelper.getRecipes(this);

    }

    @Override
    public void gotRecipes(ArrayList<Recipe> recipesArrayList, Context context) {

        Log.d("MYNIGHTJOBS", "got recipes");
        saveToDatabase(recipesArrayList);

    }

    @Override
    public void gotError(String message) {

        System.out.println("MYNIGHTJOBS gotERROR: " + message);

    }





    // save ArrayList of recipes to the database
    public static void saveToDatabase(ArrayList<Recipe> recipesArrayList) {

        Gson gson = new Gson();
        String jsonRecipes = gson.toJson(recipesArrayList);

        // save in database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {
        mDatabase.child("users").child(mUser.getUid()).child("recipes").setValue(jsonRecipes);
        System.out.println("MYNIGHTJOBS: saved recipes in database");
        }
    }

    // cast a string in JSON format (saved like that in database) to a ArrayList
    public static ArrayList<Recipe> castToArray(String stringJSONFormat) {

        Gson gson = new Gson();
        String jsonRecipes = stringJSONFormat;

        if (jsonRecipes != null) {
            Type type = new TypeToken<ArrayList<Recipe>>() {}.getType();
            ArrayList<Recipe> mSavedRecipes = gson.fromJson(jsonRecipes, type);

            return mSavedRecipes;
        }
        return null;
    }

}
