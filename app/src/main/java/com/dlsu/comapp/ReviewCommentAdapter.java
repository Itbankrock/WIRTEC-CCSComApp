package com.dlsu.comapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Enrico Zabayle on 20/03/2018.
 */

public class ReviewCommentAdapter extends RecyclerView.Adapter<ReviewCommentAdapter.MyViewHolder>{

    protected List<ReviewComment> reviewCommentList;
    protected HomeActivity homeContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userfname,datecreated,content;
        public ImageView userpic;

        public MyViewHolder(View view) {
            super(view);
            userfname = (TextView) view.findViewById(R.id.rv_comment_userfullname);
            datecreated = (TextView) view.findViewById(R.id.rv_comment_dateposted);
            content = (TextView) view.findViewById(R.id.rv_comment_maincontent);
            userpic = (ImageView) view.findViewById(R.id.rv_comment_userpic);
        }
    }


    public ReviewCommentAdapter(List<ReviewComment> reviewCommentList, HomeActivity homeContext) {
        this.reviewCommentList = reviewCommentList;
        this.homeContext = homeContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_comment_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ReviewComment reviewComment = reviewCommentList.get(position);
        holder.userfname.setText(reviewComment.getUserID());
        holder.datecreated.setText(reviewComment.getTimestamp());
        holder.content.setText(reviewComment.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewCommentList.size();
    }
}
