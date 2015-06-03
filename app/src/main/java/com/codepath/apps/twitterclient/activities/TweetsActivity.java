package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.codepath.apps.twitterclient.R;

public class TweetsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_profile_tweets);
        setTitle(R.string.tweetsLabel);
        Long userId = getIntent().getLongExtra("userId", 0);
        String query = getIntent().getStringExtra("query");
        if (query != null) {
            setTitle(query);
        }
      /*  final TweetsTimelineFragment fragmentTweets = TweetsTimelineFragment.newInstance(TimelineType.USER, userId, query);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, fragmentTweets);
        ft.commit();*/
    }
}
