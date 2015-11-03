package com.henriquemelissopoulos.dribbbletest.view.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.henriquemelissopoulos.dribbbletest.controller.Config;

/**
 * Created by h on 02/11/15.
 */
public abstract class RecyclerViewThresholdListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loading = true;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private LinearLayoutManager linearLayoutManager;

    public abstract void onVisibleThreshold();

    public RecyclerViewThresholdListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = linearLayoutManager.getItemCount();
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + Config.SHOTS_LIST_THREASHOLD)) {
            onVisibleThreshold();
            loading = true;
        }
    }

    public void reset() {
        previousTotal = 0;
    }
}