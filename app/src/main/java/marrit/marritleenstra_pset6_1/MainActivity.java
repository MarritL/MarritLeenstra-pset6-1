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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static android.R.attr.value;

public class MainActivity extends FragmentActivity {

    // firebase references
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public String TAG = "AUTHENTICATION";
    private DatabaseReference mDatabase;


    // UI references
    private TextView mTextMessage;

    // variables
    public User mUser;
    boolean mAlarmOn;
    public static final String PREFS_NAME = "MyPrefsFile";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // create new fragment
                    HomeFragment homeFragment = new HomeFragment();
                    Bundle data = new Bundle();
                    Log.d(TAG, "to fragment id:" + mUser.getUID());
                    data.putString("USER", mUser.getUID());
                    homeFragment.setArguments(data);

                    // add the fragment to the 'fragment_container' framelayout
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.fragment_container, homeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    return true;

                case R.id.navigation_user:
                    // create new fragment
                    UserFragment userFragment = new UserFragment();

                    // add the fragment to the 'fragment_container' framelayout
                    FragmentTransaction newTransaction = getSupportFragmentManager().beginTransaction();

                    newTransaction.replace(R.id.fragment_container, userFragment);
                    newTransaction.addToBackStack(null);
                    newTransaction.commit();
                    return true;

                case R.id.navigation_community:
                    // create new fragment
                    CommunityFragment communityFragment = new CommunityFragment();

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
    protected void onCreate(Bundle savedInstanceState) {
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
                    mUser = dataSnapshot.child(mUid).getValue(User.class);

                    // display displayName in the bottomNavigation
                    String mDisplayname = mUser.getDisplayName();
                    navigation.getMenu().findItem(R.id.navigation_user).setTitle(mDisplayname);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            // create new fragment
            HomeFragment firstHomeFragment = new HomeFragment();
            Bundle data = new Bundle();
            data.putString("USER", mUid);
            firstHomeFragment.setArguments(data);
            // add the fragment to the 'fragment_container' framelayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstHomeFragment).commit();
        }


        // run recurring alarm
        System.out.println("mAlarmOn1 =" + mAlarmOn);
        if (!mAlarmOn) {
            setRecurringAlarm(MainActivity.this);
            mAlarmOn = true;
            System.out.println("mAlarmOn2 =" + mAlarmOn);
        }

    }


    // schedule daily alarm
    private void setRecurringAlarm(Context context) {

        // set the alarm at approximately 21 o'clock
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 00);
        Log.d("DEBUG", "alarm was set at" + calendar.getTimeInMillis());

        // set action
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // set repeating interval
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingAlarmIntent);
    }


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

}
