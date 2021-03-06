package org.xdty.moments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ty on 15-10-5.
 */
public class Tweet {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("images")
    @Expose
    private List<Image> images = new ArrayList<Image>();
    @SerializedName("sender")
    @Expose
    private Sender sender;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = new ArrayList<Comment>();
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("unknown error")
    @Expose
    private String unknownError;

    /**
     * @return The id
     */
    public int getID() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setContent(int id) {
        this.id = id;
    }

    /**
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return The images
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * @param images The images
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     * @return The sender
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * @param sender The sender
     */
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    /**
     * @return The comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * @param comments The comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * @return The error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error The error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return The unknownError
     */
    public String getUnknownError() {
        return unknownError;
    }

    /**
     * @param unknownError The unknown error
     */
    public void setUnknownError(String unknownError) {
        this.unknownError = unknownError;
    }
}
