package com.vp.favorites.model;

import com.google.gson.annotations.SerializedName;

public class FetchResponse {

    @SerializedName("Title")
    private String title;
    @SerializedName("Poster")
    private String poster;

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }
}
