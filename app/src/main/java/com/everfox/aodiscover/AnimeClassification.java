package com.everfox.aodiscover;

/**
 * Created by daniel.soto on 4/24/2017.
 */

public enum AnimeClassification {

    R("R - 17+ (violence & profanity)","R"),
    G("G - All Ages","G"),
    RX("Rx - Hentai","Rx"),
    PG("PG - Children","PG"),
    PG13("PG-13 - Teens 13 or older","PG13"),
    RPLUS("R+ - Mild Nudity","R+"),
    NONE("None","None");

    private final String textLong;
    private final String textShort;
    private AnimeClassification(final String textLong,final String textShort) {
        this.textLong = textLong;
        this.textShort = textShort;
    }


    @Override
    public String toString() {
        return textLong;
    }

    public String toShortString() { return textShort;}
}
