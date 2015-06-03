package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Users")
public class User extends Model implements Parcelable {
    @Column(name = "user_name")
    public String userName;
    @Column(name = "userId", unique = true)
    public long userId;
    @Column(name = "screen_name")
    public String screenName;
    @Column(name = "profile_image_url")
    public String profileImageUrl;
    @Column(name = "tag_line")
    private String tagLine;
    @Column(name = "followers_count")
    private int followersCount;
    @Column(name = "following_count")
    private int followingsCount;
    @Column(name = "background_image_url")
    public String backgroundImageUrl;
    @Column(name = "tweets_count")
    public int tweetsCount;
    @Column(name = "description")
    private String description;


    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }
    public static User fromJson(JSONObject json) {
        User user = new User();
        try {
            if (json.getString("name") != null)
                user.userName = json.getString("name");
            else
                user.userName = "";
            if (json.getString("id") != null)
                user.userId = json.getLong("id");
            else
                user.userId = 0;
            if (json.getString("screen_name") != null)
                user.screenName = json.getString("screen_name");
            else
                user.screenName = "";
            if (json.getString("profile_image_url") != null)
                user.profileImageUrl = json.getString("profile_image_url");
            else
                user.profileImageUrl = "";
            if (json.getString("description") != null)
                user.tagLine = json.getString("description");
            else
                user.tagLine = "";
            if (json.getString("followers_count") != null)
                user.followersCount = json.getInt("followers_count");
            else
                user.followersCount = 0;
            if (json.getString("friends_count") != null)
                user.followingsCount = json.getInt("friends_count");
            else
                user.followingsCount = 0;
            if (json.has("profile_banner_url"))
                user.backgroundImageUrl = json.getString("profile_banner_url") + "/mobile_retina";
            else if (json.has("profile_background_image_url")) {
                // User profile background image instead
                user.backgroundImageUrl = json.getString("profile_background_image_url");
            } else
            user.backgroundImageUrl = "";
            if (json.getString("statuses_count") != null)
                user.tweetsCount = json.getInt("statuses_count");
            else
                user.tweetsCount = 0;
            if (json.getString("description")!=null)
                user.description = json.getString("description");
            else
                user.description = "";

            //findOrCreateUserFromJSON(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User findOrCreateUserFromJSON(JSONObject json) {
        User user = null;
        try {
            long unique = json.getLong("id");
            user = new Select().from(User.class).where("userId = ?", unique).executeSingle();
            if (user == null) {
                user = User.fromJson(json);
                user.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static ArrayList<User> fromJsonArray(JSONArray usersJson)
    {
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < usersJson.length(); i++) {
            users.add(User.fromJson(usersJson.optJSONObject(i)));
        }
        return users;
    }

    public String getUserName() {
        return userName;
    }

    public long getUserId() {
        return userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    public String getTagLine() {
        return tagLine;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public User() {
        super();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeLong(this.userId);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.tagLine);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.followingsCount);
        dest.writeString(this.backgroundImageUrl);
        dest.writeInt(this.tweetsCount);
        dest.writeString(this.description);
    }

    private User(Parcel in) {
        this.userName = in.readString();
        this.userId = in.readLong();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.tagLine = in.readString();
        this.followersCount = in.readInt();
        this.followingsCount = in.readInt();
        this.backgroundImageUrl = in.readString();
        this.tweetsCount = in.readInt();
        this.description = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}




