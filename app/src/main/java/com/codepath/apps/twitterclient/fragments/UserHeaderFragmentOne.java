package com.codepath.apps.twitterclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.activities.UserActivity;
import com.codepath.apps.twitterclient.apps.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.Constants;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.net.TwitterClient;
import com.squareup.picasso.Picasso;

public class UserHeaderFragmentOne extends Fragment {
    User user;
    TwitterClient client = TwitterApplication.getRestClient();
    TextView tvProfileName, tvScreenName ;
    ImageView ivProfile, ivBackgroundImage;
    User currentUser;

    public UserHeaderFragmentOne(){
    }

    public static UserHeaderFragmentOne newInstance(User user) {
        UserHeaderFragmentOne fragment = new UserHeaderFragmentOne();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_profile_header, container, false);
        setupViews(v);
        populateProfileHeader(User.getCurrentUser());
        return v;
    }

    public void setupViews(View v) {
        tvProfileName = (TextView) v.findViewById(R.id.tvProfileName);
        tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
        ivProfile = (ImageView) v.findViewById(R.id.ivProfile);
        ivBackgroundImage = (ImageView) v.findViewById(R.id.ivBackgroundImage);
    }

    public void populateProfileHeader(final User user){
        ivProfile.setImageResource(0);
        Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivProfile);
        ivBackgroundImage.setImageResource(0);
        Picasso.with(getActivity()).load(user.getBackgroundImageUrl()).into(ivBackgroundImage);
        tvProfileName.setText(user.getUserName());
        tvScreenName.setText("@"+user.getScreenName());
    }

    public void setCurrentUser(User user){
        this.currentUser= user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

}
