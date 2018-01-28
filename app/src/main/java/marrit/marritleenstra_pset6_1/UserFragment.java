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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class UserFragment extends Fragment {

    // declare UI components
    TextView mUserName;
    TextView mRunStreak;
    TextView mTotalDays;
    TextView mTotalAnimals;
    TextView mTotalCO2;


    // declare variables
    FirebaseUser firebaseUser;
    User mUser;
    public String TAG = "USERFRAGMENT";
    private DatabaseReference mDatabase;


    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user, container, false);

        // get User data
        User user = (User) getArguments().getSerializable("USERDATA");

        // initiate UI components
        mUserName = (TextView) v.findViewById(R.id.TV_username);
        mRunStreak = (TextView) v.findViewById(R.id.TV_counter);
        mTotalDays = (TextView) v.findViewById(R.id.TV_number_total_days);
        mTotalAnimals = (TextView) v.findViewById(R.id.TV_number_total_animals);
        mTotalCO2 = (TextView) v.findViewById(R.id.TV_number_total_CO2);

        // Set all UI components
        mUserName.setText(user.getDisplayName());
        mRunStreak.setText(String.valueOf(user.getRunStreak()));
        mTotalDays.setText(String.valueOf(user.getDaysVegetarian()));
        mTotalAnimals.setText(String.valueOf(user.getAnimalsSaved()));
        mTotalCO2.setText(String.valueOf(user.getCO2Avoided()));

        return v;
    }




}