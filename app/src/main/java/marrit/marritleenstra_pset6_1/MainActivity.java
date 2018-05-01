package marrit.marritleenstra_pset6_1;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static android.R.attr.data;
import static android.R.attr.value;
import static marrit.marritleenstra_pset6_1.MyNightJobs.saveToDatabase;

public class MainActivity extends FragmentActivity implements RecipesHelper.Callback {

    // firebase references
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public String TAG = "MAINACTIVITY";
    private DatabaseReference mDatabase;


    // UI references
    private TextView mTextMessage;

    // variables
    public User mUser;
    boolean mOnLaunchDone;
    boolean mOnStarted;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String STARTED = "IsStartedBefore";
    //public static RecipeLab recipeLab = RecipeLab.getInstance();
    int mSumDays;
    double mSumAnimals;
    double mSumCO2;
    int mSumParticipantsToday;
    int mSumParticipants;
    //Boolean mClickedToday;
    public static SharedPreferences settings;
    public static SharedPreferences.Editor editor;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // create new fragment
                    HomeFragment homeFragment = new HomeFragment();

                    // add User data
                    //Bundle data = new Bundle();
                    //Log.d(TAG, "to fragment id:" + mUser.getUID());
                    //data.putString("USER", mUser.getUID());
                    //data.putSerializable("USERDATA", mUser);
                    //data.putSerializable("RECIPE", recipes);
                    //homeFragment.setArguments(data);

                    // add the fragment to the 'fragment_container' framelayout
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.fragment_container, homeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //transaction.commitAllowingStateLoss(); // delete account doesn't work with normal .commit()

                    return true;

                case R.id.navigation_user:
                    // create new fragment
                    UserFragment userFragment = new UserFragment();

                    // add User data
                    //Bundle dataUser = new Bundle();
                    //dataUser.putSerializable("USERDATA", mUser);
                    //userFragment.setArguments(dataUser);

                    // add the fragment to the 'fragment_container' framelayout
                    FragmentTransaction newTransaction = getSupportFragmentManager().beginTransaction();

                    newTransaction.replace(R.id.fragment_container, userFragment);
                    newTransaction.addToBackStack(null);
                    newTransaction.commit();
                    return true;

                case R.id.navigation_community:
                    // create new fragment
                    CommunityFragment communityFragment = new CommunityFragment();

                    //TODO: put all sum data in
                    // add community data
                    Bundle dataCommunity = new Bundle();
                    dataCommunity.putInt("DAYS", mSumDays);
                    dataCommunity.putInt("PARTICIPANTS", mSumParticipants);
                    dataCommunity.putDouble("CO2", mSumCO2);
                    dataCommunity.putDouble("ANIMALS", mSumAnimals);
                    dataCommunity.putInt("TODAY", mSumParticipantsToday);
                    communityFragment.setArguments(dataCommunity);

                    // add the fragment to the 'fragment_container' framelayout
                    FragmentTransaction communityTransaction = getSupportFragmentManager().beginTransaction();

                    communityTransaction.replace(R.id.fragment_container, communityFragment);
                    communityTransaction.addToBackStack(null);
                    communityTransaction.commit();

                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        // check if user is signed in.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    // go to sign in page
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        };

        // Restore preferences
        settings = getSharedPreferences(PREFS_NAME, 0);
        mOnLaunchDone = settings.getBoolean("FIRSTLAUNCHDONE", false);
        mOnStarted = settings.getBoolean(STARTED, false);
        System.out.println(TAG + "mFirstLauncheDone3 = " + mOnLaunchDone + " and mOnStarted3 = " + mOnStarted);

        // initialise bottom navigation tabs
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        // Read from the database
        if (firebaseUser != null) {

            // start RecipeLab
            RecipeLab recipeLab = RecipeLab.getInstance();
            recipeLab.fillRecipeArray();

            UserLab userLab = UserLab.getInstance();
            userLab.fillUserData();
            mUser = userLab.getUser();
            // display displayName in the bottomNavigation
            // check again if user is not null (evoked error when user unsubscribed)
            /*if (mUser != null) {
                System.out.print(TAG + "in mUser != null");
                String mDisplayname = mUser.getDisplayName();
                String recipes = mUser.getRecipes();
                int runstreak = mUser.getRunStreak();
                System.out.println("MAINACTIVITY recipes + runstreak: " + recipes + "+" + runstreak);
                navigation.getMenu().findItem(R.id.navigation_user).setTitle(mDisplayname);
            }*/

            /*if (savedInstanceState == null) {
                    Log.d(TAG,"in onDataChange if savedInstancestate is null");
                    navigation.setSelectedItemId(R.id.navigation_home);
            }*/



            /*// Read from the database
            final String mUid = firebaseUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference();*/


            /*mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    // get current user data
                    mUser = dataSnapshot.child("users").child(mUid).getValue(User.class);

                    // display displayName in the bottomNavigation
                    // check again if user is not null (evoked error when user unsubscribed)
                    if (mUser != null) {
                        String mDisplayname = mUser.getDisplayName();
                        String recipes = mUser.getRecipes();
                        int runstreak = mUser.getRunStreak();
                        System.out.println("MAINACTIVITY recipes + runstreak: " + recipes + "+" + runstreak);
                        navigation.getMenu().findItem(R.id.navigation_user).setTitle(mDisplayname);


                        //TODO: after turning the screen errors (if already been in other tab)
                        // on launch the hometab is opened (initiated here, because needs the user data)
                        //mOnStarted = settings.getBoolean(STARTED, false);
                        Log.d(TAG, "isstartedbefore onDataChange is: " + mOnStarted);
                        if (!mOnStarted){
                            Log.d(TAG,"in onDataChange if sharedprefs is null");
                            navigation.setSelectedItemId(R.id.navigation_home);
                            mOnStarted = true;
                        }

                        if (savedInstanceState == null) {
                            Log.d(TAG,"in onDataChange if savedInstancestate is null");
                            navigation.setSelectedItemId(R.id.navigation_home);
                        }
                    }

                    // when data changed set all the community values to 0
                    mSumDays = 0;
                    mSumAnimals = 0;
                    mSumCO2 = 0;
                    mSumParticipantsToday = 0;
                    mSumParticipants = 0;

                    // set the community values
                    for (DataSnapshot ds : dataSnapshot.child("users").getChildren()) {

                        //TODO: finish all values (not only ints!)
                        // get values of all users in database
                        int DaysCommunityUser = Integer.valueOf(ds.child("daysVegetarian").getValue().toString());
                        double AnimalsCommunityUser = Double.valueOf(ds.child("animalsSaved").getValue().toString());
                        double CO2CommunityUser = Double.valueOf(ds.child("co2Avoided").getValue().toString());
                        boolean mClickedToday = Boolean.valueOf(ds.child("clickedToday").getValue().toString());

                        // TODO: make sums!
                        mSumDays = mSumDays + DaysCommunityUser;
                        mSumAnimals = mSumAnimals + AnimalsCommunityUser;
                        mSumCO2 = mSumCO2 + CO2CommunityUser;
                        mSumParticipants += 1;
                        if (mClickedToday){
                            mSumParticipantsToday +=1;
                        }
                    }


                    //Log.d(TAG, "mOnStartedisTrue");
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });*/

        }


        // run everything that has to be done on first time launch
        System.out.println("mFirstLaunchDone1 =" + mOnLaunchDone);
        if (!mOnLaunchDone) {
            setRecurringAlarm(MainActivity.this, 19, 01, AlarmReceiver.class);
            setRecurringAlarm(this, 19, 15, MyNightJobs.class);

            mOnLaunchDone = true;
            System.out.println("mFirstLauncheDone2 =" + mOnLaunchDone);
        }


        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        editor.putBoolean("SAVED", false);
        editor.putBoolean(STARTED, true);
        editor.commit();

        }


    // schedule daily alarms
    private void setRecurringAlarm(Context context, int hour, int minute, Class receiver) {

        // set the alarm at given time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Log.d("DEBUG", "alarm was set at" + calendar.getTimeInMillis());

        // set action
        Intent intent = new Intent(context, receiver);
        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // set repeating interval
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingAlarmIntent);
    }


    // save preferences
    @Override
    protected void onStop() {
        //settings = getSharedPreferences(PREFS_NAME, 0);
        //SharedPreferences.Editor editor = settings.edit();
        System.out.println("mFirstLaunchDone4 =" + mOnLaunchDone);
        editor.putBoolean("FIRSTLAUNCHDONE", mOnLaunchDone);
        Log.d(TAG, "onStop called");
       /* editor.putBoolean(STARTED, false);
        Boolean check = settings.getBoolean(STARTED, false);
        System.out.println(TAG + "is started before: " + check);

        editor.commit();
        Boolean check2 = settings.getBoolean(STARTED, false);
        System.out.println(TAG + "is started before: " + check2);*/

        super.onStop();

    }

    // remember if the app was running already before event like rotation
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        Log.d(TAG,"adding");
        savedInstanceState.putBoolean("ALREADYSTARTED", true);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void gotRecipes(ArrayList<Recipe> recipesArrayList, Context context) {

        Log.d("MAINACTIVITY", "got recipes");
//        public static RecipeLab recipeLab = RecipeLab.getInstance();
        RecipeLab recipeLab = RecipeLab.getInstance();
        recipeLab.safeToDatabase(recipesArrayList);
        //recipeLab.fillRecipeArray();

    }

    @Override
    public void gotError(String message) {

        System.out.println("MAINACTIVITY gotERROR: " + message);

    }






    // make sure that IsStartedBefore is false again, so that the app goes to the home fragment
    // again if the app is restarted
    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy called");
        //settings.edit();
        editor.putBoolean("ISSTARTEDBEFORE", false);
        editor.commit();
        super.onDestroy();
    }




    //TODO: if app not used for one day, runstreak should go to 0 (automatic a no)



}

