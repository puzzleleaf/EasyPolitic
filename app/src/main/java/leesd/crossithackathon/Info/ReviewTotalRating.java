package leesd.crossithackathon.Info;

/**
 * Created by cmtyx on 2017-12-17.
 */

public class ReviewTotalRating {
    public float rating;
    public int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ReviewTotalRating() {

    }

    public ReviewTotalRating(float rating, int total) {
        this.rating = rating;
        this.total = total;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
