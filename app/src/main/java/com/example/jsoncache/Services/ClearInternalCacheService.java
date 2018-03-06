package com.example.jsoncache.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;


public class ClearInternalCacheService extends Service {

    private static String FILE_NAME = "INTERNAL_USERS";

    @Override
    public void onCreate() {
        super.onCreate();
        long time = getSharedPreferences("cache", 0)
                .getLong("deleteInternalCache", 0) - Calendar.getInstance().getTimeInMillis();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                File internalCacheUsers = new File(getFilesDir(), FILE_NAME);
                internalCacheUsers.delete();
                if (internalCacheUsers.exists()) Toast.makeText(getApplicationContext(), "exists", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(), "deleted intern", Toast.LENGTH_SHORT).show();
                    stopSelf();
                }
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
