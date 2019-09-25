package com.vp.list.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListItem  implements Serializable {
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    @SerializedName("imdbID")
    private String imdbID;
    @SerializedName("Poster")
    private String poster;

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getID() {
        return imdbID;
    }

    public String getPoster() {
        return poster;
    }
}
