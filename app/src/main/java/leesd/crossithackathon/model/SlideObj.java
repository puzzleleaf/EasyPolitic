package leesd.crossithackathon.model;

/**
 * Created by cmtyx on 2017-12-02.
 */

public class SlideObj {
    private String name;
    private String img;

    public SlideObj(String name, String img) {
        this.name = name;
        this.img = img;
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
