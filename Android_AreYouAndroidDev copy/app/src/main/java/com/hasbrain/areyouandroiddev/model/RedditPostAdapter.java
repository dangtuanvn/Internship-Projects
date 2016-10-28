package com.hasbrain.areyouandroiddev.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;

import java.util.List;

public class RedditPostAdapter extends ArrayAdapter<RedditPost> {
    Context context;
    int resource;
    List<RedditPost> postList;
    int mode;

    public RedditPostAdapter(Context context, int resource) {
        super(context, resource);
    }

    public RedditPostAdapter(Context context, int resource, List<RedditPost> postList, int mode) {
        super(context, resource, postList);
        this.context = context;
        this.resource = resource;
        this.postList = postList;
        this.mode = mode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(mode == 0) {
            // LIST VIEW
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.list_view_item, null);
            }

            // object item based on the position
            RedditPost post = postList.get(position);

            if (post != null) {
                // get the TextView and then set the text (item name) and tag (item ID) values
                TextView score = (TextView) v.findViewById(R.id.score);
                score.setText(Integer.toString(post.getScore()) + " ");

                TextView author = (TextView) v.findViewById(R.id.author);
                author.setText(post.getAuthor() + " ");

                TextView subreddit = (TextView) v.findViewById(R.id.subreddit);
                subreddit.setText(post.getSubreddit());


                TextView title = (TextView) v.findViewById(R.id.title);
                if (post.isStickyPost()) {
                    title.setTextColor(Color.parseColor("#387801"));
                } else {
                    title.setTextColor(Color.rgb(0, 0, 0));
                }
                title.setText(post.getTitle());

                TextView num_comments = (TextView) v.findViewById(R.id.num_comments);
                num_comments.setText(Integer.toString(post.getCommentCount()) + " Comments · ");

                TextView domain = (TextView) v.findViewById(R.id.domain);
                domain.setText(post.getDomain());
            }
        }

        else{
            // GRID VIEW
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.grid_view_item, null);
                //   v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, ));
            }


            // object item based on the position
            RedditPost post = postList.get(position);

            if (post != null) {
                // get the TextView and then set the text (item name) and tag (item ID) values
                TextView score = (TextView) v.findViewById(R.id.gScore);
                score.setText(Integer.toString(post.getScore()) + " ");

                TextView author = (TextView) v.findViewById(R.id.gAuthor);
                author.setText(post.getAuthor() + " ");

                TextView subreddit = (TextView) v.findViewById(R.id.gSubreddit);
                subreddit.setText(post.getSubreddit());


                TextView title = (TextView) v.findViewById(R.id.gTitle);
                if (post.isStickyPost()) {
                    title.setTextColor(Color.parseColor("#387801"));
                } else {
                    title.setTextColor(Color.rgb(0, 0, 0));
                }
                title.setText(post.getTitle());

                TextView num_comments = (TextView) v.findViewById(R.id.gNum_comments);
                num_comments.setText(Integer.toString(post.getCommentCount()) + " Comments · ");
            }
        }
        return v;
    }
}
