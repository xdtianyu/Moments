package org.xdty.moments.model;

import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by ty on 15-10-5.
 */
public interface Moment {

    @GET("/user/{username}")
    User user(
            @Path("username") String username
    );

    @GET("/user/{username}/tweets")
    List<Tweet> tweet(
            @Path("username") String username
    );

    @POST("/comment/post")
    @FormUrlEncoded
    Message comment(
            @Field("username") String username,
            @Field("content")String content,
            @Field("tweet_id") int tweetId
    );
}
