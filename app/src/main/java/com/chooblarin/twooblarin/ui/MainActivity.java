package com.chooblarin.twooblarin.ui;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.chooblarin.twooblarin.adapter.TweetAdapter;
import com.chooblarin.twooblarin.helper.TwitterUtils;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class MainActivity extends ListActivity {

    private Twitter mTwitter;

    private TweetAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!TwitterUtils.hasStoredToken(getApplicationContext())) {
            startActivity(TwitterOAuthActivity.generateIntent(this));
            finish();

        } else {
            mAdapter = new TweetAdapter(this, new ArrayList<Status>());
            setListAdapter(mAdapter);

            mTwitter = TwitterUtils.getTwitter(getApplicationContext());
            reloadTimeLine();
        }
    }

    private void reloadTimeLine() {
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    return mTwitter.getHomeTimeline();

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    mAdapter.clear();
                    mAdapter.addAll(result);

                } else {
                    Context context = getApplicationContext();
                    Toast.makeText(context, "タイムライン取得は失敗だ", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}

