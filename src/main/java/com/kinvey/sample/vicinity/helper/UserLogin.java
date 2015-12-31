package com.kinvey.sample.vicinity.helper;

import com.kinvey.android.Client;

import android.app.Application;

public class UserLogin extends Application {
    private Client service;

    // Application Constants
    public static final String AUTHTOKEN_TYPE = "authtokenType";
    public static final String ACCOUNT_TYPE = "kinvey";
    public static final String LOGIN_TYPE_KEY = "kid_WJOa5gfLDe";
    
    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }
   

    private void initialize() {
		// Enter your app credentials here
		service = new Client.Builder(this).build();
    }

    public Client getKinveyService() {
        return service;
    }
}
