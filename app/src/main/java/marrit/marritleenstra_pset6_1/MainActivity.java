package marrit.marritleenstra_pset6_1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
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
    User mUser;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // create new fragment
                    HomeFragment homeFragment = new HomeFragment();

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

        // create new fragment
        HomeFragment firstHomeFragment = new HomeFragment();
        // add the fragment to the 'fragment_container' framelayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstHomeFragment).commit();


        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // run recurring alarm
        setRecurringAlarm(MainActivity.this);

    }



    private void setRecurringAlarm(Context context) {

        // set the alarm at approximately 21 o'clock
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 30);
        Log.d("DEBUG", "alarm was set at" + calendar.getTimeInMillis());

        // set action
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // set repeating interval
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingAlarmIntent);
    }


}
