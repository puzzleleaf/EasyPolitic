package leesd.crossithackathon.model;

/**
 * Created by cmtyx on 2017-12-16.
 */

public class PbInstitutionReview {
    public String userName;
    public String userReview;
    public float userStarRatiog;

    public PbInstitutionReview() {
    }

    public PbInstitutionReview(String userName, String userReview, float userStarRatiog) {
        this.userName = userName;
        this.userReview = userReview;
        this.userStarRatiog = userStarRatiog;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public float getUserStarRatiog() {
        return userStarRatiog;
    }

    public void setUserStarRatiog(float userStarRatiog) {
        this.userStarRatiog = userStarRatiog;
    }
}
