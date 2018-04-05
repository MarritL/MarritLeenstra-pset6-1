package marrit.marritleenstra_pset6_1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.R.attr.data;
import static android.R.attr.value;

public class MainActivity extends FragmentActivity {

    // firebase references
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public String TAG = "MAINACTIVITY";
    private DatabaseReference mDatabase;


    // UI references
    private TextView mTextMessage;

    // variables
    public User mUser;
    boolean mAlarmOn;
    public static final String PREFS_NAME = "MyPrefsFile";
    int mSumDays;
    double mSumAnimals;
    double mSumCO2;
    int mSumParticipantsToday;
    int mSumParticipants;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // create new fragment
                    HomeFragment homeFragment = new HomeFragment();

                    // add User data
                    Bundle data = new Bundle();
                    Log.d(TAG, "to fragment id:" + mUser.getUID());
                    data.putString("USER", mUser.getUID());
                    data.putSerializable("USERDATA", mUser);
                    homeFragment.setArguments(data);

                    // add the fragment to the 'fragment_container' framelayout
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.fragment_container, homeFragment);
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();

                    return true;

                case R.id.navigation_user:
                    // create new fragment
                    UserFragment userFragment = new UserFragment();

                    // add User data
                    Bundle dataUser = new Bundle();
                    dataUser.putSerializable("USERDATA", mUser);
                    userFragment.setArguments(dataUser);

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
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        mAlarmOn = settings.getBoolean("ALARMON", false);
        System.out.println("mAlarmOn3 = " + mAlarmOn);

        // initialise bottom navigation tabs
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        // Read from the database
        if (firebaseUser != null) {

            final String mUid = firebaseUser.getUid();

            mDatabase = FirebaseDatabase.getInstance().getReference();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    // get current user data
                    mUser = dataSnapshot.child("users").child(mUid).getValue(User.class);

                    // display displayName in the bottomNavigation
                    // check again if user is not null (evoked error when user unsubscribed)
                    if(mUser != null) {
                        String mDisplayname = mUser.getDisplayName();
                        navigation.getMenu().findItem(R.id.navigation_user).setTitle(mDisplayname);


                        // on launch the hometab is opened (initiated here, because needs the user data)
                        if (savedInstanceState == null) {
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
                    for (DataSnapshot ds:dataSnapshot.child("users").getChildren()) {

                        //TODO: finish all values (not only ints!)
                        // get values of all users in database
                        int DaysCommunityUser = Integer.valueOf(ds.child("daysVegetarian").getValue().toString());
                        double AnimalsCommunityUser = Double.valueOf(ds.child("animalsSaved").getValue().toString());
                        double CO2CommunityUser = Double.valueOf(ds.child("co2Avoided").getValue().toString());

                        // TODO: make sums!
                        mSumDays = mSumDays + DaysCommunityUser;
                        mSumAnimals = mSumAnimals + AnimalsCommunityUser;
                        mSumCO2 = mSumCO2 + CO2CommunityUser;
                        mSumParticipants += 1;
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

        }


        // run recurring alarms
        System.out.println("mAlarmOn1 =" + mAlarmOn);
        if (!mAlarmOn) {
            setRecurringAlarm(MainActivity.this, 10, 30, AlarmReceiver.class);
            setRecurringAlarm(this, 14, 38, MyNightJobs.class);
            mAlarmOn = true;
            System.out.println("mAlarmOn2 =" + mAlarmOn);
        }


        settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("SAVED", false);
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



    /*// Schedule recurring jobs from the MyNightJobs class (every 24h)
    private void setNightJobs(Context context) {

        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MyNightJobs.class)
                // uniquely identifies the job
                .setTag("my-night-jobs")
                // one-off job
                .setRecurring(true)
                // persist even after a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 24h and 25 hour from now
                .setTrigger(Trigger.executionWindow((int) TimeUnit.HOURS.toSeconds(24),
                        (int) TimeUnit.HOURS.toSeconds(1)))
                .setTrigger(Trigger.executionWindow(0,60)) //temporary
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // run on every network
                        Constraint.ON_ANY_NETWORK
                )
                .build();

        dispatcher.mustSchedule(myJob);
    }*/



    // save preferences
    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        System.out.println("mAlarmOn4 =" + mAlarmOn);
        editor.putBoolean("ALARMON", mAlarmOn);
        editor.commit();

    }

    // remember if the app was running already before event like rotation
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ALREADYSTARTED", true);
        super.onSaveInstanceState(savedInstanceState);
    }

    //TODO: if app not used for one day, runstreak should go to 0 (automatic a no)


    /*Store Arraylist Using Shared Preferences:
    https://stackoverflow.com/questions/22984696/storing-array-list-object-in-sharedpreferences

    SharedPreferences prefs=this.getSharedPreferences("yourPrefsKey",Context.MODE_PRIVATE);
    Editor edit=prefs.edit();

    Set<String> set = new HashSet<String>();
    set.addAll(your Arraylist Name);
    edit.putStringSet("yourKey", set);
    edit.commit();

    Retrieve Arraylist from Shared Preferences

    Set<String> set = prefs.getStringSet("yourKey", null);
    List<String> sample=new ArrayList<String>(set);*/



}
