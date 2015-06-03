package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.TimelineActivity;
import com.codepath.apps.twitterclient.apps.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.Constants;
import com.codepath.apps.twitterclient.helpers.EndlessScrollListener;
import com.codepath.apps.twitterclient.helpers.Utilities;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.net.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UserTimelineFragment extends TweetListFragment {

    private TwitterClient client;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment fragmentDemo = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the client
        client = TwitterApplication.getRestClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= super.onCreateView(inflater, container, savedInstanceState);
        // Setup refresh listener which triggers new data loading
        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeLine(Constants.DEFAULT_MAXID);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        // Attach the listener to the AdapterView onCreate for Infinity scrolling
        lvTweets.setOnScrollListener(new EndlessScrollListener(10) {
            @Override
            public void onLoadMore(long maxID, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                populateTimeLine(maxID);
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                // Resize ImageView
            }

        });

        populateTimeLine(Constants.DEFAULT_MAXID);
        return v;
    }

    //fill the twitter client with json values
    public void populateTimeLine(final long maxID) {
        String screenName = getArguments().getString("screen_name");
        if(!Utilities.isNetworkAvailable(getActivity().getApplicationContext()))
        {
            loadCache();
            swipeContainer.setRefreshing(false);
            Utilities.showAlertDialog(getActivity(), getActivity().getString(R.string.no_internet_error), false);
            return;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            client.getUserTimeline(screenName, maxID, new JsonHttpResponseHandler() {
                // Success

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (maxID <= 0) {
                        adTweets.clear();
                    }
                    alTweet = Tweet.fromJSONArray(response);
                    if (!alTweet.isEmpty()) {
                        adTweets.addAll(alTweet);
                    }
                    adTweets.notifyDataSetChanged();
                    Log.d("DEBUG SUCCESS", adTweets.toString());
                }

                // failure
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG home timeline", throwable.toString());
                }
                @Override
                public void onFinish() {
                    swipeContainer.setRefreshing(false);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    // loading Cached Tweets
    private void loadCache() {
        Log.v("TimelineActivity","inside log Cache");
        List<Tweet> cTweets = new Select().from(Tweet.class)
                .orderBy("createdDate DESC").limit(100).execute();
        Log.v("TimelineActivity","inside log Cache"+cTweets.toString());
        addAll(cTweets);
        //adTweets.addAll(alTweet);
    }
    // delete cached tweets and users
    private void deleteCache() {
        new Delete().from(Tweet.class).execute();
        new Delete().from(User.class).execute();
    }
}
