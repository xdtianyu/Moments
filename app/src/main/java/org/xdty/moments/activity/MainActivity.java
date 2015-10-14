package org.xdty.moments.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
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

    public final static String USER = "jsmith";

    @ViewById
    RecyclerView recyclerView;

    TweetAdapter tweetAdapter;
    boolean isLoading = false;
    private User mUser = null;
    private List<Tweet> mTweets = null;
    private TextView mUsername;
    private ImageView mAvatar;
    private ImageView mProfileImage;
    private int mTweetPage = 0;

    @AfterViews
    public void afterViews() {

        getTweets();

        RecyclerViewHeader header = RecyclerViewHeader.fromXml(this, R.layout.header);

        mUsername = (TextView) header.findViewById(R.id.username);
        mAvatar = (ImageView) header.findViewById(R.id.avatar);
        mProfileImage = (ImageView) header.findViewById(R.id.profile_image);

        tweetAdapter = new TweetAdapter(this);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tweetAdapter);
        header.attachTo(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastPosition == recyclerView.getAdapter().getItemCount() - 1) {
                    //Log.d(TAG, "" + lastPosition);

                    // load more tweets
                    loadMoreTweets();
                }
            }
        });
    }

    @Background
    public void getTweets() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.PROVIDER_URI)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        Moment moment = restAdapter.create(Moment.class);

        try {
            mUser = moment.user(USER);
            mTweets = moment.tweet(USER);
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

        updateProfile();
        updateTweets();
    }

    @UiThread
    public void updateProfile() {
        mUsername.setText(mUser.getNick());
        Picasso.with(this).load(mUser.getAvatar()).into(mAvatar);
        Picasso.with(this).load(mUser.getProfileImage())
                .centerCrop()
                .resize(mProfileImage.getMeasuredWidth(), mProfileImage.getMeasuredHeight())
                .into(mProfileImage);
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

        if (Config.SEPARATE_TWEET) {
            mTweets.clear();
            mTweets.addAll(tweets);
            // show first five tweets.
            loadMoreTweets();
        } else {
            // load all tweets into RecyclerViewer
            tweetAdapter.swap(tweets);
        }
    }

    @Background
    public void loadMoreTweets() {
        Log.e(TAG, "mTweetPage:" + mTweetPage);
        if (!isLoading) {

            isLoading = true;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<Tweet> tweets = new ArrayList<>();

            for (int i = 0; i < Config.TWEET_PER_PAGE; i++) {
                int position = mTweetPage * Config.TWEET_PER_PAGE + i;
                if (position < mTweets.size()) {
                    tweets.add(mTweets.get(position));
                } else {
                    makeToast(getString(R.string.no_more_tweets));
                    break;
                }
            }

            mTweetPage++;

            appendTweets(tweets);

            isLoading = false;
        }
    }

    @UiThread
    public void appendTweets(List<Tweet> tweets) {
        tweetAdapter.append(tweets);
    }

    @UiThread
    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
