package leesd.crossithackathon.model;

/**
 * Created by cmtyx on 2017-12-02.
 */

public class SlideObj {
    private String name;
    private String img;
    private float rating;
    private int total;

    public SlideObj(String name, String img, float rating, int total) {
        this.name = name;
        this.img = img;
        this.rating = rating;
        this.total = total;
    }

    public float getRating() {

        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }
}
