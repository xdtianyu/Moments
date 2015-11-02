package org.xdty.moments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ty on 15-10-8.
 */
public class Comment {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("sender")
    @Expose
    private Sender sender;

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
     *
     * @return
     * The content
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     * The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return
     * The sender
     */
    public Sender getSender() {
        return sender;
    }

    /**
     *
     * @param sender
     * The sender
     */
    public void setSender(Sender sender) {
        this.sender = sender;
    }

}
