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

    private List<Status> mStatusList;

    private ImageLoader mLoader;

    public TweetAdapter(Context context, List<Status> items) {
        super(context, items);
        mStatusList = items;
        mLoader = ImageLoader.getInstance();
    }

    @Override
    public Status getItem(int position) {
        return mStatusList.get(position);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup parent) {
        return inflater.inflate(R.layout.list_item_tweet, parent, false);
    }

    @Override
    public void bindView(Status item, View view) {
        // twitter icon
        mLoader.displayImage(
                item.getUser().getProfileImageURL(),
                (ImageView) view.findViewById(R.id.twitter_icon));

        ((TextView) view.findViewById(R.id.twitter_id)) // twitter id
                .setText("@" + item.getUser().getScreenName());
        ((TextView) view.findViewById(R.id.twitter_name)) // user name
                .setText(item.getUser().getName());
        ((TextView) view.findViewById(R.id.tweet)) // tweet
                .setText(item.getText());
    }
}
