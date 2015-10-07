package org.xdty.moments.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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
        updateTweets();
    }

    @UiThread
    public void updateTweets() {
        tweetAdapter.swap(mTweets);
    }

    @UiThread
    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
