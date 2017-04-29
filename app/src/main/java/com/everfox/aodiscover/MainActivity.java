package com.everfox.aodiscover;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SearchHelper.OnGetSearchResultsListener {

    public static int MAX_ELEVATION_FACTOR = 8;
    String AOTRACKING_PACKAGE ="com.everfox.animetrackerandroid";
    String AOFORUMS_PACKAGE ="com.everfox.aozoraforums";
    MenuItem searchMenuItem;
    SearchView searchView;
    SearchHelper searchHelper;
    EditText searchEditText;
    CardFragmentPagerAdapter cardFragmentPagerAdapter;
    ShadowTransformer mFragmentCardShadowTransformer;

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
    @BindView(R.id.svSuggestions)
    ScrollView svSuggestions;
    @BindView(R.id.tv_suggestion_one)
    TextView tvSuggestionOne;
    @BindView(R.id.tv_suggestion_two)
    TextView tvSuggestionTwo;
    @BindView(R.id.tv_suggestion_three)
    TextView tvSuggestionThree;
    @BindView(R.id.tv_suggestion_four)
    TextView tvSuggestionFour;
    @BindView(R.id.tv_suggestion_five)
    TextView tvSuggestionFive;
    @BindView(R.id.tv_suggestion_six)
    TextView tvSuggestionSix;
    @BindView(R.id.tv_suggestion_seven)
    TextView tvSuggestionSeven;

    View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ButterKnife.bind(this);
        Bitmap bitmap = Utils.blur(this, BitmapFactory.decodeResource(getResources(),R.drawable.anime_scenery));
        ivBackground.setImageBitmap(bitmap);
        //tool_bar.setTitle("Enter your request here");
        setSupportActionBar(tool_bar);
        cardFragmentPagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),dpToPixels(2, this));
        mFragmentCardShadowTransformer = new ShadowTransformer(vpResults, cardFragmentPagerAdapter);
        searchHelper = new SearchHelper(this);
        vpResults.setAdapter(cardFragmentPagerAdapter);
        vpResults.setPageTransformer(false, mFragmentCardShadowTransformer);
        vpResults.setOffscreenPageLimit(3);
        mFragmentCardShadowTransformer.enableScaling(true);
        ivShare.setColorFilter(Color.WHITE);
        ivAozoraForums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClicked(AOFORUMS_PACKAGE);
            }
        });
        ivAozoraTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClicked(AOTRACKING_PACKAGE);
            }
        });
        searchEditText = (EditText) findViewById(R.id.etSearch);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setTextSize(18);
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEditText.setHint("Enter your request here");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TextChange", "Lenght" + s.length());
                if (s.length() > 0) {
                    Log.d("TextChange", s.toString());
                    Drawable drawable = getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel);
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                } else {
                    searchEditText.setCompoundDrawables(null, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (searchEditText.getText().equals("")) { return false; }
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (searchEditText.getCompoundDrawables()[DRAWABLE_RIGHT] == null) { return false; }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (searchEditText.getRight() - searchEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        searchEditText.setText("");
                        noResults();
                        return false;
                    }
                }
                return false;
            }
        });
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                performSearch(v.getText().toString());
                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvSelected = (TextView)v;
                searchEditText.setText(tvSelected.getText().toString());
                performSearch(tvSelected.getText().toString());
            }
        };

        tvSuggestionOne.setOnClickListener(listener);
        tvSuggestionTwo.setOnClickListener(listener);
        tvSuggestionThree.setOnClickListener(listener);
        tvSuggestionFour.setOnClickListener(listener);
        tvSuggestionFive.setOnClickListener(listener);
        tvSuggestionSix.setOnClickListener(listener);
        tvSuggestionSeven.setOnClickListener(listener);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /*getMenuInflater().inflate(R.menu.menu_search, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setTextSize(18);
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEditText.setHint("Enter your request here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    noResults();
                }
                return true;
            }
        });
*/
        return super.onCreateOptionsMenu(menu);
    }
    private void performSearch(String newText) {

        if(newText == ""){
            noResults();
        } else {
            if (newText.length() > 2) {
                cardFragmentPagerAdapter.clear();
                searchHelper.searchAnime(newText);
                pbLoading.setVisibility(View.VISIBLE);
                vpResults.setVisibility(View.GONE);
                svSuggestions.setVisibility(View.GONE);
            }
        }

    }

    private void noResults() {
        pbLoading.setVisibility(View.GONE);
        vpResults.setVisibility(View.GONE);
        svSuggestions.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetSearchResultsListener(List<Anime> results) {

        if(results == null || results.size() == 0) {
            pbLoading.setVisibility(View.GONE);
            vpResults.setVisibility(View.GONE);
            svSuggestions.setVisibility(View.VISIBLE);
        } else {
            pbLoading.setVisibility(View.GONE);
            vpResults.setVisibility(View.VISIBLE);
            svSuggestions.setVisibility(View.GONE);
            for (int i = 0; i < results.size(); i++) {
                cardFragmentPagerAdapter.addCardFragment(AnimeFragment.newInstance(results.get(i)));
                cardFragmentPagerAdapter.notifyDataSetChanged();
                vpResults.setCurrentItem(0);
            }
        }

    }
    public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private List<AnimeFragment> mFragments;
        private float mBaseElevation;

        public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
            super(fm);
            mFragments = new ArrayList<>();
            mBaseElevation = baseElevation;
        }

        public float getBaseElevation() {
            return mBaseElevation;
        }

        public CardView getCardViewAt(int position) {
            if(mFragments.size()> position)
                return mFragments.get(position).getCardView();
            else
                return null;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        public Object instantiateItem(ViewGroup container, int position) {
            Object fragment = super.instantiateItem(container, position);
            mFragments.set(position, (AnimeFragment) fragment);
            return fragment;
        }

        public void addCardFragment(AnimeFragment fragment) {
            mFragments.add(fragment);
        }

        public void clear() {
            mFragments.clear();
        }

    }

    public class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

        private ViewPager viewPager;
        private CardFragmentPagerAdapter cardAdapter;
        private float lastOffset;
        private boolean scalingEnabled;

        public ShadowTransformer(ViewPager viewPager, CardFragmentPagerAdapter adapter) {
            this.viewPager = viewPager;
            viewPager.addOnPageChangeListener(this);
            cardAdapter = adapter;
        }

        public void enableScaling(boolean enable) {
            if (scalingEnabled && !enable) {
                // shrink main card
                CardView currentCard = cardAdapter.getCardViewAt(viewPager.getCurrentItem());
                if (currentCard != null) {
                    currentCard.animate().scaleY(1);
                    currentCard.animate().scaleX(1);
                }
            }else if(!scalingEnabled && enable){
                // grow main card
                CardView currentCard = cardAdapter.getCardViewAt(viewPager.getCurrentItem());
                if (currentCard != null) {
                    //enlarge the current item
                    currentCard.animate().scaleY(1.1f);
                    currentCard.animate().scaleX(1.1f);
                }
            }
            scalingEnabled = enable;
        }

        @Override
        public void transformPage(View page, float position) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realCurrentPosition;
            int nextPosition;
            float baseElevation = cardAdapter.getBaseElevation();
            float realOffset;
            boolean goingLeft = lastOffset > positionOffset;

            // If we're going backwards, onPageScrolled receives the last position
            // instead of the current one
            if (goingLeft) {
                realCurrentPosition = position + 1;
                nextPosition = position;
                realOffset = 1 - positionOffset;
            } else {
                nextPosition = position + 1;
                realCurrentPosition = position;
                realOffset = positionOffset;
            }

            // Avoid crash on overscroll
            if (nextPosition > cardAdapter.getCount() - 1
                    || realCurrentPosition > cardAdapter.getCount() - 1) {
                return;
            }

            CardView currentCard = cardAdapter.getCardViewAt(realCurrentPosition);

            // This might be null if a fragment is being used
            // and the views weren't created yet
            if (currentCard != null) {
                if (scalingEnabled) {
                    currentCard.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                    currentCard.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
                }
                currentCard.setCardElevation((baseElevation + baseElevation
                        * (MAX_ELEVATION_FACTOR - 1) * (1 - realOffset)));
            }

            CardView nextCard = cardAdapter.getCardViewAt(nextPosition);

            // We might be scrolling fast enough so that the next (or previous) card
            // was already destroyed or a fragment might not have been created yet
            if (nextCard != null) {
                if (scalingEnabled) {
                    nextCard.setScaleX((float) (1 + 0.1 * (realOffset)));
                    nextCard.setScaleY((float) (1 + 0.1 * (realOffset)));
                }
                nextCard.setCardElevation((baseElevation + baseElevation
                        * (MAX_ELEVATION_FACTOR - 1) * (realOffset)));
            }

            lastOffset = positionOffset;
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private void appClicked(String packageName) {

        if(appInstalledOrNot(packageName)) {
            runApp(packageName);
        } else {
            goToMarket(packageName);
        }
    }

    private boolean appInstalledOrNot(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private void goToMarket(String packageName) {

        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this,"Please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private void runApp(String packageName) {

        Intent LaunchIntent = getPackageManager()
                .getLaunchIntentForPackage(packageName);
        startActivity(LaunchIntent);
    }

}
