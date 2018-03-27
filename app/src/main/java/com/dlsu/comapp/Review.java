package com.dlsu.comapp;

/**
 * Created by Enrico Zabayle on 14/03/2018.
 */

public class Review {
    private String reviewID, reviewProfID, reviewDate, reviewCourseID, reviewContent,userID;
    private float reviewRating;

    public Review(String reviewID, String userID, String reviewProfID, String reviewDate, String reviewCourseID, String reviewContent, float reviewRating) {
        this.reviewID = reviewID;
        this.reviewProfID = reviewProfID;
        this.reviewDate = reviewDate;
        this.reviewCourseID = reviewCourseID;
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.userID = userID;
    }
    public Review(){}

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getReviewProfID() {
        return reviewProfID;
    }

    public void setReviewProfID(String reviewProfID) {
        this.reviewProfID = reviewProfID;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewCourseID() {
        return reviewCourseID;
    }

    public void setReviewCourseID(String reviewCourseID) {
        this.reviewCourseID = reviewCourseID;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public float getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(float reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
