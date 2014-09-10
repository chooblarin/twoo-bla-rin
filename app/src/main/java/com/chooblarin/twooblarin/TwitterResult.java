package com.chooblarin.twooblarin;

import twitter4j.TwitterException;

/**
 * Created by chooblarin on 2014/09/04.
 */
public class TwitterResult<Result> {
    private Result result;
    private TwitterException error;
    public Result getResult() {
        return result;
    }
    public void setResult(Result result) {
        this.result = result;
    }
    public boolean hasError() {
        return error != null;
    }
    public TwitterException getError() {
        return error;
    }
    public void setError(TwitterException error) {
        this.error = error;
    }
}
