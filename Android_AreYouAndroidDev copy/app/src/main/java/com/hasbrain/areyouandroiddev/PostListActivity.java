package com.hasbrain.areyouandroiddev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.hasbrain.areyouandroiddev.datastore.FeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.FileBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.model.RecyclerExpandableAdapter;
import com.hasbrain.areyouandroiddev.model.RecyclerGridAdapter;
import com.hasbrain.areyouandroiddev.model.RecyclerListAdapter;
import com.hasbrain.areyouandroiddev.model.RedditGroupList;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends AppCompatActivity implements RecyclerExpandableAdapter.OnItemClick {

    public static final String DATA_JSON_FILE_NAME = "data.json";
    private FeedDataStore feedDataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
        Gson gson = gsonBuilder.create();
        InputStream is = null;
        try {
            is = getAssets().open(DATA_JSON_FILE_NAME);
            feedDataStore = new FileBasedFeedDataStore(gson, is);
            feedDataStore.getPostList(new FeedDataStore.OnRedditPostsRetrievedListener() {
                @Override
                public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
                    // Toast.makeText(getApplicationContext(),"Size: " + postList.size(),Toast.LENGTH_SHORT).show(); // size 26
                    displayRecyclerExpandableList(postList);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void displayPostList(final List<RedditPost> postList) {
        final Context context = this;
        RedditPostAdapter adapterList = new RedditPostAdapter(this, R.layout.list_view_item, postList, 0);
        RedditPostAdapter adapterGrid = new RedditPostAdapter(this, R.layout.list_view_item, postList, 1);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // LIST VIEW
            final ListView listview = (ListView) findViewById(R.id.listview);
            View footer = getLayoutInflater().inflate(R.layout.footer, null);
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostViewActivity.class);
                    intent.putExtra("url", "https://www.reddit.com/r/androiddev/");
                    startActivity(intent);
                }
            });
            listview.addFooterView(footer);

            listview.setAdapter(adapterList);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, PostViewActivity.class);
                    intent.putExtra("url", postList.get(position).getUrl());
                    startActivity(intent);
                }
            });
        }

        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // GRID VIEW
            final GridView gridView = (GridView) findViewById(R.id.gridview);
            gridView.setAdapter(adapterGrid);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, PostViewActivity.class);
                    intent.putExtra("url", postList.get(position).getUrl());
                    startActivity(intent);
                }
            });
        }
    }

    protected int getLayoutResource() {
        return R.layout.activity_post_list;
    }

    protected void displayRecyclerList(final List<RedditPost> postList){
        RecyclerView recycleListView = (RecyclerView) findViewById(R.id.recyclerview);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerListAdapter adapter = new RecyclerListAdapter(this, postList);
            recycleListView.setAdapter(adapter);
            recycleListView.setLayoutManager(new LinearLayoutManager(this));
        }

        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RecyclerGridAdapter adapter = new RecyclerGridAdapter(this, postList);
            recycleListView.setAdapter(adapter);
            recycleListView.setLayoutManager(new GridLayoutManager(this, 3));
            recycleListView.addItemDecoration(new ItemOffsetDecoration(30));
        }
    }

    public void onItemClick(View view, Object data, int position) {
        if (data instanceof RedditPost) {
            displayWebView(((RedditPost)data).getUrl());
        }
        else{
            displayWebView((String)data);
        }
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int offset;

        public ItemOffsetDecoration(int offset) {
            this.offset = offset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = offset;
            outRect.right = offset;
            outRect.bottom = offset;
            outRect.top = offset;
        }
    }

    protected void displayRecyclerExpandableList(final List<RedditPost> postList){
        RecyclerView recycleListView = (RecyclerView) findViewById(R.id.recyclerview);

        List<RedditGroupList> groupList = new ArrayList<>();
        groupList.add(new RedditGroupList("Sticky posts"));
        groupList.add(new RedditGroupList("Normal posts"));
        groupList.add(new RedditGroupList("Footer"));
        groupList.get(1).setIsBeta();

        int i = 0;
        for(RedditPost post : postList){
            if(i % 2 == 0){
                post.setIsBeta();
            }
            if(post.isStickyPost()){
                groupList.get(0).addChildObjectList(post);
            }
            else{
                groupList.get(1).addChildObjectList(post);
            }
            i++;
        }

        RecyclerExpandableAdapter recyclerExpandableView = new RecyclerExpandableAdapter(this, groupList);
        recyclerExpandableView.setOnItemClick(this);
        recycleListView.setAdapter(recyclerExpandableView);
        recycleListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void displayWebView(String url){
        Intent intent = new Intent(this, PostViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}