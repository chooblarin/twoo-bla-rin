package com.chooblarin.twooblarin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;
import com.twitter.sdk.android.core.models.Search;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                AppSession guestAppSession = result.data;

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
                twitterApiClient.getSearchService().tweets("#fabric",
                        null, null, null, null, 50, null, null, null, true, new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        // use result tweets
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        final TwitterApiException apiException = (TwitterApiException) exception;
                        final int errorCode = apiException.getErrorCode();
                        if (errorCode == TwitterApiConstants.Errors.APP_AUTH_ERROR_CODE
                                || errorCode == TwitterApiConstants.Errors.GUEST_AUTH_ERROR_CODE) {
                            // get new guestAppSession
                            // optionally retry
                        }
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }
}

