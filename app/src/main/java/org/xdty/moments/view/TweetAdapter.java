package org.xdty.moments.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;
import org.xdty.moments.R;
import org.xdty.moments.model.Comment;
import org.xdty.moments.model.Image;
import org.xdty.moments.model.Sender;
import org.xdty.moments.model.Tweet;
import org.xdty.moments.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ty on 15-10-5.
 */
public class TweetAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TWEET = 1;
    private static final int TYPE_FOOTER = 2;

    private final LayoutInflater mLayoutInflater;
    private List<Tweet> mTweets = new ArrayList<>();

    private Context mContext;

    private User mUser = null;

    public TweetAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_TWEET) {
            return new TweetViewHolder(
                    mLayoutInflater.inflate(R.layout.tweet_layout, parent, false));
        } else if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(
                    mLayoutInflater.inflate(R.layout.tweet_header, parent, false));
        } else {
            return new FooterViewHolder(
                    mLayoutInflater.inflate(R.layout.tweet_footer, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof TweetViewHolder) {

            position = position - 1;

            final TweetViewHolder tweetViewHolder = (TweetViewHolder) holder;
            Sender sender = mTweets.get(position).getSender();
            if (sender != null) {
                tweetViewHolder.sender.setText(sender.getNick());
                Picasso.with(mContext).load(sender.getAvatar())
                        .centerCrop()
                        .resize(160, 160) // TODO: hard coded
                        .into(tweetViewHolder.avatar);
            }

            tweetViewHolder.content.setText(mTweets.get(position).getContent());

            if (mTweets.get(position).getImages().size() > 0) {

                tweetViewHolder.images.removeAllViewsInLayout();

                // show images
                for (Image image : mTweets.get(position).getImages()) {
                    SquareImageView imageView =
                            (SquareImageView) mLayoutInflater.inflate(R.layout.tweet_image,
                                    tweetViewHolder.images, false);
                    Picasso.with(mContext).load(image.getUrl())
                            .fit()
                            .centerCrop()
                            .into(imageView);
                    tweetViewHolder.images.addView(imageView);
                    tweetViewHolder.images.invalidate();
                }
                tweetViewHolder.images.setVisibility(View.VISIBLE);
            } else {
                tweetViewHolder.images.setVisibility(View.GONE);
            }

            if (mTweets.get(position).getComments().size() > 0) {

                Comment comment = mTweets.get(position).getComments().get(0);

                Spanned content =
                        Html.fromHtml("<font color=\"#669999\">" + comment.getSender().getNick()
                                + "</font>: " + comment.getContent());

                tweetViewHolder.comment.setText(content);
                tweetViewHolder.commentLayout.setVisibility(View.VISIBLE);
            } else {
                tweetViewHolder.commentLayout.setVisibility(View.GONE);
            }
        } else if (holder instanceof HeaderViewHolder) {
            if (mUser != null) {
                final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

                if (mTweets.size() > 1) {
                    headerViewHolder.headerLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.tweet_background));
                } else {
                    headerViewHolder.headerLayout.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.transparent));
                }

                headerViewHolder.username.setText(mUser.getNick());
                Picasso.with(mContext).load(mUser.getAvatar()).into(headerViewHolder.avatar);
                Picasso.with(mContext).load(mUser.getProfileImage())
                        .fit()
                        .centerCrop()
                        .into(headerViewHolder.profileImage);
            }
        } else if (holder instanceof FooterViewHolder) {
            final FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.footerText.setText(R.string.no_more_tweets);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == mTweets.size() + 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_TWEET;
        }
    }

    @Override
    public int getItemCount() {
        return mTweets == null ? 2 : mTweets.size() + 2;
    }

    public void swap(List<Tweet> tweets) {
        mTweets.clear();
        mTweets.addAll(tweets);
        notifyDataSetChanged();
    }

    public void append(List<Tweet> tweets) {
        mTweets.addAll(tweets);
        notifyDataSetChanged();
    }

    public void updateProfile(User user) {
        mUser = user;
        notifyDataSetChanged();
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView sender;
        TextView content;
        TextView comment;
        LinearLayout commentLayout;
        FlowLayout images;

        public TweetViewHolder(View view) {
            super(view);

            avatar = (ImageView) view.findViewById(R.id.avatar);
            sender = (TextView) view.findViewById(R.id.sender);
            content = (TextView) view.findViewById(R.id.content);
            comment = (TextView) view.findViewById(R.id.comment);
            commentLayout = (LinearLayout) view.findViewById(R.id.comment_layout);
            images = (FlowLayout) view.findViewById(R.id.images);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        FrameLayout headerLayout;
        ImageView profileImage;
        TextView username;
        ImageView avatar;

        public HeaderViewHolder(View view) {
            super(view);

            headerLayout = (FrameLayout) view.findViewById(R.id.tweet_header_layout);
            profileImage = (ImageView) view.findViewById(R.id.profile_image);
            username = (TextView) view.findViewById(R.id.username);
            avatar = (ImageView) view.findViewById(R.id.avatar);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView footerText;

        public FooterViewHolder(View view) {
            super(view);
            footerText = (TextView) view.findViewById(R.id.footer);
        }
    }
}
