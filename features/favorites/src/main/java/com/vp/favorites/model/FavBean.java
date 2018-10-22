package com.vp.favorites.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavBean implements Serializable{
    @SerializedName("ImdbID")
    private String imdbID;
    @SerializedName("Poster")
    private String poster;

    public FavBean(String imdbID, String poster) {
        this.imdbID = imdbID;
        this.poster = poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
