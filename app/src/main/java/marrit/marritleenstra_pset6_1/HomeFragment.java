package marrit.marritleenstra_pset6_1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static marrit.marritleenstra_pset6_1.MainActivity.PREFS_NAME;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link  HomeFragment . OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
/*public class HomeFragment extends Fragment implements RecipesHelper.FragmentCallback {*/
public class HomeFragment extends Fragment {

    // declare UI components
    Button mYesButton;
    Button mNoButton;
    TextView mQuestion;
    TextView mClickedNo;
    TextView mClickedYes;
    public ImageView mIVRecipe;
    private GridView mGridView;

    // declare variables
    ArrayList<Recipe> recipesArrayList;

    // declare Firebase components
    //FirebaseUser mUser;
    User mUser;
    private DatabaseReference mDatabase;

    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;*/

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //* @param param1 Parameter 1.
     //* @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // initiate UI components
        mYesButton = (Button) v.findViewById(R.id.button_yes);
        mNoButton = (Button) v.findViewById(R.id.button_no);
        mQuestion = (TextView) v.findViewById(R.id.TV_question);
        mClickedNo = (TextView) v.findViewById(R.id.TV_clicked_no);
        mClickedYes = (TextView) v.findViewById(R.id.TV_clicked_yes);
        mIVRecipe = (ImageView) v.findViewById(R.id.IV_recipe);
        mGridView = (GridView) v.findViewById(R.id.gridView);

        //mQuestion.setVisibility(View.VISIBLE);
        mClickedNo.setVisibility(View.INVISIBLE);
        mClickedYes.setVisibility(View.INVISIBLE);

        mYesButton.setOnClickListener(new TodayVegetarianClickListener());
        mNoButton.setOnClickListener(new TodayVegetarianClickListener());

        // Retrieve recipes and put in new arrayList
        recipesArrayList = new ArrayList<>();

        User user = (User) getArguments().getSerializable("USERDATA");
        String recipes = user.getRecipes();

        // display recipes in gridview
        if (!recipes.equals("")) {
            recipesArrayList = MyNightJobs.castToArray(recipes);
            GridViewAdapter mAdapter = new GridViewAdapter(getContext(), R.layout.grid_item, recipesArrayList);
            mGridView.setAdapter(mAdapter);

            mGridView.setOnItemClickListener(new MyRecipeClickedListener());
        }




        return v;

    }

   private class updateUI {


   }

    private class TodayVegetarianClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            // get user details
            User user = (User) getArguments().getSerializable("USERDATA");

            // get database reference
            mDatabase = FirebaseDatabase.getInstance().getReference();

            // get the user's values
            String mUID = user.getUID();
            int daysVegetarian = user.getDaysVegetarian();
            int runStreak = user.getRunStreak();
            double co2 = user.getCO2Avoided();
            double animals = user.getAnimalsSaved();

            // update database based on button clicked
            if (view == mYesButton) {
                mDatabase.child("users").child(mUID).child("clickedToday").setValue(true);

                // update user's values
                daysVegetarian++;
                runStreak++;
                System.out.println("CO2 is:" + co2);
                co2 = co2 +1.5;
                //co2 = co2 + 0.5;
                System.out.println("CO2 is now:" + co2);
                animals = animals+0.2;


                // display user's values
                mDatabase.child("users").child(mUID).child("daysVegetarian").setValue(daysVegetarian);
                mDatabase.child("users").child(mUID).child("runStreak").setValue(runStreak);
                mDatabase.child("users").child(mUID).child("co2Avoided").setValue(co2);
                mDatabase.child("users").child(mUID).child("animalsSaved").setValue(animals);

                // replace question till next day
                mQuestion.setVisibility(View.INVISIBLE);
                mClickedYes.setVisibility(View.VISIBLE);

            }
            else if (view == mNoButton) {
                mDatabase.child("users").child(mUID).child("clickedToday").setValue(false);
                mDatabase.child("users").child(mUID).child("runStreak").setValue(0);

                // TODO: replace question till next day
                mQuestion.setVisibility(view.INVISIBLE);
                mClickedNo.setVisibility(View.VISIBLE);
            }

        }
    }

    private class MyRecipeClickedListener implements GridView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            Toast.makeText(getContext(), "clicked item: " + position, Toast.LENGTH_SHORT).show();

            // get the recipe clicked
            Recipe mRecipe = (Recipe) parent.getItemAtPosition(position);

            // create new fragment
            RecipeFragment recipeFragment = new RecipeFragment();

            // add Recipe data
            Bundle dataRecipe = new Bundle();
            dataRecipe.putSerializable("RECIPEDATA", mRecipe);
            recipeFragment.setArguments(dataRecipe);

            // add the fragment to the 'fragment_container' framelayout
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, recipeFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }



    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
