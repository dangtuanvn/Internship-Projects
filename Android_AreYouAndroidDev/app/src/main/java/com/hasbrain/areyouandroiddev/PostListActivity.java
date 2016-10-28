package com.hasbrain.areyouandroiddev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.hasbrain.areyouandroiddev.datastore.FeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.FileBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.NetworkBasedFeedDataStore;
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
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends AppCompatActivity implements RecyclerExpandableAdapter.OnItemClick, SwipeRefreshLayout.OnRefreshListener {

    public static final String DATA_JSON_FILE_NAME = "data.json";
    private NetworkBasedFeedDataStore feedDataStore;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recycleListView;
    private int position;
    private boolean loading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.parseColor("#2196F3"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#FF9800"),
                Color.parseColor("#F44336"));

//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
//        Gson gson = gsonBuilder.create();
//        InputStream is = null;
        try {
//            is = getAssets().open(DATA_JSON_FILE_NAME);
//            feedDataStore = new FileBasedFeedDataStore(gson, is);

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                feedDataStore = new NetworkBasedFeedDataStore(this);    // fetch data
                feedDataStore.setBaseUrl("https://www.reddit.com/r/androiddev/");
                feedDataStore.setUrlGetNew();
                feedDataStore.getPostList(new FeedDataStore.OnRedditPostsRetrievedListener() {
                    @Override
                    public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
                        // Toast.makeText(getApplicationContext(),"Size: " + postList.size(),Toast.LENGTH_SHORT).show(); // size 26
                        // displayRecyclerExpandableList(postList);
                        displayRecyclerList(postList);
                        position = postList.size() - 2;
                    }
                });
            } else {
                Intent retryIntent = new Intent(this, RetryConnectionActivity.class);
                retryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(retryIntent);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Intent retryIntent = new Intent(this, RetryConnectionActivity.class);
            retryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(retryIntent);

//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
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
        recycleListView = (RecyclerView) findViewById(R.id.recyclerview);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerListAdapter adapter = new RecyclerListAdapter(this, postList);
            recycleListView.setAdapter(adapter);

            final LinearLayoutManager linearLayout = new LinearLayoutManager(this);

            recycleListView.setLayoutManager(linearLayout);
            recycleListView.clearOnScrollListeners();
//            recycleListView.setVerticalScrollBarEnabled(true);
            recycleListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView view, int dx, int dy) {
                    boolean atTop = true;
                    boolean atBottom = false;
                    if(recycleListView != null && recycleListView.getChildCount() > 0){
                        // check if the first item of the list is visible
                        atTop = linearLayout.findFirstCompletelyVisibleItemPosition() == 0;
                        // check if the top of the first item is visible
                        // enabling or disabling the refresh layout
                        atBottom = linearLayout.findLastCompletelyVisibleItemPosition() == postList.size();
//                        Log.i("LAST ITEM", "" + linearLayout.findLastCompletelyVisibleItemPosition());
                    }
                    swipeLayout.setEnabled(atTop);
                    if(atBottom){
                       loadMorePosts();
                    }
                }});
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(true);
                feedDataStore = new NetworkBasedFeedDataStore(getApplicationContext());    // fetch data
                feedDataStore.setBaseUrl("https://www.reddit.com/r/androiddev/");
                feedDataStore.setUrlGetNew();
                feedDataStore.getPostList(new FeedDataStore.OnRedditPostsRetrievedListener() {
                    @Override
                    public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
                        // Toast.makeText(getApplicationContext(),"Size: " + postList.size(),Toast.LENGTH_SHORT).show(); // size 26
                        displayRecyclerList(postList);
                        recycleListView.invalidate();
                        swipeLayout.setRefreshing(false);
                    }
                });
            }
        }, 10000);
    }

    public void loadMorePosts(){
        if(!loading) {
            loading = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                swipeLayout.setRefreshing(true);
                    feedDataStore.setUrlAfterPost();
                    feedDataStore.getPostList(new FeedDataStore.OnRedditPostsRetrievedListener() {
                        @Override
                        public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
                            displayRecyclerList(postList);
                            recycleListView.invalidate();
//                        swipeLayout.setRefreshing(false);
//                        Toast.makeText(getApplicationContext(),"Size: " + postList.size(),Toast.LENGTH_SHORT).show(); // size 26
                            recycleListView.scrollToPosition(position);
                            position = postList.size() - 2;
                            loading = false;
                            Toast.makeText(getApplicationContext(),"Load complete",Toast.LENGTH_SHORT).show(); // size 26
                        }
                    });
                }
            }, 10000);
        }
    }

    protected void displayRecyclerExpandableList(final List<RedditPost> postList){
        recycleListView = (RecyclerView) findViewById(R.id.recyclerview);

        List<RedditGroupList> groupList = new ArrayList<>();
        groupList.add(new RedditGroupList("Sticky posts"));
        groupList.add(new RedditGroupList("Normal posts"));
        groupList.add(new RedditGroupList("Footer"));
//        groupList.get(1).setIsBeta();

//        int i = 0;
        for(RedditPost post : postList){
//            if(i % 2 == 0){
//                post.setIsBeta();
//            }
            if(post.isStickyPost()){
                groupList.get(0).addChildObjectList(post);
            }
            else{
                groupList.get(1).addChildObjectList(post);
            }
//            i++;
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
}