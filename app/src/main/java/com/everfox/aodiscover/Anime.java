package com.everfox.aodiscover;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by daniel.soto on 4/24/2017.
 */

@ParseClassName("Anime")
public class Anime extends ParseObject {


    public static String informationString(Anime anime) {
        String animeInfo = "";
        animeInfo = anime.getString("type");
        animeInfo += " - ";
        animeInfo += convertClassificationToShortText(anime.getParseObject("details").getString("classification"));
        animeInfo += " - ";
        animeInfo += String.valueOf(anime.getInt("episodes"));
        animeInfo += " eps - ";
        animeInfo += String.valueOf(anime.getInt("duration"));
        animeInfo += " min - ";
        animeInfo += String.valueOf(anime.getInt("year"));
        return animeInfo;
    }


    public static String convertClassificationToShortText(String classification) {

        if(classification == null)
            return AnimeClassification.NONE.toShortString();
        if(classification.equals(AnimeClassification.G.toString()))
            return AnimeClassification.G.toShortString();
        if(classification.equals(AnimeClassification.PG13.toString()))
            return AnimeClassification.PG13.toShortString();
        if(classification.equals(AnimeClassification.NONE.toString()))
            return AnimeClassification.NONE.toShortString();
        if(classification.equals(AnimeClassification.R.toString()))
            return AnimeClassification.R.toShortString();
        if(classification.equals(AnimeClassification.RPLUS.toString()))
            return AnimeClassification.RPLUS.toShortString();
        if(classification.equals(AnimeClassification.RX.toString()))
            return AnimeClassification.RX.toShortString();
        return AnimeClassification.NONE.toShortString();
    }


}
