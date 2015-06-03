package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;

import com.codepath.apps.twitterclient.apps.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.Constants;

import com.codepath.apps.twitterclient.helpers.Utilities;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.net.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailTweetActivity extends ActionBarActivity {
    Tweet tweet = null;
    Tweet tweetToDisplay = null;
    TwitterClient client = TwitterApplication.getClient();
    ImageView ivProfile, ivDetailPostImage;
    TextView tvName, tvScreenName,tvBody, tvDate, tvRetweetsCount, tvFavouritesCount,tvRetweets,tvFavourites;
    TextView tvDetailFavoriteCount,tvDetailRetweetCount, tvDetailRetweetedUser;
    View vTopSeparator, vBottomSeparator;
    WebView wvMedia;
    ImageView ivDetailReply, ivDetailShare,ivDetailFavouriteCount, ivDetailRetweetCount ;
    int favouriteCount=0;
    public User user = null;
    RelativeLayout rlDetailRetweetInfoRow, rlDetailTweetHolder, rlDetailFavouriteHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        tweet = getIntent().getParcelableExtra("selectedTweet");
        user = tweet.getUser();
        setupViews();
        fillViews();
    }

    private void setupViews() {

        // Add twitter bird
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ivProfile = (ImageView) findViewById(R.id.ivDetailProfileImage);
      //  btTweetStatus = (Button) findViewById(R.id.bTweetStatusDetail);
        tvName = (TextView) findViewById(R.id.tvDetailname);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvRetweetsCount = (TextView) findViewById(R.id.tvRetweetsCount);
        tvFavouritesCount = (TextView) findViewById(R.id.tvFavouritesCount);
        tvRetweets = (TextView) findViewById(R.id.tvRetweets);
        tvFavourites = (TextView) findViewById(R.id.tvFavourites);
        tvDetailFavoriteCount = (TextView) findViewById(R.id.tvDetailFavoriteCount);
        tvDetailRetweetCount = (TextView) findViewById(R.id.tvDetailRetweetCount);
        vTopSeparator = (View) findViewById(R.id.vTopSeparator);
        vBottomSeparator = (View) findViewById(R.id.vBottomSeparator);
        wvMedia = (WebView) findViewById(R.id.wvMedia);
        ivDetailPostImage = (ImageView) findViewById(R.id.ivDetailPostImage);
        tvFavourites = (TextView) findViewById(R.id.tvFavourites);
        rlDetailRetweetInfoRow = (RelativeLayout) findViewById(R.id.rlDetailRetweetInfoRow);
        tvDetailRetweetedUser = (TextView) findViewById(R.id.tvDetailRetweetedUser);


        rlDetailTweetHolder = (RelativeLayout) findViewById(R.id.rlDetailReTweetHolder);
        rlDetailFavouriteHolder = (RelativeLayout) findViewById(R.id.rlDetailFavouriteHolder);

        ivDetailReply = (ImageView) findViewById(R.id.ivDetailReply);
        ivDetailShare = (ImageView) findViewById(R.id.ivDetailShare);
        ivDetailFavouriteCount = (ImageView) findViewById(R.id.ivDetailFavouriteCount);
        ivDetailRetweetCount = (ImageView) findViewById(R.id.ivDetailRetweetCount);
    }

    private void fillViews() {
        user = tweet.getUser();
        tvName.setText(user.getUserName());
        tvScreenName.setText("@" + user.getScreenName());
        if (tweet.getMedia_url() != null) {
            Picasso.with(this).load(tweet.getMedia_url())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .resize(tweet.getMediaWidth(), tweet.getMediaHeight())
                    .into(ivDetailPostImage);
        }

        if (tweet.hasOriginalTweet()) {
            rlDetailRetweetInfoRow.setVisibility(View.VISIBLE);
            tvDetailRetweetedUser.setText(tweet.getUser().getUserName() + " retweeted");
            tweetToDisplay = tweet.getRetweetedTweet();
            tweet = tweetToDisplay;

        } else {
            rlDetailRetweetInfoRow.setVisibility(View.GONE);
        }

        if (tweet.getFavorited()) {
            ivDetailFavouriteCount.setImageResource(R.drawable.favorite_on);
            tvDetailFavoriteCount.setTextColor(Color.parseColor("#FFAC33"));
        } else {
            ivDetailFavouriteCount.setImageResource(R.drawable.favorite);
            tvDetailFavoriteCount.setTextColor(Color.parseColor("#808080"));
        }

        if(tweet.getRetweeted())
        {
            ivDetailRetweetCount.setImageResource(R.drawable.retweet_on);
            tvDetailRetweetCount.setTextColor(Color.parseColor("#5C913B"));
        } else {
            ivDetailRetweetCount.setImageResource(R.drawable.retweet);
            tvDetailRetweetCount.setTextColor(Color.parseColor("#808080"));
        }

         // TO DO
        ivDetailReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Utilities.isNetworkAvailable(getApplicationContext()))
                {
                    Utilities.showAlertDialog(DetailTweetActivity.this, getBaseContext().getString(R.string.no_internet_error), false);
                } else {
                    Utilities.composeTweet(DetailTweetActivity.this, tweet, Constants.ACTION_REPLY, 0);
                }
            }
        });
        ivDetailRetweetCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utilities.isNetworkAvailable(getApplicationContext()))
                {
                    Utilities.showAlertDialog(DetailTweetActivity.this, getBaseContext().getString(R.string.no_internet_error), false);
                }
                else {
                    if (tweet.getRetweeted()) {
                        Utilities.reTweet(tweet, false, getApplicationContext());
                        tweet.setRetweeted(false);
                        tweet.setRetweetCount(tweet.getRetweetCount() - 1);
                        ivDetailRetweetCount.setImageResource(R.drawable.retweet);
                        tvDetailRetweetCount.setTextColor(Color.parseColor("#808080"));
                        tvDetailRetweetCount.setText(tweet.getRetweetCount()+"");
                        tvRetweetsCount.setText(tweet.getRetweetCount()+"");

                    } else {
                        Utilities.reTweet(tweet, true, getApplicationContext());
                        tweet.setRetweeted(true);
                        tweet.setRetweetCount(tweet.getRetweetCount() + 1);
                        ivDetailRetweetCount.setImageResource(R.drawable.retweet_on);
                        tvDetailRetweetCount.setTextColor(Color.parseColor("#5C913B"));
                        tvDetailRetweetCount.setText(tweet.getRetweetCount()+"");
                      //  Utilities.composeTweet(DetailTweetActivity.this, tweet, Constants.ACTION_RETWEET, 0); // Quote a tweet
                        tvRetweetsCount.setText(tweet.getRetweetCount()+"");
                    }
                }
            }
        });

        tvDetailRetweetCount.setText(tweet.getRetweetCount()+"");
        tvRetweetsCount.setText(tweet.getRetweetCount()+"");

        ivDetailFavouriteCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!Utilities.isNetworkAvailable(getApplicationContext()))
                {
                    Utilities.showAlertDialog(DetailTweetActivity.this, getBaseContext().getString(R.string.no_internet_error), false);
                }
                else {
                    if (tweet.getFavorited()) {
                        Utilities.favourite(tweet, false, getApplicationContext());
                        tweet.setFavorited(false);
                        tweet.setFavouritesCount(tweet.getFavouritesCount() - 1);
                        ivDetailFavouriteCount.setImageResource(R.drawable.favorite);
                        tvDetailFavoriteCount.setText(tweet.getFavouritesCount()+"");
                        tvDetailFavoriteCount.setTextColor(Color.parseColor("#808080"));
                        tvFavouritesCount.setText(tweet.getFavouritesCount()+"");
                    } else {
                        Utilities.favourite(tweet, true, getApplicationContext());
                        tweet.setFavorited(true);
                        tweet.setFavouritesCount(tweet.getFavouritesCount() + 1);
                        //tvDetailFavoriteCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_on, 0, 0, 0);
                        ivDetailFavouriteCount.setImageResource(R.drawable.favorite_on);
                        tvDetailFavoriteCount.setTextColor(Color.parseColor("#FFAC33"));
                        tvDetailFavoriteCount.setText(tweet.getFavouritesCount()+"");
                        tvFavouritesCount.setText(tweet.getFavouritesCount()+"");
                    }
                }

            }
        });

        tvDetailFavoriteCount.setText(tweet.getFavouritesCount()+"");
        tvFavouritesCount.setText(tweet.getFavouritesCount()+"");
        ivDetailShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = tweet.getUser();
                SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a",
                        Locale.US);
                String strTime = formatTime.format(tweet.getCreatedAt());
                SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yy",
                        Locale.US);
                String strDate = formatDate.format(tweet.getCreatedAt());
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String strShareText = user.getUserName() + " (@" + user.getScreenName()
                        + ") tweeted at " + strTime + " on " + strDate + ": "
                        + tweet.getBody();
                shareIntent.putExtra(Intent.EXTRA_TEXT, strShareText);
                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
        });
        //setBottomBarMetadata();

        // For Rounded image in profile picture
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl())
                .transform(transformation)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivProfile);

        if (tweet.getRetweetedUserId() != 0) {
            client.getUserInfo(tweet.getRetweetedUserId(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (tweet.getRetweetedUserId() != 0) {
                        User retweetedUser = User.fromJson(response);
                      //  btTweetStatus.setVisibility(View.VISIBLE);
                       // btTweetStatus.setText(" "
                             //   + retweetedUser.getUserName() + " retweeted");
                    } else {
                       // btTweetStatus.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                  //  btTweetStatus.setVisibility(View.GONE);
                    throwable.printStackTrace();
                }
            });
        }

        String body = tweet.getBody();
        if (tweet.getActualUrl() != null && tweet.getDisplayUrl() != null) {
            body = body.replace(tweet.getActualUrl(), tweet.getDisplayUrl());
        }
        tvBody.setText(body);
        SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a",
                Locale.US);
        String strTime = formatTime.format(tweet.getCreatedAt());
        SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yy",
                Locale.US);
        String strDate = formatDate.format(tweet.getCreatedAt());
        tvDate.setText(strTime + " . "+ strDate);
        if (tweet.getActualUrl() != null && tweet.getActualUrl().length() > 0) {
            wvMedia.setVisibility(View.VISIBLE);
            wvMedia.getSettings().setJavaScriptEnabled(true);
            wvMedia.loadUrl(tweet.getActualUrl());
            wvMedia.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }
            });
        } else {
            wvMedia.setVisibility(View.GONE);
        }

    }


}