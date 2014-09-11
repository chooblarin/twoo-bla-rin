package com.chooblarin.twooblarin.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chooblarin.twooblarin.R;
import com.chooblarin.twooblarin.helper.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by chooblarin on 2014/09/11.
 */
public class TwitterOAuthActivity extends Activity {

    public static Intent generateIntent(Context context) {
        return new Intent(context, TwitterOAuthActivity.class);
    }

    private static final String CALLBACK = "twooblarin://callback";

    private Twitter mTwitter;
    private RequestToken mRequestToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_oauth);

        mTwitter = TwitterUtils.getTwitter(getApplicationContext());
        mTwitter.setOAuthAccessToken(null);

        findViewById(R.id.button_authorize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAuthorize();
            }
        });
    }

    private void startAuthorize() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    mRequestToken = mTwitter.getOAuthRequestToken(CALLBACK);
                    return mRequestToken.getAuthorizationURL();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String url) {
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            }
        };
        task.execute();
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intent == null
                || intent.getData() == null
                || !intent.getData().toString().startsWith(CALLBACK)) {
            return;
        }
        String verifier = intent.getData().getQueryParameter("oauth_verifier");
        new AsyncTask<String, Void, AccessToken>() {
            @Override
            protected AccessToken doInBackground(String... params) {
                try {
                    return mTwitter.getOAuthAccessToken(mRequestToken, params[0]);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(AccessToken accessToken) {
                Context context = getApplicationContext();
                if (accessToken != null) {
                    Toast.makeText(context, "認証成功だ", Toast.LENGTH_SHORT).show();
                    successOAuth(accessToken);
                } else {
                    Toast.makeText(context, "認証失敗だ", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(verifier);
    }

    private void successOAuth(AccessToken accessToken) {
        TwitterUtils.storeAccessToken(this, accessToken);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
