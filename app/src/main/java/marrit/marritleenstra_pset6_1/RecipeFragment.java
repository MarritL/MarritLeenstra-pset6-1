package marrit.marritleenstra_pset6_1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecipeFragment extends Fragment implements RecipeDetailHelper.FragmentCallback {
    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;*/

    public RecipeFragment() {
        // Required empty public constructor
    }

    // declare UI components
    TextView mSourceName;
    TextView mRecipeName;
    TextView mNumberIngredients;
    ImageView mLargeImage;
    TextView mTime;
    ListView mIngredientsList;
    Button mGetDirections;

    // variables
    String mSourceUrl;
    ArrayList<String> mIngredientsArray;
    Recipe recipe;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
    // * @param param1 Parameter 1.
    // * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeFragment.
     */
    /*// TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get recipe details
        recipe = (Recipe) getArguments().getSerializable("RECIPEDATA");

        RecipeDetailHelper recipeDetailHelper = new RecipeDetailHelper(getContext());
        recipeDetailHelper.getRecipeDetails(this, recipe.getId());

        RecipeLab recipeLab = RecipeLab.getInstance();
        recipe = recipeLab.getRecipe(recipe.getId());

        /*// get recipe details
        Recipe recipe = (Recipe) getArguments().getSerializable("RECIPEDATA");

        Toast.makeText(getContext(), "RECIPEFRAGEMENT: clicked item: " + recipe.getRecipeName(), Toast.LENGTH_SHORT).show();

        RecipeDetailHelper recipeDetailHelper = new RecipeDetailHelper(getContext());
        recipeDetailHelper.getRecipeDetails(this, recipe.getId());*/






        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        /*// get recipe details
        recipe = (Recipe) getArguments().getSerializable("RECIPEDATA");

        RecipeDetailHelper recipeDetailHelper = new RecipeDetailHelper(getContext());
        recipeDetailHelper.getRecipeDetails(this, recipe.getId());

        RecipeLab recipeLab = RecipeLab.getInstance();
        recipe = recipeLab.getRecipe(recipe.getId());*/

        // initialise UI components
        // TODO: delete and move to gotRecipes?
        mLargeImage = (ImageView) view.findViewById(R.id.IV_detail_recipe);
        mNumberIngredients = (TextView) view.findViewById(R.id.TV_detail_number_ingredients);
        mRecipeName = (TextView) view.findViewById(R.id.TV_detail_recipename);
        mSourceName = (TextView) view.findViewById(R.id.TV_detail_sourcename);
        mTime = (TextView) view.findViewById(R.id.TV_detail_time);
        mGetDirections = (Button) view.findViewById(R.id.Button_click_for_details);
        mIngredientsList = (ListView) view.findViewById(R.id.LV_detail_ingredientslist);

        // set UI components
        if(!recipe.getLargeImageUrl().equals("")){
        Picasso.with(getContext())
                .load(recipe.getLargeImageUrl())
                .into(mLargeImage);
        }
        mIngredientsArray = recipe.getIngredients();
        mNumberIngredients.setText(String.valueOf(mIngredientsArray.size()));
        mTime.setText(recipe.getTime());
        mRecipeName.setText(recipe.getRecipeName());
        mSourceName.setText(recipe.getSourceName());
        mSourceUrl = recipe.getSourceUrl();

        // initialise list array of ingredients with standard array adapter
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_list_item_1, android.R.id.text1,
                mIngredientsArray);

        mIngredientsList.setAdapter(ingredientsAdapter);

        // attach onClickListener to Button
        mGetDirections.setOnClickListener(new getDirectionsOnClick());



        return view;
    }

    @Override
    public void gotRecipeDetails(Recipe recipe, Context mContext) {

        System.out.println("RECIPEFRAGMENT gotRecipe: " + recipe);

        // update UI
        if(!recipe.getLargeImageUrl().equals("")){
        Picasso.with(getContext())
                .load(recipe.getLargeImageUrl())
                .into(mLargeImage);
        }
        mIngredientsArray = recipe.getIngredients();
        mNumberIngredients.setText(String.valueOf(mIngredientsArray.size()));
        mTime.setText(recipe.getTime());
        mRecipeName.setText(recipe.getRecipeName());
        mSourceName.setText(recipe.getSourceName());
        mSourceUrl = recipe.getSourceUrl();

        // initialise list array of ingredients with standard array adapter
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_list_item_1, android.R.id.text1,
                mIngredientsArray);

        mIngredientsList.setAdapter(ingredientsAdapter);

        // attach onClickListener to Button
        mGetDirections.setOnClickListener(new getDirectionsOnClick());

    }

    @Override
    public void gotError(String message) {
        System.out.println("RECIPEFRAGMENT gotERROR: " + message);

    }

    // class to open the recipe source url
    // source: http://android.okhelp.cz/open-url-with-browser-if-button-clicked-android-example/
    public class getDirectionsOnClick implements Button.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(mSourceUrl));
            startActivity(browserIntent);

        }
    }

   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
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
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    // remember if the app was running already before event like rotation
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ALREADYSTARTED", true);
        savedInstanceState.putSerializable("RECIPE", recipe);
        super.onSaveInstanceState(savedInstanceState);
    }


}
