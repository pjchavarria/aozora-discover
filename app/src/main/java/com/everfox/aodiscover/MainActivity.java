package com.everfox.aodiscover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SearchHelper.OnGetSearchResultsListener {

    MenuItem searchMenuItem;
    SearchView searchView;
    SearchHelper searchHelper;
    EditText searchEditText;

    @BindView(R.id.ivBackground)
    ImageView ivBackground;
    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.vpResults)
    ViewPager vpResults;
    @BindView(R.id.llSuggestions)
    LinearLayout llSuggestions;
    @BindView(R.id.ivAozoraForums)
    ImageView ivAozoraForums;
    @BindView(R.id.ivAozoraTracker)
    ImageView ivAozoraTracker;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bitmap bitmap = BlurUtils.blur(this, BitmapFactory.decodeResource(getResources(),R.drawable.anime_scenery));
        ivBackground.setImageBitmap(bitmap);
        tool_bar.setTitle("Enter your request here");
        setSupportActionBar(tool_bar);
        searchHelper = new SearchHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHint("Enter your request here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private void performSearch(String newText) {

        if(newText == ""){
            noResults();
        } else {
            if (newText.length() > 2) {
                searchHelper.searchAnime(newText);
                pbLoading.setVisibility(View.VISIBLE);
                vpResults.setVisibility(View.GONE);
                llSuggestions.setVisibility(View.GONE);
            }
        }

    }

    private void noResults() {
        pbLoading.setVisibility(View.GONE);
        vpResults.setVisibility(View.GONE);
        llSuggestions.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetSearchResultsListener(List<Anime> results) {

    }
}
