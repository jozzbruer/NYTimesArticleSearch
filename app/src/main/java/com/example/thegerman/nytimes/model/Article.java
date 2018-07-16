package com.example.thegerman.nytimes.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable  {
    String web_url, headline,thumbnail,snippet;

    public String getWeb_url() {
        return web_url;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSnippet() {
        return snippet;
    }

    public Article(JSONObject jsonObject){
        try {
            this.web_url = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.snippet = jsonObject.getString("snippet");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0){
                JSONObject multimedia0bj = multimedia.getJSONObject(0);
                this.thumbnail = "https://www.nytimes.com/"+multimedia0bj.getString("url");
            }else {
                this.thumbnail ="";
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray jsonArray){
        ArrayList<Article> results = new ArrayList<>();
        for (int i = 0;i < jsonArray.length();i++){
            try {
                results.add(new Article(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
