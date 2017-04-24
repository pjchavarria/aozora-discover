package com.everfox.aodiscover;

import android.app.Activity;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String,String> parameters = new HashMap<>();
        parameters.put("query",newText);
        ParseCloud.callFunctionInBackground("QueryWitAi", parameters, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if(e==null) {
                    mOnGetSearchResultsCallback.onGetSearchResultsListener((List<Anime>)object);
                }
            }
        });
    }
}
