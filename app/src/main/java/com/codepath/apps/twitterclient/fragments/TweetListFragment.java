package com.codepath.apps.twitterclient.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.DetailTweetActivity;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.activities.TimelineActivity;
import com.codepath.apps.twitterclient.adapters.HomeTweetsAdapter;
import com.codepath.apps.twitterclient.helpers.Constants;
import com.codepath.apps.twitterclient.helpers.Utilities;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TweetListFragment extends Fragment {
    public ArrayList<Tweet> alTweet;




    public HomeTweetsAdapter adTweets, adHomeTweets;
    public ListView lvTweets;
    public SwipeRefreshLayout swipeContainer;
    private TimelineActivity timelineActivity;
    private HomeTweetsAdapter tweetsAdapter;
    public ProgressWheel progressBar;
    View v;
    public TextView tvTest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alTweet = new ArrayList<>();
        //construct the adapter from the data source
        adTweets= new HomeTweetsAdapter(getActivity(), alTweet, new HomeTweetsAdapter.HomeTweetsAdapterListener() {
            @Override
            public void profileClicked(User user) {
                showProfileActivity(user);

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets); // find the list view
        // Inflate the footer
        progressBar = (ProgressWheel) v.findViewById(R.id.pbLoading);
        lvTweets.setAdapter(adTweets);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets); // find the list view
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tweet tweet = alTweet.get(position);
                showDetailActivity(tweet);
            }
        });
    }
    public void addAll(List<Tweet> tweets){
        adTweets.addAll(tweets);
    }

    public ArrayList<Tweet> getTweets() {
        return alTweet;
    }

    private void showDetailActivity(Tweet tweet) {
        Intent intent = new Intent(getActivity(), DetailTweetActivity.class);
        intent.putExtra("selectedTweet", tweet);
        startActivity(intent);
    }
    public HomeTweetsAdapter getTweetsAdapter() {
        return adHomeTweets;
    }

    public void setTweetsAdapter(HomeTweetsAdapter adTweets) {
        this.adHomeTweets = adTweets;
    }

    private void showProfileActivity(User user) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    protected List<Tweet> getListDataFromDB(){
        return null;
    }
    protected void getDataFromDB(){
        List<Tweet> newTweets = getListDataFromDB();
        adTweets.addAll(newTweets);

        swipeContainer.setRefreshing(false);
    }


}
