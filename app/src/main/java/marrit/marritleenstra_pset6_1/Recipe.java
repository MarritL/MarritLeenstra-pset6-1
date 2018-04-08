package marrit.marritleenstra_pset6_1;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Recipe implements Serializable {

    private String mRecipeName;
    private Double mRating;
    //private String mSource;
    private ArrayList<String> mImages;

    // default constructor for FireBase
    public Recipe() {}

    // constructor
    //Recipe(String name, Double rating, String source, Array[] images ){
        Recipe(String name, Double rating, ArrayList<String> images){
        this.mRecipeName = name;
        this.mRating = rating;
        //this.mSource = source;
        this.mImages = images;
    }

    // getters and setters
    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    public Double getRating() {
        return mRating;
    }

    public void setRating(Double rating) {
        mRating = rating;
    }

    /*public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }*/

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        mImages = images;
    }
}
