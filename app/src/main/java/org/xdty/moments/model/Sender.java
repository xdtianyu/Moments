package org.xdty.moments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ty on 15-10-5.
 */
public class Sender {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("nick")
    @Expose
    private String nick;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * @param nick The nick
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * @return The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
