package com.example.jsoncache.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class ClearExternalCacheService extends Service {

    private static String EXTERNAL_FILE_NAME = "EXTERNAL_USERS";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("CACHE", "STARTED EXTERNAL CLEANER");
        long time = getSharedPreferences("cache", 0)
                .getLong("deleteExternalCache", 0) - Calendar.getInstance().getTimeInMillis();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                File externalCacheUsers = new File(getExternalCacheDir(), EXTERNAL_FILE_NAME);
                externalCacheUsers.delete();
                if (!externalCacheUsers.exists())
                    Toast.makeText(getApplicationContext(), "deleted extern", Toast.LENGTH_SHORT).show();
                stopSelf();
                }
            }, time);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
