package com.chooblarin.twooblarin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chooblarin.twooblarin.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import twitter4j.Status;

/**
 * Created by chooblarin on 2014/09/10.
 */
public class TweetAdapter extends BindableAdapter<Status> {

    private Context context;

    private List<Status> statuses;

    private ImageLoader imageLoader;

    public TweetAdapter(Context context, List<Status> items) {
        super(context, items);
        this.context = context;
        statuses = items;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public Status getItem(int position) {
        return statuses.get(position);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup parent) {
        return inflater.inflate(R.layout.list_item_tweet, parent, false);
    }

    @Override
    public void bindView(Status item, View view) {
        // twitter icon
        ImageView icon = (ImageView) view.findViewById(R.id.twitter_icon);
        icon.setImageDrawable(context.getResources().getDrawable(R.drawable.non_image));

        imageLoader.displayImage(
                item.getUser().getProfileImageURL(), icon);

        ((TextView) view.findViewById(R.id.twitter_id)) // twitter id
                .setText("@" + item.getUser().getScreenName());
        ((TextView) view.findViewById(R.id.twitter_name)) // user name
                .setText(item.getUser().getName());
        ((TextView) view.findViewById(R.id.tweet)) // tweet
                .setText(item.getText());
    }
}
