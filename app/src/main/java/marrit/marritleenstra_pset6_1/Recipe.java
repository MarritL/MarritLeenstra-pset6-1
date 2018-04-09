package marrit.marritleenstra_pset6_1;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Recipe implements Serializable {

    private String mId;
    private String mRecipeName;
    private Double mRating;
    private String mSourceName;
    private String mSourceUrl;
    private int mServings;
    private int mTime;
    private ArrayList<String> mImages;
    private String mLargeImageUrl;
    private ArrayList<String> mIngredients;




    // default constructor for FireBase
    public Recipe() {}

    // constructor
    Recipe(String id, String name, String sourceName, Double rating, ArrayList<String> images){
        this.mId = id;
        this.mRecipeName = name;
        this.mSourceName = sourceName;
        this.mRating = rating;
        this.mImages = images;
        this.mSourceUrl = "";
        this.mServings = 0;
        this.mTime = 0;
        this.mLargeImageUrl = "";
        this.mIngredients = new ArrayList<>();
    }

    // getters and setters
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

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

    public String getSourceName() {
        return mSourceName;
    }

    public void setSourceName(String sourceName) {
        mSourceName = sourceName;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        mImages = images;
    }

    public String getSourceUrl() {
        return mSourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        mSourceUrl = sourceUrl;
    }

    public int getServings() {
        return mServings;
    }

    public void setServings(int servings) {
        mServings = servings;
    }

    public int getTime() {
        return mTime;
    }

    public void setTime(int time) {
        mTime = time;
    }

    public String getLargeImageUrl() {
        return mLargeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        mLargeImageUrl = largeImageUrl;
    }

    public ArrayList<String> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        mIngredients = ingredients;
    }

    // find the recipe with the specified id in the specified arrayList
    public static Recipe getRecipe(String id, ArrayList<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }
}
