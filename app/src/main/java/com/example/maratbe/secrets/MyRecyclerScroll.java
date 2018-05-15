package com.example.maratbe.secrets;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class MyRecyclerScroll extends RecyclerView.OnScrollListener implements Constants{

    private boolean loading = true;
    int previousTotal = 0, visibleItemCount, totalItemCount, lastVisibleItem, lastItem = 0, index = 0;
    private String listName;
    private LinearLayoutManager mLinearLayoutManager;

    public MyRecyclerScroll(LinearLayoutManager linearLayoutManager, String listName, int index) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.listName = listName;
        this.index = index;
    }
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (loading)
        {
            if (totalItemCount >= previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading) {
            if (lastVisibleItem >= Util.getListSize(listName, index) - 2 && Util.getListSize(listName, index) == totalItemCount) {
                if (lastVisibleItem != lastItem && lastVisibleItem != lastItem + 1) {
                    onLoadMore();
                    loading = true;
                    lastItem = lastVisibleItem;
                }
            }
        }
    }

    public abstract void onLoadMore();
}