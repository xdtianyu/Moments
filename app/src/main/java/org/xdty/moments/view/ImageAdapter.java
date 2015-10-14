package org.xdty.moments.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import org.xdty.moments.R;
import org.xdty.moments.model.Image;

import java.util.List;

/**
 * Created by ty on 15-10-8.
 */
public class ImageAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;

    private Context mContext;

    private List<Image> mImages;

    public ImageAdapter(Context context, List<Image> images) {
        mContext = context;
        mImages = images;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: fix inflater and image size.
        View view = mLayoutInflater.inflate(R.layout.grid_item, parent, false);
        SquareImageView imageView = (SquareImageView) view.findViewById(R.id.image);
        Picasso.with(mContext).load(mImages.get(position).getUrl())
                .centerCrop()
                .resize(160, 160)
                .into(imageView);
        return imageView;
    }
}
