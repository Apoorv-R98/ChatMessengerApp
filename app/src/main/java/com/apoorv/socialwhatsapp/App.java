package com.apoorv.socialwhatsapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("iockbifusVsVEW0OBGjGLpelTByKR55sd336AXRJ")
                .clientKey("8vpdQuFSm5BoHMA08MmYsF78XPz9JWplzGIgroDj")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
