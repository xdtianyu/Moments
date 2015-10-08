package org.xdty.moments.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.xdty.moments.R;
import org.xdty.moments.model.Config;
import org.xdty.moments.model.Moment;
import org.xdty.moments.model.Tweet;
import org.xdty.moments.model.User;
import org.xdty.moments.view.TweetAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by ty on 15-10-5.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";

    @ViewById
    RecyclerView recyclerView;

    @ViewById
    TextView username;

    @ViewById
    ImageView avatar;

    @ViewById
    ImageView profileImage;

    TweetAdapter tweetAdapter;

    private User mUser = null;
    private List<Tweet> mTweets = null;

    @AfterViews
    public void afterViews() {
        getTweets();

        tweetAdapter = new TweetAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tweetAdapter);

    }

    @Background
    public void getTweets() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.PROVIDER_URI)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        Moment moment = restAdapter.create(Moment.class);

        try {
            mUser = moment.user("jsmith");
            mTweets = moment.tweet("jsmith");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mUser == null) {
            makeToast(getString(R.string.network_error));
            return;
        }

        if (mTweets == null) {
            makeToast(getString(R.string.network_error));
            return;
        }
        Log.d(TAG, "return");
        updateProfile();
        updateTweets();
    }

    @UiThread
    public void updateProfile() {
        username.setText(mUser.getNick());
        Picasso.with(this).load(mUser.getAvatar()).into(avatar);
        Picasso.with(this).load(mUser.getProfileImage())
                .centerCrop()
                .resize(profileImage.getMeasuredWidth(), profileImage.getMeasuredHeight())
                .into(profileImage);
    }

    @UiThread
    public void updateTweets() {
        List<Tweet> tweets = new ArrayList<>();
        for (Tweet tweet : mTweets) {
            // ignore the tweet which does not contain a content and images
            if (tweet.getContent() == null && tweet.getImages().size() == 0) {
                continue;
            }
            tweets.add(tweet);
        }
        tweetAdapter.swap(tweets);
    }

    @UiThread
    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
