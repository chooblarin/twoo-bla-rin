package com.chooblarin.twooblarin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by chooblarin on 2014/09/04.
 */
public class OAuthTestActivity extends Activity {

    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";
    private static final String CALLBACK = "twooblarin://test";

    private OAuthAuthorization mOauth;

    public static Intent generateIntent(Context context) {
        return new Intent(context, OAuthTestActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConfigurationBuilder confBuilder = new ConfigurationBuilder()
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET);

        mOauth = new OAuthAuthorization(confBuilder.build());
        mOauth.setOAuthAccessToken(null);

        new TwitterAsyncTask<Object, Void, String>(
                new TwitterAsyncTask.TwitterPreExecute() {
                    @Override
                    public void run() {
                    }
                },
                new TwitterAsyncTask.TwitterAction<TwitterResult<String>, Object>() {
                    @Override
                    public TwitterResult<String> run(Object param) {
                        TwitterResult<String> r = new TwitterResult<String>();

                        try {
                            r.setResult(mOauth.getOAuthRequestToken(CALLBACK).getAuthorizationURL());
                        } catch (TwitterException e) {
                            r.setError(e);
                        }

                        return r;
                    }
                },
                new TwitterAsyncTask.TwitterCallback<String>() {
                    @Override
                    public void run(String result) {
                        //Oauth認証開始
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                        startActivity(intent);
                    }
                },
                new TwitterAsyncTask.TwitterOnError() {
                    @Override
                    public void run(TwitterException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Twitterとの接続に失敗しました", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
        ).execute(new Object());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith(CALLBACK)) {

            new TwitterAsyncTask<Uri, Void, AccessToken>(
                    new TwitterAsyncTask.TwitterPreExecute() {
                        @Override
                        public void run() {
                        }
                    },
                    new TwitterAsyncTask.TwitterAction<TwitterResult<AccessToken>, Uri>() {
                        @Override
                        public TwitterResult<AccessToken> run(Uri param) {
                            TwitterResult<AccessToken> r = new TwitterResult<AccessToken>();
                            //AccessTokenの取得
                            String verifier = param.getQueryParameter("oauth_verifier");
                            try {
                                r.setResult(mOauth.getOAuthAccessToken(verifier));
                            } catch (TwitterException e) {
                                r.setError(e);
                            }

                            return r;
                        }
                    },
                    new TwitterAsyncTask.TwitterCallback<AccessToken>() {
                        @Override
                        public void run(AccessToken result) {
                            // TODO Oauth認証に成功した後の処理
                            String token = result.getToken();
                            String secret = result.getTokenSecret();
                            Log.d("mimic", "成功!!! token -> " + token + ", secret -> " + secret);
                        }
                    },
                    new TwitterAsyncTask.TwitterOnError() {
                        @Override
                        public void run(TwitterException e) {
                            Log.d("mimic", "失敗...");
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Oauth認証に失敗しました", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
            ).execute(uri);
        }
    }
}
