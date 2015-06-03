package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

public class UserHeaderFragmentTwo extends Fragment {

    private static final float BLUR_RADIUS = 25F;
    private User user;

    public static UserHeaderFragmentTwo newInstance(User user) {
        UserHeaderFragmentTwo fragment = new UserHeaderFragmentTwo();
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

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_header_page2, container, false);

        ImageView ivImage = (ImageView) view.findViewById(R.id.ivProfileHeaderImage);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvProfileHeaderDescription);

        ivImage.setImageResource(0);
        Picasso.with(getActivity()).load(user.getBackgroundImageUrl()).into(ivImage);

        tvDescription.setFocusable(true);
        tvDescription.setText(Html.fromHtml("<p align=\"center\">" + user.getDescription()));

        return view;
    }
}
