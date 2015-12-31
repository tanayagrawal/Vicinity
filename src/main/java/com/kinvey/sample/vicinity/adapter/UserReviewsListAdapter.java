package com.kinvey.sample.vicinity.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.object.Review;
import com.kinvey.sample.vicinity.util.SetTypeface;

import java.util.ArrayList;

/**
 * Created by tanayagrawal on 26/12/15.
 */
public class UserReviewsListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Review> userReview = new ArrayList<Review>();
    ViewHolder holder;

    public UserReviewsListAdapter(Context context, ArrayList<Review> userReview){
        this.context = context;
        this.userReview = userReview;

    }

    @Override
    public int getCount() {
        return userReview.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.reviews_item, null);

        }
        holder = new ViewHolder();
        holder.userName = (TextView) convertView.findViewById(R.id.user_name);
        holder.userReview = (TextView) convertView.findViewById(R.id.user_review);
        holder.userRating = (TextView) convertView.findViewById(R.id.user_rating);
        setTypeface();

        holder.userName.setText(userReview.get(position).getAuthor_name());
        holder.userReview.setText(userReview.get(position).getText());
        holder.userRating.setText("" + userReview.get(position).getRating());
        convertView.setTag(holder);
        return convertView;
    }

    public void setTypeface(){
        SetTypeface typeface = new SetTypeface(context);
        Typeface tf = typeface.getFont("bold");
        holder.userName.setTypeface(tf);

        Typeface tf1 = typeface.getFont("regular");
        holder.userRating.setTypeface(tf1);
        holder.userReview.setTypeface(tf1);
    }

    static class ViewHolder{

        TextView userName;
        TextView userReview;
        TextView userRating;
    }
}



