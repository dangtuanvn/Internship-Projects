package com.hasbrain.areyouandroiddev.model;


import com.bignerdranch.expandablerecyclerview.model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class RedditGroupList implements ParentListItem {
    private List<RedditPost> mChildrenList;
    private String title;
    int type = 0;

    public RedditGroupList(String title) {
        this.title = title;
        mChildrenList = new ArrayList<RedditPost>();
    }
    @Override
    public List<RedditPost> getChildItemList() {
        return mChildrenList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void addChildObjectList(RedditPost post) {
        mChildrenList.add(post);
    }

    public void setIsBeta(){
        type = 1;
    }

    public boolean isBeta(){
        return type == 1;
    }
}
