package com.everfox.aodiscover;

import android.app.Activity;

import java.util.List;

/**
 * Created by daniel.soto on 4/24/2017.
 */

public class SearchHelper {


    private OnGetSearchResultsListener mOnGetSearchResultsCallback;
    public interface OnGetSearchResultsListener {
        public void onGetSearchResultsListener(List<Anime> results);
    }


    public SearchHelper (Activity listener) {
        mOnGetSearchResultsCallback = (OnGetSearchResultsListener)listener;
    }
    public void searchAnime(String newText) {
    }
}
