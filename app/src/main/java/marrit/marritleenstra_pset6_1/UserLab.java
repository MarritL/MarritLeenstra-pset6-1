package marrit.marritleenstra_pset6_1;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Marrit on 15-1-2018.
 * The User lab contains all the methods to manipulate the data stash for User objects in
 * a singleton structure.
 */

public class UserLab {

    // variables
    private static UserLab sUserLab;
    private static DatabaseReference mDatabase;
    private static FirebaseUser mFirebaseUser;
    private static User mUser;

    // create instance
    public static UserLab getInstance() {
        System.out.println("USERLAB: Userlab getInstance called");
        if (sUserLab == null){
            sUserLab = new UserLab();
        }
        return sUserLab;
    }

    // create a new recipes ArrayList
    private UserLab() {
        System.out.println("USERLAB: made new UserLab()");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public User getUser(){
        System.out.print("USERLAB: getUser() called");
        return mUser;
    }

    // fill the ArrayList with the recipes from the database
    public void fillUserData() {
        System.out.println("USERDATA: getUserData() called");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mFirebaseUser != null) {
                    String uid = mFirebaseUser.getUid();
                    mUser = dataSnapshot.child("users").child(uid).getValue(User.class);

                    System.out.println("USERLAB: done with fillUserData()");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("RECIPELAB", "Failed to read value.", databaseError.toException());

            }
        });
    }


}
