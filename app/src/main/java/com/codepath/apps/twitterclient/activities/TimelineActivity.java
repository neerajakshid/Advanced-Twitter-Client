package com.codepath.apps.twitterclient.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.HomeTweetsAdapter;
import com.codepath.apps.twitterclient.adapters.SmartFragmentStatePagerAdapter;
import com.codepath.apps.twitterclient.apps.TwitterApplication;
import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimeLineFragment;
import com.codepath.apps.twitterclient.fragments.TweetListFragment;
import com.codepath.apps.twitterclient.helpers.Constants;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.net.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

public class TimelineActivity extends ActionBarActivity{
    private SmartFragmentStatePagerAdapter adapterViewPager;
    private TweetListFragment fragmentTweetsList;
    private User currentUser;
    TwitterClient client = TwitterApplication.getRestClient();
    ViewPager viewPager;
    TweetsPagerAdapter tweetsPagerAdapter;
    SearchView miSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
// adding logo to the actionbar at runtime
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // get hte viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //set the viewpager adapter for the pager
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // find the string tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the tabstrip to the view pager
        tabStrip.setViewPager(viewPager);

    }


   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
       MenuItem searchItem = menu.findItem(R.id.action_search);
       miSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

       miSearchView.setIconifiedByDefault(false);
       miSearchView.setMaxWidth(800);

       miSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               // encode any text
               Intent intent = new Intent(TimelineActivity.this, SearchActivity.class);
               intent.putExtra("query", s);
               startActivityForResult(intent, Constants.REQUEST_CODE);
               return true;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               return false;
           }
       });
       return true;
   }

    protected List<Tweet> getListDataFromDB(){
        return null;
    }

       @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.compose_tweet) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onComposeClick(MenuItem menuItem) {
        Intent i = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
        startActivityForResult(i, Constants.REQUEST_CODE);
    }

    // onclick of an item in a list view
     public void showDetialTweet(Tweet selectedTweet) {
        Intent intent = new Intent(TimelineActivity.this, DetailTweetActivity.class);
        intent.putExtra("selectedTweet", selectedTweet);
        startActivity(intent);
   }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent result) {
         super.onActivityResult(requestCode, resultCode, result);
         if (resultCode == RESULT_OK) {
             Tweet newTweet = (Tweet) result.getExtras().get("newTweet");
             for (int i = 0; i < 2; i++) {
                 if (tweetsPagerAdapter.getPageTitle(i).toString().equals("Home")) {
                     fragmentTweetsList = (TweetListFragment) getSupportFragmentManager().findFragmentByTag(getTag(i));
                     fragmentTweetsList.getTweetsAdapter().insert(newTweet, 0);
                     fragmentTweetsList.getTweetsAdapter().notifyDataSetChanged();
                 }
             }
         }
     }
    private String getTag(int position) {
        return "android:switcher:" + viewPager.getId() + ":" + position;
    }
   private void launchProfileActivity() {
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.putExtra("screen_name", User.getCurrentUser().getScreenName());
        intent.putExtra("user_name", User.getCurrentUser().getUserName());
        intent.putExtra("user", User.getCurrentUser());
        intent.putExtra("parentClass", this.getClass());

        startActivity(intent);
   }

    public void onProfileClick(MenuItem mi) {
        // lauch the profile of the user
// we need the user before we can show his details in the profile view

        if(User.getCurrentUser()!= null){
            //launch profile activity
            launchProfileActivity();

        }
        else {
            client.verifyAccountCredentials(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                    //Deserialize the json object
                    //create the models and add them to the adapter
                    //Load the model data in to the listview

                    User user = User.fromJson(jsonObject);//gets the current user
                    User.setCurrentUser(user);
                    Log.d("DEBUG", jsonObject.toString());
                    launchProfileActivity();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                    Toast.makeText(TimelineActivity.this, "ERROR fetching user details", Toast.LENGTH_SHORT).show();
                }
            });
        }
   }



        public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};
        private SmartFragmentStatePagerAdapter adapterViewPager;

        // Adapter gets teh manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // TimelineFragment fragmentHomeTimeline = TimelineFragment.newInstance(Utilities.TimelineType.HOME.toString(), null);
                return new HomeTimelineFragment();

            } else if (position == 1) {
                return new MentionsTimeLineFragment();

            } else
                return null;
        }


        // Return the no of fragments to swipe
        @Override
        public int getCount() {
            return tabTitles.length;
        }
        // Return the page title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
   }

    public void onLogout(MenuItem mi){
        client.clearAccessToken();
        finish();
    }

}
