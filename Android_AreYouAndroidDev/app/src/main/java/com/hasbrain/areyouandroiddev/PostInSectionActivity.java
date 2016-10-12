package com.hasbrain.areyouandroiddev;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.hasbrain.areyouandroiddev.model.ExpandablePostAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.media.CamcorderProfile.get;
import static com.hasbrain.areyouandroiddev.R.id.listview;

public class PostInSectionActivity extends PostListActivity {

    List<String> listDataHeader;
    HashMap<String, List<RedditPost>> listDataChild;

    @Override
    protected void displayPostList(List<RedditPost> postList) {
        final ExpandableListView expListView = (ExpandableListView) findViewById(R.id.expandableview);
        final Context context = this;

        // preparing list data
        prepareListData(postList);

        ExpandablePostAdapter listAdapter = new ExpandablePostAdapter(this, listDataHeader, listDataChild);

        View footer = getLayoutInflater().inflate(R.layout.footer, null);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostViewActivity.class);
                intent.putExtra("url", "https://www.reddit.com/r/androiddev/");
                startActivity(intent);
            }
        });
        expListView.addFooterView(footer);

        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick (ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(context, PostViewActivity.class);
                intent.putExtra("url", (listDataChild.get(listDataHeader.get(groupPosition))).get(childPosition).getUrl());
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_in_section;
    }

    private void prepareListData(List<RedditPost> postList) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<RedditPost>>();

        // Adding child data
        listDataHeader.add("Sticky posts");
        listDataHeader.add("Normal posts");

        // Adding child data
        List<RedditPost> sticky = new ArrayList<RedditPost>();
        List<RedditPost> normal = new ArrayList<RedditPost>();

        for(RedditPost post: postList){
            if(post.isStickyPost()){
                sticky.add(post);
            }
            else{
                normal.add(post);
            }
        }

        listDataChild.put(listDataHeader.get(0), sticky); // Header, Child data
        listDataChild.put(listDataHeader.get(1), normal);
    }
}
