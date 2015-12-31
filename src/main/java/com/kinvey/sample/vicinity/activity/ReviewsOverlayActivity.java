package com.kinvey.sample.vicinity.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.adapter.UserReviewsListAdapter;
import com.kinvey.sample.vicinity.object.Review;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewsOverlayActivity extends Activity {

    @Bind(R.id.reviews_listview)
    ListView reviews_listview;

    UserReviewsListAdapter userReviewsListAdapter;
    ArrayList<Review> userReviews = new ArrayList<Review>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_overlay);

        ButterKnife.bind(this);

        userReviews = PlaceDetailsActivity.userReviewsWithText;
        initializeListAdapter();
    }


    public void initializeListAdapter(){
        userReviewsListAdapter = new UserReviewsListAdapter(this,userReviews);
        reviews_listview.setAdapter(userReviewsListAdapter);

    }
}
