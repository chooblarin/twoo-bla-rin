package com.chooblarin.twooblarin.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.chooblarin.twooblarin.R;
import com.chooblarin.twooblarin.adapter.TweetAdapter;
import com.chooblarin.twooblarin.event.BusHolder;
import com.chooblarin.twooblarin.event.TweetEvent;
import com.chooblarin.twooblarin.helper.TwitterUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by chooblarin on 2014/09/12.
 */
public class TweetListFragment extends Fragment {

    private Twitter twitter;

    private SwipeRefreshLayout refreshLayout;

    private ListView listView;

    private TweetAdapter tweetAdapter;

    private Button tweetButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusHolder.getInstance().register(this);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.tweet_swipe_refresh);
        listView = (ListView) rootView.findViewById(R.id.tweet_list_view);
        tweetButton = (Button) rootView.findViewById(R.id.tweet_button);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadTimeLine();
            }
        });

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TweetDialogFragment().show(getActivity().getFragmentManager(), "tweet_dialog");
            }
        });

        tweetAdapter = new TweetAdapter(getActivity(), new ArrayList<Status>());
        listView.setAdapter(tweetAdapter);
        twitter = TwitterUtils.getTwitter(getActivity().getApplicationContext());
        reloadTimeLine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusHolder.getInstance().unregister(this);
    }

    private void reloadTimeLine() {
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    return twitter.getHomeTimeline();

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                refreshLayout.setRefreshing(false);

                if (result != null) {
                    tweetAdapter.clear();
                    tweetAdapter.addAll(result);

                } else {
                    Context context = getActivity().getApplicationContext();
                    Toast.makeText(context, "タイムライン取得は失敗だ", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Subscribe
    public void subscribeTweetEvent(TweetEvent tweetEvent) {
        tweet(tweetEvent.getTweetText());
    }

    private void tweet(String tweetText) {
        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    twitter.updateStatus(params[0]);
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    Toast.makeText(getActivity(), "ツイートした", Toast.LENGTH_SHORT).show();
                    reloadTimeLine();

                } else {
                    Toast.makeText(getActivity(), "ツイート失敗", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(tweetText);
    }
}
