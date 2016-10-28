package com.hasbrain.areyouandroiddev.model;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.ParentListItem;
import com.hasbrain.areyouandroiddev.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static java.text.DateFormat.getDateInstance;

public class RecyclerExpandableAdapter extends ExpandableRecyclerAdapter<RecyclerExpandableAdapter.GroupViewHolder, ChildViewHolder> {

    private static final int PARENT_FOOTER = -1;
    private static final int PARENT_NORMAL = 3;
    private static final int PARENT_BETA = 4;
    private static final int CHILD_NORMAL = 5;
    private static final int CHILD_BETA = 6;
    private LayoutInflater inflater;
    private List<RedditGroupList> groupList;
    private OnItemClick onItemClick;

    public RecyclerExpandableAdapter(Context context, List<RedditGroupList> groupList) {
        super(groupList);
        inflater = LayoutInflater.from(context);
        this.groupList = groupList;
    }

    @Override
    public int getItemViewType(int position){
        if(position == getItemCount() - 1){
            return PARENT_FOOTER;
        }
        else{
            return super.getItemViewType(position);
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == PARENT_FOOTER) {
            // GroupViewHolder pvh = onCreateParentViewHolder(viewGroup, viewType);
            View view = inflater.inflate(R.layout.footer, viewGroup, false);
            return new FooterViewHolder(view, onItemClick);
        }
        else{
            return super.onCreateViewHolder(viewGroup, viewType);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Log.i("abc", "Position " + position);
        if(position == getItemCount() - 1){
           return;
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public GroupViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup, int viewType) {
//        View view = inflater.inflate(R.layout.list_group, parentViewGroup, false);
//        return new GroupViewHolder(view);
        View view;
        switch (viewType) {
            default:
            case PARENT_NORMAL:
                view = inflater.inflate(R.layout.list_group, parentViewGroup, false);
                break;
            case PARENT_BETA:
                view = inflater.inflate(R.layout.list_group_beta, parentViewGroup, false);
                break;
        }
        return new GroupViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
//        View view = inflater.inflate(R.layout.list_view_item, childViewGroup, false);
//        return new PostViewHolder(view);
        View view;
        switch (viewType) {
            default:
            case CHILD_NORMAL:
                view = inflater.inflate(R.layout.list_view_item, childViewGroup, false);
                return new PostViewHolder(view, onItemClick);
            case CHILD_BETA:
                view = inflater.inflate(R.layout.list_view_item_beta, childViewGroup, false);
                return new BetaViewHolder(view, onItemClick);
        }
    }


    @Override
    public void onBindParentViewHolder(GroupViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        RedditGroupList group = (RedditGroupList) parentListItem;
        parentViewHolder.bind(group);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int parentPosition,  int childPosition, Object childListItem) {
        final RedditPost post = (RedditPost) childListItem;
        if(childViewHolder instanceof BetaViewHolder) {
            BetaViewHolder postView = (BetaViewHolder) childViewHolder;
            postView.bind(post);
        }
        else{
            PostViewHolder postView = (PostViewHolder) childViewHolder;
            postView.bind(post);
        }
    }

    static class GroupViewHolder extends ParentViewHolder {
        private TextView groupTitleText;
        private GroupViewHolder(View itemView) {
            super(itemView);
            groupTitleText = (TextView) itemView.findViewById(R.id.lblListHeader);
        }

        private void bind(RedditGroupList group){
            groupTitleText.setText(group.getTitle());
        }
    }

    static class PostViewHolder extends ChildViewHolder implements View.OnClickListener {
        public TextView score, author, subreddit, title, num_comments, domain, date;
        private OnItemClick onItemClick;

        private PostViewHolder(View itemView, OnItemClick onItemClick) {
            super(itemView);
            score = (TextView) itemView.findViewById(R.id.score);
            author = (TextView) itemView.findViewById(R.id.author);
            subreddit = (TextView) itemView.findViewById(R.id.subreddit);
            title = (TextView) itemView.findViewById(R.id.title);
            num_comments = (TextView) itemView.findViewById(R.id.num_comments);
            domain = (TextView) itemView.findViewById(R.id.domain);
            date = (TextView) itemView.findViewById(R.id.date);

            this.onItemClick = onItemClick;
            itemView.setOnClickListener(this);//displayWebView(post.getUrl())
        }

        private void bind(RedditPost post){
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
            domain.setText(post.getDomain() + " · ");

//            Calendar c = Calendar.getInstance();
//            System.out.println("Current time => " + c.getTime());
//            java.util.Date d = c.getTime();
//
//            java.util.Date time = new java.util.Date(post.getCreatedUTC());
//            DateFormat dateFormat = getDateInstance();
//            String now = dateFormat.format(c.getTime());
//            String day = dateFormat.format(time);
//
//
//            long diff = d.getTime() - time.getTime();
//
//            long diffSeconds = diff / 1000 % 60;
//            long diffMinutes = diff / (60 * 1000) % 60;
//            long diffHours = diff / (60 * 60 * 1000) % 24;
//            long diffDays = diff / (24 * 60 * 60 * 1000);
//
//            date.setText(Long.toString(diffDays) + " days ago");
            itemView.setTag(post);

        }

        @Override
        public void onClick(View v) {
            if (onItemClick != null) {
                onItemClick.onItemClick(v, v.getTag(), getAdapterPosition());
            }
        }
    }

    static class BetaViewHolder extends ChildViewHolder implements View.OnClickListener  {
        public TextView score, author, subreddit, title, num_comments, domain;
        private OnItemClick onItemClick;

        private BetaViewHolder(View itemView, OnItemClick onItemClick) {
            super(itemView);
            score = (TextView) itemView.findViewById(R.id.score);
            author = (TextView) itemView.findViewById(R.id.author);
            subreddit = (TextView) itemView.findViewById(R.id.subreddit);
            title = (TextView) itemView.findViewById(R.id.title);
            num_comments = (TextView) itemView.findViewById(R.id.num_comments);
            domain = (TextView) itemView.findViewById(R.id.domain);

            this.onItemClick = onItemClick;
            itemView.setOnClickListener(this);//displayWebView(post.getUrl())
        }

        private void bind(RedditPost post){
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

            itemView.setTag(post);
            // itemView.setOnClickListener(displayWebView(post.getUrl()));
        }

        @Override
        public void onClick(View v) {
            if (onItemClick != null) {
                onItemClick.onItemClick(v, v.getTag(), getAdapterPosition());

            }
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private OnItemClick onItemClick;
        private FooterViewHolder(View itemView, OnItemClick onItemClick) {
            super(itemView);
            this.onItemClick = onItemClick;
            itemView.setOnClickListener(this);//displayWebView(post.getUrl())
            itemView.setTag("https://www.reddit.com/r/androiddev/");
            }

        @Override
        public void onClick(View v) {
            if (onItemClick != null) {
                onItemClick.onItemClick(v, v.getTag(), getAdapterPosition());
            }
        }
    }

    @Override
    public boolean isParentViewType(int viewType) {
        return viewType == PARENT_BETA || viewType == PARENT_NORMAL;
    }

    @Override
    public int getParentItemViewType(int parentPosition) {
        if (groupList.get(parentPosition).isBeta()) {
            return PARENT_BETA;
        } else {
            return PARENT_NORMAL;
        }
    }

    @Override
    public int getChildItemViewType(int parentPosition, int childPosition) {
        RedditPost post = groupList.get(parentPosition).getChildItemList().get(childPosition);
        if (post.isBeta()) {
            return CHILD_BETA;
        } else {
            return CHILD_NORMAL;
        }
    }

    public interface OnItemClick {
        void onItemClick(View view, Object data, int position);
    }
}
