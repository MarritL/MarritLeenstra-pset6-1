package marrit.marritleenstra_pset6_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {

    // Variables
    private Context mContext;
    private ArrayList<Recipe> mRecipeArrayList;

    /*// Constructor
    public GridViewAdapter(Context context, ArrayList<Recipe> recipes) {
        this.mContext = context;
        this.mRecipeArrayList = recipes;
    }*/

    // construct an adapter for the To Listen List
    GridViewAdapter(Context context, int textViewResourceId, ArrayList<Recipe> recipes) {
        super(context, textViewResourceId, recipes);
        this.mRecipeArrayList = recipes;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return mRecipeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecipeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // check to see if the view is null and need to be inflated
        if (v == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, null);
        }

        // iterate through the arrayList
        Recipe i = mRecipeArrayList.get(position);

        if (i != null) {

            // initiate the UI-components
            ImageView IVimageView = (ImageView) v.findViewById(R.id.IV_grid_item);
            TextView TVTitle = (TextView) v.findViewById(R.id.TV_grid_item_title);
            TextView TVSource = (TextView) v.findViewById(R.id.TV_grid_item_source);

            // set components
            TVTitle.setText(i.getRecipeName());
            TVSource.setText(i.getRating().toString());
        }

        return v;
    }
}
