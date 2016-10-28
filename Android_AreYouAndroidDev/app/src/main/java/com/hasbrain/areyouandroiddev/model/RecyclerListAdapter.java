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

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_LOADING = 3;
    private List<RedditPost> postList;
    private Context mContext;

    public RecyclerListAdapter(Context context, List<RedditPost> postList) {
        this.postList = postList;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        if(viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            return new FooterViewHolder(v);
        }

        if(viewType == TYPE_LOADING){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false);
            return new LoadingViewHolder(v);
        }

        else if(viewType == TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(context);

            View listView = inflater.inflate(R.layout.list_view_item, parent, false);
            return new ViewHolder(listView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if(viewHolder instanceof RecyclerListAdapter.FooterViewHolder) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PostViewActivity.class);
                    intent.putExtra("url", "https://www.reddit.com/r/androiddev/");
                    mContext.startActivity(intent);
                }
            });
        }

        if(viewHolder instanceof RecyclerListAdapter.LoadingViewHolder) {

        }

        else if(viewHolder instanceof RecyclerListAdapter.ViewHolder) {
            RedditPost post = postList.get(position);
            RecyclerListAdapter.ViewHolder itemViewHolder = (RecyclerListAdapter.ViewHolder) viewHolder;

            TextView score = itemViewHolder.score;
            TextView author = itemViewHolder.author;
            TextView subreddit = itemViewHolder.subreddit;
            TextView title = itemViewHolder.title;
            TextView num_comments = itemViewHolder.num_comments;
            TextView domain = itemViewHolder.domain;

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
            domain.setText(post.getDomain());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PostViewActivity.class);
                    intent.putExtra("url", postList.get(position).getUrl());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView score;
        public TextView author;
        public TextView subreddit;
        public TextView title;
        public TextView num_comments;
        public TextView domain;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            score = (TextView) itemView.findViewById(R.id.score);
            author = (TextView) itemView.findViewById(R.id.author);
            subreddit = (TextView) itemView.findViewById(R.id.subreddit);
            title = (TextView) itemView.findViewById(R.id.title);
            num_comments = (TextView) itemView.findViewById(R.id.num_comments);
            domain = (TextView) itemView.findViewById(R.id.domain);
        }
    }

    @Override
    public int getItemCount() {
        if(!loading){
            return postList.size() + 1;
        }
        return postList.size() + 2;
    }

    @Override
    public int getItemViewType (int position) {
        if(isPositionFooter (position)) {
            return TYPE_FOOTER;
        }
        else if(isPositionLoading(position)){
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        if(!loading){
            return position == postList.size();
        }
        return position == postList.size() + 1;
    }

    private boolean isPositionLoading(int position) {
        if(!loading){
            return false;
        }
        return position == postList.size();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private boolean loading = true;
    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}