package marrit.marritleenstra_pset6_1;

import java.lang.reflect.Array;

public class Recipe {

    private String mRecipeName;
    private Double mRating;
    private String mSource;
    private Array[] mImages;

    // constructor
    Recipe(String name, Double rating, String source, Array[] images ){
        this.mRecipeName = name;
        this.mRating = rating;
        this.mSource = source;
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

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public Array[] getImages() {
        return mImages;
    }

    public void setImages(Array[] images) {
        mImages = images;
    }
}
