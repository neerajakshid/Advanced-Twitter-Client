package com.codepath.apps.twitterclient.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.UserProfileHeaderAdapter;
import com.codepath.apps.twitterclient.apps.TwitterApplication;
import com.codepath.apps.twitterclient.fragments.UserHeaderFragmentOne;
import com.codepath.apps.twitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.net.TwitterClient;

public class ProfileActivity extends ActionBarActivity  {
    TwitterClient client = TwitterApplication.getRestClient();
    User user;
    private ViewPager vpProfileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // get the screenname
        String screenName = getIntent().getStringExtra("screen_name");
        String userName = getIntent().getStringExtra("user_name");
        // Setup ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(userName);

        vpProfileView = (ViewPager) findViewById(R.id.vpProfileView);
        vpProfileView.setAdapter(new UserProfileHeaderAdapter(getSupportFragmentManager(), user));
        if (savedInstanceState == null) {
            // create the user Timeline Fragment
            UserTimelineFragment fragmentTimeline = UserTimelineFragment.newInstance(screenName);
            //display the user timeline freagment within activity (dynamacially)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentTimeline);
            ft.commit();

            setupActionBar();

            TextView tvProfileTweetCount = (TextView) findViewById(R.id.tvTweetsCount);
            TextView tvProfileFollowersCount = (TextView) findViewById(R.id.tvFollowingCount);
            TextView tvProfileFollowingCount = (TextView) findViewById(R.id.tvFollowersCount);

            final User user = getIntent().getParcelableExtra("user");

            vpProfileView = (ViewPager) findViewById(R.id.vpProfileView);
            vpProfileView.setAdapter(new UserProfileHeaderAdapter(getSupportFragmentManager(), user));

            tvProfileTweetCount.setText(Html.fromHtml("<b><font color=\"black\">" + user.getTweetsCount() + "</font></b>" + "<br/><font color=\"808080\"> TWEETS </font>"));
            tvProfileFollowersCount.setText(Html.fromHtml("<b><font color=\"black\">" + user.getFollowersCount() + "</font></b>" + "<br/><font color=\"808080\"> FOLLOWERS</font>"));
            tvProfileFollowingCount.setText(Html.fromHtml("<b><font color=\"black\">" + user.getFollowingsCount() + "</font></b>" + "<br/><font color=\"808080\"> FOLLOWING</font>"));

            tvProfileFollowersCount.setClickable(true);
            tvProfileFollowersCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileActivity.this, UserActivity.class);
                    Bundle data = new Bundle();
                    data.putParcelable("user", user);
                    data.putString("user_type", UserActivity.UserType.FOLLOWERS.name());

                    intent.putExtras(data);
                    startActivityForResult(intent, 201);
                }
            });

            tvProfileFollowingCount.setClickable(true);
            tvProfileFollowingCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileActivity.this, UserActivity.class);
                    Bundle data = new Bundle();
                    data.putParcelable("user", user);
                    data.putString("user_type", UserActivity.UserType.FOLLOWING.name());
                    intent.putExtras(data);
                    startActivityForResult(intent, 201);
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        if (vpProfileView.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            vpProfileView.setCurrentItem(vpProfileView.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_custom_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public Intent getSupportParentActivityIntent() {
        Class<?> parentClass = (Class) getIntent().getSerializableExtra("ParentClass");
        Intent i = new Intent(this, parentClass);
        return i;
    }

    @Override
    public void onCreateSupportNavigateUpTaskStack(TaskStackBuilder builder) {
                super.onCreateSupportNavigateUpTaskStack(builder);
           }
}
