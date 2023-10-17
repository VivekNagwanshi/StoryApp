package com.example.storyapp;

import android.content.Context;
import android.content.SharedPreferences;


public class StoryPreference {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private static final String PREF_NAME = "storyview_cache_pref";
    private static final int PREF_MODE_PRIVATE = 1;

    public StoryPreference(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();
    }
    public void clearStoryPreferences() {
        editor.clear();
        editor.apply();
    }

    public void setStoryVisited(String uri){
         editor.putBoolean(uri,true);
         editor.apply();
    }

    public boolean isStoryVisited(String uri){
        return preferences.getBoolean(uri,false);
    }


    /*public void saveStoryArraylist(ArrayList<StoryModel> storyList){
        Gson gson = new Gson();
        String myListJson = gson.toJson(storyList);
        editor.putString("myListKey", myListJson);
        editor.apply();
    }
    public ArrayList<StoryModel> getStoryArraylist(){
        String myListJson = preferences.getString("myListKey", null);
        Gson gson = new Gson();
        Type storyModelListType = new TypeToken<ArrayList<StoryModel>>() {}.getType();
        ArrayList<StoryModel> storyModelList = gson.fromJson(myListJson, storyModelListType);
        return storyModelList;
    }*/
}
