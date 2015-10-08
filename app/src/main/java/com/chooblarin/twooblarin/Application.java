package com.chooblarin.twooblarin;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.consumer_key),
                getString(R.string.consumer_secret));
        Fabric.with(this, new Twitter(authConfig));
    }
}
