package com.atlantbh.boristomic.movieapplication.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.models.rest.Review;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boristomic on 03/02/16.
 */
public class ReviewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.review_author_name)
    protected TextView authorName;
    @Bind(R.id.review_content)
    protected TextView reviewContent;

    private Context context;

    public ReviewsHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void bind(Review review) {
        authorName.setText(review.getAuthor());
        reviewContent.setText(review.getContent());
    }

    @Override
    public void onClick(View v) {

    }
}
