package marrit.marritleenstra_pset6_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class UserFragment extends Fragment {

    // declare UI components
    TextView mUserName;
    TextView mRunStreak;
    TextView mTotalDays;
    TextView mTotalAnimals;
    TextView mTotalCO2;
    ImageButton mSettings;


    // declare variables
    FirebaseUser firebaseUser;
    User mUser;
    public String TAG = "USERFRAGMENT";
    private DatabaseReference mDatabase;
    double chicken;
    int sheep;
    int pig;
    int calf;
    int horse;
    int cow;


    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user, container, false);

        // get User data
        UserLab userLab = UserLab.getInstance();
        User user = userLab.getUser();
        //User user = (User) getArguments().getSerializable("USERDATA");

        // initiate UI components
        mUserName = (TextView) v.findViewById(R.id.TV_username);
        mRunStreak = (TextView) v.findViewById(R.id.TV_counter);
        mTotalDays = (TextView) v.findViewById(R.id.TV_number_total_days);
        mTotalAnimals = (TextView) v.findViewById(R.id.TV_number_total_animals);
        mTotalCO2 = (TextView) v.findViewById(R.id.TV_number_total_CO2);
        mSettings = (ImageButton) v.findViewById(R.id.IB_settings);

        // Set all UI components
        mUserName.setText(user.getDisplayName());
        mRunStreak.setText(String.valueOf(user.getRunStreak()));
        mTotalDays.setText(String.valueOf(user.getDaysVegetarian()));
        mTotalAnimals.setText(String.format("%.2f", user.getAnimalsSaved()));
        mTotalCO2.setText(String.format("%.1f", user.getCO2Avoided()));

        // get the animals saved
        Bundle animals = calculateAnimals(user.getDaysVegetarian());
        cow = animals.getInt("COW");
        horse = animals.getInt("HORSE");
        calf = animals.getInt("CALF");
        pig = animals.getInt("PIG");
        sheep = animals.getInt("SHEEP");
        chicken = animals.getDouble("CHICKEN");

        System.out.println("USERFRAGMENT: " + cow + horse+ calf+ pig+ sheep+ chicken);

        // set listener to settings button
        mSettings.setOnClickListener(new goToSettings());


        return v;
    }

    public class goToSettings implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            getActivity().getSupportFragmentManager().beginTransaction().remove(UserFragment.this).commit();

            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    // method to calculate how many animals someone safed based on the amount of vegetarian days.
    public Bundle calculateAnimals(int daysVegetarian) {

        // set saved animals to 0
        double chicken = 0;
        int sheep = 0;
        int pig = 0;
        int calf = 0;
        int horse = 0;
        int cow = 0;

        // Days it takes to save an animal
        final int DAYSCHICKEN = 5;
        final int DAYSSHEEP = 53;
        final int DAYSPIG = 238;
        final int DAYSCALF = 390;
        final int DAYSHORSE = 568;
        final int DAYSCOW = 778;


        while(daysVegetarian>0){

            if (daysVegetarian > DAYSCOW){
                daysVegetarian -= DAYSCOW;
                cow += 1;
            }
            else if (daysVegetarian > DAYSHORSE){
                daysVegetarian -= DAYSHORSE;
                horse += 1;
            }
            else if (daysVegetarian > DAYSCALF){
                daysVegetarian -= DAYSCALF;
                calf += 1;
            }
            else if (daysVegetarian > DAYSPIG){
                daysVegetarian -= DAYSPIG;
                pig += 1;
            }
            else if (daysVegetarian > DAYSSHEEP){
                daysVegetarian -= DAYSSHEEP;
                sheep += 1;
            }
            else if (daysVegetarian > DAYSCHICKEN){
                chicken += daysVegetarian/DAYSCHICKEN;
                daysVegetarian = 0;
            }

        }

        Bundle animals = new Bundle();
        animals.putDouble("CHICKEN", chicken);
        animals.putInt("SHEEP", sheep);
        animals.putInt("PIG", pig);
        animals.putInt("CALF", calf);
        animals.putInt("HORSE", horse);
        animals.putInt("COW", cow);

        return animals;
    }
}