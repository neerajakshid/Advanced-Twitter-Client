package com.codepath.apps.twitterclient.adapters;

        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.codepath.apps.twitterclient.R;
        import com.codepath.apps.twitterclient.activities.ProfileActivity;
        import com.codepath.apps.twitterclient.models.User;
        import com.makeramen.roundedimageview.RoundedTransformationBuilder;
        import com.squareup.picasso.Picasso;
        import com.squareup.picasso.Transformation;

        import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    public static class UserViewHolder {
        ImageView ivUserProfilePic;
        TextView tvUserName;
        TextView tvBody;
        TextView tvScreenName;
    }

    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.item_user, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);

        UserViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new UserViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            viewHolder.ivUserProfilePic = (ImageView) convertView.findViewById(R.id.ivUserProfilePicture);
            viewHolder.tvUserName= (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (UserViewHolder) convertView.getTag();
        }

        viewHolder.ivUserProfilePic.setImageResource(android.R.color.transparent);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(user.getProfileImageUrl())
                .fit()
                .transform(transformation)
                .placeholder(R.drawable.ic_profile)
                .into(viewHolder.ivUserProfilePic);

        // View listener for showing the profile of user
        viewHolder.ivUserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the profile activity directly from the fragment
                Intent i = new Intent(getContext(), ProfileActivity.class);
                User.setCurrentUser(user);
                i.putExtra("screen_name", user.getScreenName());
                i.putExtra("user_name", user.getUserName());
                i.putExtra("user", user);
                i.putExtra("ParentClass", this.getClass());
                getContext().startActivity(i);
            }
        });

        viewHolder.tvUserName.setText(user.getUserName());
        viewHolder.tvScreenName.setText("@" + user.getScreenName());
        viewHolder.tvBody.setText(user.getDescription());

        return convertView;
    }
}


