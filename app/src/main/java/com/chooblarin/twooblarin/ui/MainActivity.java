package com.chooblarin.twooblarin.ui;

import android.app.ListActivity;
import android.os.Bundle;

import com.chooblarin.twooblarin.helper.TwitterUtils;
import com.chooblarin.twooblarin.ui.fragment.TweetListFragment;


public class MainActivity extends ListActivity {

    private TweetListFragment tweetListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!TwitterUtils.hasStoredToken(getApplicationContext())) {
            startActivity(TwitterOAuthActivity.generateIntent(this));
            finish();

        } else {
            tweetListFragment = new TweetListFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, tweetListFragment).commit();
        }
    }
}

