package com.bookmark.Abstract;

public class Comment {
    public Comment() {
    }
    public Comment(String body, int rating) {
        this.body = body;
        this.rating = rating;
    }
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int rating;
    public String body;

}
