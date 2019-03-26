package co.uk.missionlabs.db.Model;

import com.google.gson.annotations.SerializedName;

public class ListItem {
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    private String imdbID;
    @SerializedName("Poster")
    private String poster;

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getPoster() {
        return poster;
    }

    public void setItem(String title,String year, String imdbID,String poster){
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.poster = poster;
    }
}
