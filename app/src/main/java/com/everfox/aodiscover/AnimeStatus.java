package com.everfox.aodiscover;

/**
 * Created by daniel.soto on 4/24/2017.
 */

public enum AnimeStatus {

    NOTYETAIRED("not yet aired","Not Aired"),
    CURRENTLYAIRING("currently airing","Airing"),
    FINISHEDAIRING("finished airing","Finished"),
    CANCELLED("cancelled","Cancelled");

    private final String text;
    private final String shortText;
    private AnimeStatus(final String text, String shortText) {
        this.text = text;
        this.shortText = shortText;
    }


    @Override
    public String toString() {
        return text;
    }
    public String toShortString() {
        return shortText;
    }

}
