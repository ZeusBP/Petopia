package com.android.petopia;

import android.app.Application;

import com.android.petopia.data_local.DataLocalManager;
import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());

        Map config = new HashMap();
        config.put("cloud_name", "dttyqjqvo");
        config.put("api_key", "577745356291642");
        config.put("api_secret", "_7N157pN9_MeainXG6EA8_-Ftb4");
        MediaManager.init(getApplicationContext(), config);
    }
}
