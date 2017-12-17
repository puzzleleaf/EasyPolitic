package leesd.crossithackathon.model;

/**
 * Created by cmtyx on 2017-12-16.
 */

public class DetailUserReview {
    public String userName;
    public String userReview;
    public float userStarRating;
    public String userReviewTime;
    public String key;
    public String userKey;

    public DetailUserReview(String userName, String userReview, float userStarRating, String userReviewTime, String key, String userKey) {
        this.userName = userName;
        this.userReview = userReview;
        this.userStarRating = userStarRating;
        this.userReviewTime = userReviewTime;
        this.key = key;
        this.userKey = userKey;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public DetailUserReview() {
    }

    public String getUserReviewTime() {
        return userReviewTime;
    }

    public void setUserReviewTime(String userReviewTime) {
        this.userReviewTime = userReviewTime;
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

    public float getUserStarRating() {
        return userStarRating;
    }

    public void setUserStarRating(float userStarRating) {
        this.userStarRating = userStarRating;
    }


}
