package com.hasbrain.areyouandroiddev.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.PostViewActivity;
import com.hasbrain.areyouandroiddev.R;

import java.util.List;

/**
 * Created by dangtuanvn on 10/6/16.
 */

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.ViewHolder> {

    private List<RedditPost> postList;
    private Context mContext;

    public RecyclerGridAdapter(Context context, List<RedditPost> postList) {
        this.postList = postList;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View gridView = inflater.inflate(R.layout.grid_view_item, parent, false);

        return new ViewHolder(gridView);
    }

    @Override
    public void onBindViewHolder(RecyclerGridAdapter.ViewHolder viewHolder, final int position) {
        RedditPost post = postList.get(position);

        TextView score = viewHolder.score;
        TextView author = viewHolder.author;
        TextView subreddit = viewHolder.subreddit;
        TextView title = viewHolder.title;
        TextView num_comments = viewHolder.num_comments;

        score.setText(Integer.toString(post.getScore()) + " ");
        author.setText(post.getAuthor() + " ");
        subreddit.setText(post.getSubreddit());
        if (post.isStickyPost()) {
            title.setTextColor(Color.parseColor("#387801"));
        } else {
            title.setTextColor(Color.rgb(0, 0, 0));
        }
        title.setText(post.getTitle());
        num_comments.setText(Integer.toString(post.getCommentCount()) + " Comments · ");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostViewActivity.class);
                intent.putExtra("url", postList.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView score;
        public TextView author;
        public TextView subreddit;
        public TextView title;
        public TextView num_comments;
        public TextView domain;

        public ViewHolder(View itemView) {
            super(itemView);
            score = (TextView) itemView.findViewById(R.id.gScore);
            author = (TextView) itemView.findViewById(R.id.gAuthor);
            subreddit = (TextView) itemView.findViewById(R.id.gSubreddit);
            title = (TextView) itemView.findViewById(R.id.gTitle);
            num_comments = (TextView) itemView.findViewById(R.id.gNum_comments);
        }
    }
}

