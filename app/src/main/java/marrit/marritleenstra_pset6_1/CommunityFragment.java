package marrit.marritleenstra_pset6_1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static java.lang.String.valueOf;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommunityFragment . OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommunityFragment # newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

    // declare UI components
    TextView mTotalDaysCommunity;
    TextView mTotalAnimalsCommunity;
    TextView mTotalCO2Community;
    TextView mTotalParticipantsToday;
    TextView mTotalParticipants;

    // declare variables
    final static private String TAG = "COMMUNITYFRAGMENT";
    //int sumDays = 0;


    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_community, container, false);

        /*// get User data
        User user = (User) getArguments().getSerializable("USERDATA");*/

        // get Community data

        int mSumDays = getArguments().getInt("DAYS");
        double mSumAnimals = getArguments().getDouble("ANIMALS");
        double mSumCO2 = getArguments().getDouble("CO2");
        int mSumParticipants = getArguments().getInt("PARTICIPANTS");

        // initiate UI components
        mTotalDaysCommunity = (TextView) view.findViewById(R.id.TV_number_total_days_community);
        mTotalAnimalsCommunity = (TextView) view.findViewById(R.id.TV_number_total_animals_community);
        mTotalCO2Community = (TextView) view.findViewById(R.id.TV_number_total_CO2_community);
        mTotalParticipantsToday = (TextView) view.findViewById(R.id.TV_number_total_participants_today);
        mTotalParticipants = (TextView) view.findViewById(R.id.TV_number_total_participants);

        // format strings



        // display values in UI
        mTotalDaysCommunity.setText(String.valueOf(mSumDays));
        mTotalAnimalsCommunity.setText(String.format("%.2f", mSumAnimals));
        mTotalCO2Community.setText(String.valueOf(mSumCO2));
        mTotalParticipants.setText(String.valueOf(mSumParticipants));



        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Read from the database
        if (firebaseUser != null) {

            final String mUid = firebaseUser.getUid();

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");




            /*mDatabase.child("users").orderByChild("daysVegetarian").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                    User user = dataSnapshot.getValue(User.class);
                    System.out.println(dataSnapshot.getKey() + " was " + dinosaur.height + " meters tall.");
                }*/


            //TODO: maove to mainActivity
            //mDatabase.child("users").orderByChild("daysVegetarian").addValueEventListener(new ValueEventListener() {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    /*for (DataSnapshot ds:dataSnapshot.getChildren()){
                        int daysInt = Integer.valueOf(ds.child("daysVegetarian").getValue().toString());
                        System.out.println("daysint is:" + daysInt);
                        sumDays = sumDays + daysInt;

                        System.out.println("sumDays is: " + sumDays);
                        //System.out.println("ds is:" +  ds.child("daysVegetarian").getValue());
                        //System.out.println("datasnapshot is: " + dataSnapshot.getValue());
                    }*/


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

                /*@Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    // TODO: query right numbers
                    //Query query = mDatabase.child("users").orderByChild("daysVegetarian");
                    System.out.println(dataSnapshot.getKey());
                    mTotalDaysCommunity.setText(dataSnapshot.getKey());
                    //mUser = dataSnapshot.child("users").child(mUid).getValue(User.class);





                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });*/



        }

        //mTotalDaysCommunity.setText(sumDays);


        return view;
    }



}
