package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.DetailTweetActivity;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.apps.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.Constants;
import com.codepath.apps.twitterclient.helpers.Utilities;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.net.TwitterClient;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.util.List;


public class HomeTweetsAdapter extends ArrayAdapter<Tweet> implements ListAdapter{
    private HomeTweetsAdapterListener listener;

    // Define the events that the fragment will use to communicate
    public interface HomeTweetsAdapterListener {
        public void profileClicked(User user);
    }

    TwitterClient client = TwitterApplication.getRestClient();

    public HomeTweetsAdapter(Context context, List<Tweet> tweets, HomeTweetsAdapterListener listener) {
        super(context, 0, tweets);
        this.listener = listener;
    }
    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
    private static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvRelativeTime;
        TextView tvName;
        TextView tvScreenName;
        TextView tvBody;
        ImageView ivImage;
        TextView tvRetweetCount;
        TextView tvFavoritesCount;
        TextView tvReply;
        TextView tvRetweetedUser;
        RelativeLayout rlRetweetHolder;
        RelativeLayout rlFavoritesHolder;
        RelativeLayout rlRetweetInfoRow;
        long id;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       Tweet tweet = getItem(position);
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflator = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_tweet, null);
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
            viewHolder.tvRetweetedUser = (TextView) convertView.findViewById(R.id.tvRetweetedUser);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweetCount);
            viewHolder.tvFavoritesCount = (TextView) convertView.findViewById(R.id.tvFavoritesCount);
            viewHolder.tvReply = (TextView) convertView.findViewById(R.id.tvReply);
            viewHolder.rlRetweetHolder = (RelativeLayout) convertView.findViewById(R.id.rlRetweetHolder);
            viewHolder.rlFavoritesHolder = (RelativeLayout) convertView.findViewById(R.id.rlFavoritesHolder);
            viewHolder.rlRetweetInfoRow = (RelativeLayout) convertView.findViewById(R.id.rlRetweetInfoRow);
            viewHolder.id = tweet.getUniqueId();
            convertView.setTag(viewHolder);
            convertView.setClickable(true);
            convertView.setFocusable(true);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // converting time to relative time span
        String relativeDate="";
        try {
            //error processing.
            String createdTime = tweet.getStrCreatedAt();
            if(createdTime!=null) {
                relativeDate = Utilities.getRelativeTimeAgo(tweet.getStrCreatedAt());
            }
            viewHolder.tvRelativeTime.setText(relativeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Tweet tweetToDisplay = null;
        if (tweet.hasOriginalTweet()) {
            viewHolder.rlRetweetInfoRow.setVisibility(View.VISIBLE);
            viewHolder.tvRetweetedUser.setText(tweet.getUser().getUserName() + " retweeted");
            tweetToDisplay = tweet.getRetweetedTweet();
            setViews(viewHolder, tweetToDisplay);
        } else {
            viewHolder.rlRetweetInfoRow.setVisibility(View.GONE);
            setViews(viewHolder, tweet);
        }


        return convertView;
    }
private void setViews (final ViewHolder viewHolder, final Tweet tweet){
    viewHolder.tvName.setText(Html.fromHtml("<b>"+tweet.getUser().getUserName()+"</b> <font color='"+ Constants.GRAY_COLOR+"'> @"+tweet.getUser().getScreenName()+"</font>"));
    viewHolder.tvBody.setText(tweet.getBody());
    if(tweet.getMedia_url()!=null){
        resizeImageView(viewHolder.ivImage, true);
        Picasso.with(getContext()).load(tweet.getMedia_url())
                .placeholder(R.drawable.ic_image_placeholder)
                .resize(tweet.getMediaWidth(), tweet.getMediaHeight())
                .into(viewHolder.ivImage);
    } else {
        resizeImageView(viewHolder.ivImage, false);
    }

    Transformation transformation = new RoundedTransformationBuilder()
            .borderColor(Color.BLACK)
            .borderWidthDp(1)
            .cornerRadiusDp(30)
            .oval(false)
            .build();
    Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl())
            .transform(transformation)
            .placeholder(R.mipmap.ic_launcher)
            .into(viewHolder.ivProfileImage);

    if (tweet.getFavorited()) {
        viewHolder.tvFavoritesCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_on_small, 0, 0, 0);
        viewHolder.tvFavoritesCount.setTextColor(Color.parseColor("#FFAC33"));
    } else {
        viewHolder.tvFavoritesCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_small, 0, 0, 0);
        viewHolder.tvFavoritesCount.setTextColor(Color.parseColor("#808080"));
    }

    if(tweet.getRetweeted())
    {
        viewHolder.tvRetweetCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.retweet_on_small, 0, 0, 0);
        viewHolder.tvRetweetCount.setTextColor(Color.parseColor("#5C913B"));
    } else {
        viewHolder.tvRetweetCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.retweet_small, 0, 0, 0);
        viewHolder.tvRetweetCount.setTextColor(Color.parseColor("#808080"));
    }


    final User currentUser = tweet.getUser();


    viewHolder.tvReply.setOnClickListener(new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            if(!Utilities.isNetworkAvailable(getContext()))
            {
                Utilities.showAlertDialog(getContext(), getContext().getString(R.string.no_internet_error), false);
            } else {
                Utilities.composeTweet(getContext(), tweet, Constants.ACTION_REPLY, 0);
            }
        }
    });
    viewHolder.tvRetweetCount.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!Utilities.isNetworkAvailable(getContext()))
            {
                Utilities.showAlertDialog(getContext(), getContext().getString(R.string.no_internet_error), false);
            }
            else {
                if (tweet.getRetweeted()) {
                    Utilities.reTweet(tweet, false, getContext());
                    tweet.setRetweeted(false);
                    tweet.setRetweetCount(tweet.getRetweetCount() - 1);
                    viewHolder.tvRetweetCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.retweet_small, 0, 0, 0);
                    viewHolder.tvRetweetCount.setTextColor(Color.parseColor("#808080"));
                    viewHolder.tvRetweetCount.setText(tweet.getRetweetCount()+"");

                } else {
                    Utilities.reTweet(tweet, true, getContext());
                    tweet.setRetweeted(true);
                    tweet.setRetweetCount(tweet.getRetweetCount() + 1);
                    viewHolder.tvRetweetCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.retweet_on_small, 0, 0, 0);
                    viewHolder.tvRetweetCount.setTextColor(Color.parseColor("#5C913B"));
                    viewHolder.tvRetweetCount.setText(tweet.getRetweetCount()+"");
                    // Utilities.composeTweet(getContext(), tweet, Constants.ACTION_RETWEET, 0); // Quote a tweet
                }
            }
        }
    });

    viewHolder.tvRetweetCount.setText(tweet.getRetweetCount()+"");

    viewHolder.tvFavoritesCount.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(!Utilities.isNetworkAvailable(getContext()))
            {
                Utilities.showAlertDialog(getContext(), getContext().getString(R.string.no_internet_error), false);
            }
            else {
                if (tweet.getFavorited()) {
                    Utilities.favourite(tweet, false, getContext());
                    tweet.setFavorited(false);
                    tweet.setFavouritesCount(tweet.getFavouritesCount() - 1);
                    viewHolder.tvFavoritesCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_small, 0, 0, 0);
                    viewHolder.tvFavoritesCount.setText(tweet.getFavouritesCount()+"");
                    viewHolder.tvFavoritesCount.setTextColor(Color.parseColor("#808080"));
                } else {
                    Utilities.favourite(tweet, true, getContext());
                    tweet.setFavorited(true);
                    tweet.setFavouritesCount(tweet.getFavouritesCount() + 1);
                    viewHolder.tvFavoritesCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_on_small, 0, 0, 0);
                    viewHolder.tvFavoritesCount.setTextColor(Color.parseColor("#FFAC33"));
                    viewHolder.tvFavoritesCount.setText(tweet.getFavouritesCount()+"");
                }
            }

        }
    });



    viewHolder.tvFavoritesCount.setText(tweet.getFavouritesCount()+"");



    // View listener for showing the profile of user
    viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Launch the profile activity directly from the fragment
            Intent i = new Intent(getContext(), ProfileActivity.class);
            User.setCurrentUser(tweet.getUser());
            i.putExtra("screen_name", tweet.getUser().getScreenName());
            i.putExtra("user_name", tweet.getUser().getUserName());
            i.putExtra("user", tweet.getUser());
            i.putExtra("ParentClass", this.getClass());
             getContext().startActivity(i);
        }
    });

    viewHolder.tvBody.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Launch the profile activity directly from the fragment
            Intent i = new Intent(getContext(), DetailTweetActivity.class);
            i.putExtra("selectedTweet", tweet);
            getContext().startActivity(i);
        }
    });
}
    private void resizeImageView(ImageView imageView, boolean flag) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.height = flag ? (int) getContext().getResources().getDimension(R.dimen.preview_image_height) : 0;
        imageView.setLayoutParams(params);
    }

    public void setCallback(HomeTweetsAdapterListener listener){
        this.listener = listener;
    }

 }
