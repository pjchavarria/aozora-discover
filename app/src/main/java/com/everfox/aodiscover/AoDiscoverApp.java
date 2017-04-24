package com.everfox.aodiscover;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by daniel.soto on 4/24/2017.
 */

public class AoDiscoverApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Anime.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("AneKeKPLygTGmVmqWsY6totXXTQfk8")
                .clientKey("vvsbzUBBgnPKCoYQlltREy5S0gSIgMfBp34aDrkc")
                .server("http://www.aozoraapp.com/parse/")
                .enableLocalDataStore()
                .build());
    }
}
