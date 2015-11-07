package org.xdty.moments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ty on 15-10-5.
 */
public class User extends Sender {
    @SerializedName("profile-image")
    @Expose
    private String profileImage;

    /**
     * @return The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * @param profileImage The profile-image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
