package com.hasbrain.areyouandroiddev.datastore;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.hasbrain.areyouandroiddev.RetryConnectionActivity;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/22/15.
 */
public class NetworkBasedFeedDataStore implements FeedDataStore {
    private String baseUrl, afterPost, url;
    private Context context;
    private List<RedditPost> posts, newPosts;
    public NetworkBasedFeedDataStore(Context context)    {
        this.context = context;
    }
    public void getPostList(String topic, String before, String after,
                            OnRedditPostsRetrievedListener onRedditPostsRetrievedListener) {
        //TODO: Implement network calls.
    }

    @Override
    public void getPostList(final OnRedditPostsRetrievedListener onRedditPostsRetrievedListener) {
        final RequestQueue queue = Volley.newRequestQueue(context);
//            baseUrl = "https://www.reddit.com/r/androiddev/.json?limit=100";
//            baseUrl ="https://www.reddit.com/r/dota2/.json?limit=100";

        Log.i("GET URL", url);
        // Request a string response from the provided URL.
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

//                        Log.i("RESPONSE", "Response is: "+ response);
                        if(newPosts != null) {
                            newPosts.clear();
                        }
                        Type type = new TypeToken<List<RedditPost>>(){}.getType();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
                        Gson gson = gsonBuilder.create();

                        JsonParser jsonParser = new JsonParser();

                        JsonObject jsonObject = (JsonObject)jsonParser.parse(response.toString());
//                      System.out.println(jsonObject.toString());

                        newPosts = gson.fromJson(jsonObject.get("data").getAsJsonObject().get("children").getAsJsonArray(), type);
                        updateListPosts(newPosts);
                        onRedditPostsRetrievedListener.onRedditPostsRetrieved(posts, null);
//                        Log.i("LIST SIZE", "" + posts.size());
                        afterPost = jsonObject.get("data").getAsJsonObject().get("after").getAsString();
//                        Log.i("AFTER", afterPost);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY RESPONSE FAIL", "Volley gets fail");
                Intent retryIntent = new Intent(context, RetryConnectionActivity.class);
                retryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(retryIntent);
            }
        });
// Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }

    public void updateListPosts(List<RedditPost> newPosts){
        if(posts == null){
            posts = new ArrayList<RedditPost>();
        }
            posts.addAll(newPosts);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setUrlAfterPost(){
        url = baseUrl + ".json?count=25&after=" + afterPost;
    }

    public void setUrlGetNew(){
        url = baseUrl + ".json?limit=25";
    }
}