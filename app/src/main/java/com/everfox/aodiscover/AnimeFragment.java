package com.everfox.aodiscover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daniel.soto on 4/24/2017.
 */

public class AnimeFragment extends Fragment {

    Anime anime;
    private CardView mCardView;

    @BindView(R.id.ivAnime)
    ImageView ivAnime;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvGenres)
    TextView tvGenres;
    @BindView(R.id.tvRating)
    TextView tvRating;
    @BindView(R.id.tvUsers)
    TextView tvUsers;
    @BindView(R.id.tvAnimeDesc)
    TextView tvAnimeDesc;
    @BindView(R.id.tvAnimeBasicInfo)
    TextView tvAnimeBasicInfo;


    public static AnimeFragment newInstance(Anime animeToLoad) {
        AnimeFragment fragment = new AnimeFragment();
        fragment.anime = animeToLoad;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_viewpager, container, false);
        mCardView = (CardView) view.findViewById(R.id.cardView);
        mCardView.setMaxCardElevation(mCardView.getCardElevation()
                * MainActivity.MAX_ELEVATION_FACTOR);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(anime == null) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return;
        }

        Glide.with(this).load(anime.getString("fanart")).centerCrop().into(ivAnime);
        tvTitle.setText(anime.getString("title"));
        JSONArray genres = anime.getJSONArray("genres");
        String genresString = "";
        try {
            if(genres != null) {
                for (int i = 0; i < genres.length(); i++) {
                    genresString += genres.getString(i);
                    if (i != genres.length() - 1)
                        genresString += ", ";
                }
            }
        }
        catch(JSONException jEx){
            jEx.printStackTrace();
        }
        tvGenres.setText(genresString);
        tvRating.setTypeface(AoDiscoverApp.getAwesomeTypeface());
        tvUsers.setTypeface(AoDiscoverApp.getAwesomeTypeface());
        tvRating.setText(getString(R.string.fa_rating)+ " " + String.valueOf(anime.getNumber("membersScore")));
        tvUsers.setText(getString(R.string.fa_users)+ " " + String.valueOf(anime.getNumber("membersCount")));
        tvAnimeDesc.setText(Html.fromHtml(anime.getParseObject("details").getString("synopsis")));
        if(anime.getString("status").equals(AnimeStatus.NOTYETAIRED.toString())) {
            tvAnimeBasicInfo.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.notyet_airing));
        }
        if(anime.getString("status").equals(AnimeStatus.CURRENTLYAIRING.toString())) {
            tvAnimeBasicInfo.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.currently_airing));
        }
        if(anime.getString("status").equals(AnimeStatus.FINISHEDAIRING.toString())) {
            tvAnimeBasicInfo.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.finished_airing));
        }
        tvAnimeBasicInfo.setText(Anime.informationString(anime));
    }

    public CardView getCardView() {
        return mCardView;
    }
}
