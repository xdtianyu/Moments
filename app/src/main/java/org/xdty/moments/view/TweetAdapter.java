package org.xdty.moments.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xdty.moments.R;
import org.xdty.moments.model.Comment;
import org.xdty.moments.model.Sender;
import org.xdty.moments.model.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ty on 15-10-5.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<Tweet> mTweets = new ArrayList<>();

    private Context mContext;

    public TweetAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.tweet_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sender sender = mTweets.get(position).getSender();
        if (sender != null) {
            holder.mSender.setText(sender.getNick());
            Picasso.with(mContext).load(sender.getAvatar())
                    .centerCrop()
                    .resize(160, 160) // TODO: hard coded
                    .into(holder.mAvatar);
        }

        holder.mContent.setText(mTweets.get(position).getContent());

        if (mTweets.get(position).getImages().size() > 0) {
            // show images
            holder.mImages.setAdapter(
                    new ImageAdapter(mContext, mTweets.get(position).getImages()));
            holder.mImages.setVisibility(View.VISIBLE);
        } else {
            holder.mImages.setAdapter(null);
            holder.mImages.setVisibility(View.GONE);
        }

        if (mTweets.get(position).getComments().size() > 0) {

            Comment comment = mTweets.get(position).getComments().get(0);

            Spanned content = Html.fromHtml("<font color=\"#669999\">" + comment.getSender().getNick()
                    + "</font>: " + comment.getContent());

            holder.mComment.setText(content);
            holder.mCommentLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mCommentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mTweets == null ? 0 : mTweets.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mSender;
        TextView mContent;
        TextView mComment;
        LinearLayout mCommentLayout;
        GridView mImages;

        public ViewHolder(View view) {
            super(view);

            mAvatar = (ImageView) view.findViewById(R.id.avatar);
            mSender = (TextView) view.findViewById(R.id.sender);
            mContent = (TextView) view.findViewById(R.id.content);
            mComment = (TextView) view.findViewById(R.id.comment);
            mCommentLayout = (LinearLayout) view.findViewById(R.id.comment_layout);
            mImages = (GridView) view.findViewById(R.id.images);
        }
    }
}
