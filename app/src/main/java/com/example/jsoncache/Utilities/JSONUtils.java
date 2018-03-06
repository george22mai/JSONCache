package com.example.jsoncache.Utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jsoncache.Services.ClearExternalCacheService;
import com.example.jsoncache.Services.ClearInternalCacheService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class JSONUtils {

    public static String FILE_NAME = "INTERNAL_USERS";
    public static String EXTERNAL_FILE_NAME = "EXTERNAL_USERS";
    public static long EXPIRING_TIME_INTERNAL_CACHE = 1000 * 60 * 10; //10 MINUTES
    public static long EXPIRING_TIME_EXTERNAL_CACHE = 1000 * 60 * 30; //30 MINUTES

    public static StackUser parseJson(String json){
        StackUser user = new StackUser();
        try{
            JSONObject object = new JSONObject(json);
            JSONObject medals = object.getJSONObject("badge_counts");

            user.setName(object.optString("display_name"));
            user.setProfilePicUrl(object.optString("profile_image"));
            user.setLocation(object.optString("location"));
            user.setBronze(medals.optInt("bronze"));
            user.setSilver(medals.optInt("silver"));
            user.setGold(medals.optInt("gold"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }

    public static StackUser getUserFromInternalCache(Context context, int position){
        StackUser user = new StackUser();
        File internalCacheUsers = new File(context.getFilesDir(), FILE_NAME);
        Log.d("EXISTS", internalCacheUsers + "");
        try{
            BufferedReader br = new BufferedReader(new FileReader(internalCacheUsers));
            try{
                try{
                    JSONArray array = new JSONArray(br.readLine());
                    user = JSONUtils.parseJson(array.getJSONObject(position).toString());
                }catch (JSONException g){
                    g.printStackTrace();
                }
            }catch (IOException f){
                f.printStackTrace();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return user;
    }

    public static void copyFromExternToInternCache(Context context){
        File internalCacheUsers = new File(context.getFilesDir(), FILE_NAME);
        File externalCacheUsers = new File(context.getExternalCacheDir(), EXTERNAL_FILE_NAME);

        try{
            BufferedReader br = new BufferedReader(new FileReader(externalCacheUsers));
            try{
                FileWriter fileWriter = new FileWriter(internalCacheUsers.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fileWriter);
                bw.write(br.readLine());
                bw.close();
            }catch (IOException f){
                f.printStackTrace();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        startClearInternService(context);
    }

    public static void startClearInternService(Context context){
        context.getSharedPreferences("cache", 0)
                .edit()
                .putLong("deleteInternalCache", Calendar.getInstance().getTimeInMillis() + EXPIRING_TIME_INTERNAL_CACHE)
                .commit();
        context.startService(new Intent(context, ClearInternalCacheService.class));
    }

    public static void startClearExternService(Context context){
        context.getSharedPreferences("cache", 0)
                .edit()
                .putLong("deleteExternalCache", Calendar.getInstance().getTimeInMillis() + EXPIRING_TIME_EXTERNAL_CACHE)
                .commit();
        context.startService(new Intent(context, ClearExternalCacheService.class));
    }

}
