package org.xdty.moments.activity;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rey.material.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.xdty.moments.R;
import org.xdty.moments.model.Comment;
import org.xdty.moments.model.Config;
import org.xdty.moments.model.Message;
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
public class MainActivity extends AppCompatActivity implements TweetAdapter.OnReplyClickListener {

    public final static String TAG = "MainActivity";

    public final static String USER = "tianyu";

    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;

    @ViewById
    RecyclerView recyclerView;

    @ViewById
    LinearLayout inputLayout;

    @ViewById
    EditText input;

    @ViewById
    Button inputSubmit;

    TweetAdapter tweetAdapter;
    boolean isLoading = false;
    private User mUser = null;
    private List<Tweet> mTweets = null;
    private int mTweetPage = 0;

    private int mSelectedTweetId = -1;

    @AfterViews
    public void afterViews() {

        getTweets();

        tweetAdapter = new TweetAdapter(this);

        tweetAdapter.setOnReplyClickListener(this);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tweetAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // can scroll up and disable refresh
                if (recyclerView.canScrollVertically(-1)) {
                    swipeRefreshLayout.setEnabled(false);
                } else {
                    swipeRefreshLayout.setEnabled(true);
                }

                // can not scroll down and load more tweets
                if (!recyclerView.canScrollVertically(1)) {
                    loadMoreTweets();
                }
            }
        });

        // pulling down the view to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mTweetPage = 0;
                getTweets();
            }
        });

        inputSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputLayout.getVisibility() == View.VISIBLE) {
                    String content = input.getText().toString();

                    if (!content.isEmpty()) {
                        for (Tweet tweet : mTweets) {
                            if (tweet.getID() == mSelectedTweetId) {
                                Comment comment = new Comment();
                                comment.setSender(tweet.getSender());
                                comment.setContent(content);
                                tweet.getComments().add(0, comment);
                                tweetAdapter.notifyDataSetChanged();

                                postComment(comment);
                                break;
                            }
                        }
                    } else {
                        makeToast(getString(R.string.empty_comment));
                        return;
                    }

                    hideCommentInput();
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

    @Background
    public void postComment(Comment comment) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.PROVIDER_URI)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        Moment moment = restAdapter.create(Moment.class);

        try {
            Message message =
                    moment.comment(comment.getSender().getUsername(), comment.getContent(),
                            mSelectedTweetId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void updateProfile() {
        tweetAdapter.updateProfile(mUser);
    }

    @UiThread
    public void updateTweets() {

        tweetAdapter.setHasMoreTweets(true);

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

            // clear recycler view
            tweetAdapter.swap(new ArrayList<Tweet>());

            // show first five tweets.
            loadMoreTweets();
        } else {
            // load all tweets into RecyclerViewer
            tweetAdapter.swap(tweets);

            tweetAdapter.setHasMoreTweets(false);
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Background
    public void loadMoreTweets() {

        if (!isLoading) {

            isLoading = true;

            // simulate network delay
            if (Config.SIMULATE_NETWORK_DELAY) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            List<Tweet> tweets = new ArrayList<>();

            for (int i = 0; i < Config.TWEET_PER_PAGE; i++) {
                int position = mTweetPage * Config.TWEET_PER_PAGE + i;
                if (mTweets != null && position < mTweets.size()) {
                    tweets.add(mTweets.get(position));
                } else {
//                    makeToast(getString(R.string.no_more_tweets));
                    tweetAdapter.setHasMoreTweets(false);
                    break;
                }
            }

            if (tweets.size() > 0) {
                mTweetPage++;
                appendTweets(tweets);
            }

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

    @Override
    public void onReplyClick(int tweetId) {
        if (inputLayout.getVisibility() != View.VISIBLE) {
            mSelectedTweetId = tweetId;
            showCommentInput();
        }
    }

    private void showCommentInput() {
        inputLayout.setVisibility(View.VISIBLE);
        recyclerView.smoothScrollBy(0,
                (int) getResources().getDimension(R.dimen.reply_bar_height));


        input.requestFocus();
        input.dispatchTouchEvent(
                MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN, 0, 0, 0));
        input.dispatchTouchEvent(
                MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP, 0, 0, 0));
    }

    private void hideCommentInput() {
        inputLayout.setVisibility(View.GONE);
        recyclerView.smoothScrollBy(0,
                (int) -getResources().getDimension(R.dimen.reply_bar_height));
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        input.setText("");
    }

    @Override
    public void onBackPressed() {

        if (inputLayout.getVisibility() == View.VISIBLE) {
            hideCommentInput();
        } else {
            super.onBackPressed();
        }
    }
}
