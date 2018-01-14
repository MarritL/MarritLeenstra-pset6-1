package marrit.marritleenstra_pset6_1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class UserFragment extends Fragment {

    // declare UI components
    TextView mUserName;
    FirebaseUser mUser;


    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user, container, false);

        // initiate UI components
        mUserName = (TextView) v.findViewById(R.id.TV_username);

        // get email from account
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            String mName = mUser.getEmail();
            mUserName.setText(mName);
        }

        return v;
    }

    public void addVegetarianDay() {

        // write to the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = mDatabase.getReference();
        //myRef.setValue("Hello, World!");

        //mDatabase.child
    }

}