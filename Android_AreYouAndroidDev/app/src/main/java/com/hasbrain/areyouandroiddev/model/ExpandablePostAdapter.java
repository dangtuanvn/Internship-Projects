package com.hasbrain.areyouandroiddev.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;

import java.util.HashMap;
import java.util.List;

public class ExpandablePostAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<RedditPost>> _listDataChild;

    public ExpandablePostAdapter(Context context, List<String> listDataHeader, HashMap<String, List<RedditPost>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final  RedditPost childPost = (RedditPost) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_view_item, null);
            }

            TextView score = (TextView) convertView.findViewById(R.id.score);
            score.setText(Integer.toString(childPost.getScore()) + " ");

            TextView author = (TextView) convertView.findViewById(R.id.author);
            author.setText(childPost.getAuthor() + " ");

            TextView subreddit = (TextView) convertView.findViewById(R.id.subreddit);
            subreddit.setText(childPost.getSubreddit());


            TextView title = (TextView) convertView.findViewById(R.id.title);
            if (childPost.isStickyPost()) {
                title.setTextColor(Color.parseColor("#387801"));
            } else {
                title.setTextColor(Color.rgb(0, 0, 0));
            }
            title.setText(childPost.getTitle());

            TextView num_comments = (TextView) convertView.findViewById(R.id.num_comments);
            num_comments.setText(Integer.toString(childPost.getCommentCount()) + " Comments · ");

            TextView domain = (TextView) convertView.findViewById(R.id.domain);
            domain.setText(childPost.getDomain());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
