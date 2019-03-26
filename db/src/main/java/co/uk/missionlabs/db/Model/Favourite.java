package co.uk.missionlabs.db.Model;

public class Favourite {

    public Favourite(String imdbID,MovieDetail movieDetail){
        this.imdbID = imdbID;
        this.movieDetail = movieDetail;
    }


    private MovieDetail movieDetail;
    private String imdbID;

    public MovieDetail getMovieDetail() {
        return movieDetail;
    }

    public String getImdbID() {
        return imdbID;
    }


}

