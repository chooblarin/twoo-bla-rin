package com.chooblarin.twooblarin.event;

/**
 * Created by chooblarin on 2014/09/13.
 */
public class TweetEvent {
    private String tweetText;

    public TweetEvent(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getTweetText() {
        return tweetText;
    }
}
