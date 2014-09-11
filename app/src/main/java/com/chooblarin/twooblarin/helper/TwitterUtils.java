package com.chooblarin.twooblarin.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.chooblarin.twooblarin.R;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by chooblarin on 2014/09/11.
 */
public class TwitterUtils {

    private static final String PREFS_NAME = "preference_twitter";
    private static final String KEY_TOKEN = "twitter_access_token";
    private static final String KEY_SECRET = "twitter_access_secret";

    public static Twitter getTwitter(Context context) {
        String consumerKey = context.getString(R.string.consumer_key);
        String consumerSecret = context.getString(R.string.consumer_secret);

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        if (hasStoredToken(context)) {
            twitter.setOAuthAccessToken(loadAccessToken(context));
        }

        return twitter;
    }

    public static boolean hasStoredToken(Context context) {
        return loadAccessToken(context) != null;
    }

    private static AccessToken loadAccessToken(Context context) {
        SharedPreferences prefs
                = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, null);
        String secret = prefs.getString(KEY_SECRET, null);
        Log.d("mimic", "loadAccessToken token -> " + token + " secret -> " + secret);

        if (token != null && secret != null) {
            return new AccessToken(token, secret);
        } else {
            return null;
        }
    }

    public static void storeAccessToken(Context context, AccessToken accessToken) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_TOKEN, accessToken.getToken());
        editor.putString(KEY_SECRET, accessToken.getTokenSecret());
        editor.apply();
    }
}
