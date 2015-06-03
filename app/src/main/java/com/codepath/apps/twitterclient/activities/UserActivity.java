package com.codepath.apps.twitterclient.activities;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.UserAdapter;
import com.codepath.apps.twitterclient.apps.TwitterApplication;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.net.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;


public class UserActivity extends ActionBarActivity {

    public enum UserType {
        FOLLOWING, FOLLOWERS
    }

    private ArrayList<User> users;
    private ListView lvUsers;
    private UserAdapter userAdapter;
    private TwitterClient client;

    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        users = new ArrayList<>();
        lvUsers = (ListView) findViewById(R.id.lvUsers);
        userAdapter = new UserAdapter(this, users);
        lvUsers.setAdapter(userAdapter);
        client = TwitterApplication.getRestClient();

        User user = getIntent().getParcelableExtra("user");
        type = getIntent().getStringExtra("user_type");
        setupActionBar();
        getListOfUsers(user, type);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getListOfUsers(User user, String type) {
        if (type.equals(UserType.FOLLOWERS.name())) {

            client.getAllFollowers(user, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    userAdapter.clear();
                    userAdapter.addAll(User.fromJsonArray(response.optJSONArray("users")));
                }
            });
        } else if (type.equals(UserType.FOLLOWING.name())) {

            client.getAllFollowing(user, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    userAdapter.clear();
                    userAdapter.addAll(User.fromJsonArray(response.optJSONArray("users")));
                }
            });
        }
    }
    private void setupActionBar(){

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        if (type.equals(UserType.FOLLOWERS.name())) {
            getSupportActionBar().setTitle("Followers");
        } else {
            getSupportActionBar().setTitle("Following");
        }

    }
}


