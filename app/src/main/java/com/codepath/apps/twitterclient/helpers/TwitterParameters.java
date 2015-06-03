package com.codepath.apps.twitterclient.helpers;

public class TwitterParameters {
    public int pageSize;
    public long sinceId;
    public long maxId;

    public TwitterParameters(){
        // default
        this.pageSize = 30;
        this.sinceId = 1;
        this.maxId = 0;
    }
}
