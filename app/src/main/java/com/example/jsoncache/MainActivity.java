package com.example.jsoncache;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jsoncache.Utilities.DeviceInfo;
import com.example.jsoncache.Utilities.JSONUtils;
import com.example.jsoncache.Utilities.StackUser;
import com.example.jsoncache.Utilities.StackUserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;

import static com.example.jsoncache.Utilities.DeviceInfo.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {

    List<StackUser> mListUsers = new ArrayList<>();
    private static final int NUMBER_OF_ITEMS_DISPLAYED = 10;
    ListView mListView;
    StackUserAdapter mStackUserAdapter;

    @BindString(R.string.linkToObject) String mLinkToJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mListView = findViewById(R.id.listView);
        mStackUserAdapter = new StackUserAdapter(getApplicationContext(), R.layout.stack_user_view, mListUsers);
        mListView.setAdapter(mStackUserAdapter);

       fillWithData();
    }

    private void fillWithData(){
        if (new File(getFilesDir(), JSONUtils.FILE_NAME).exists()) {//check for if internal cache exists
            Log.d("DATA_STATE", "LOADED FROM INTERNAL CACHE");
            displayFromCache();
        } else if(new File(getExternalCacheDir(), JSONUtils.EXTERNAL_FILE_NAME).exists()
                && DeviceInfo.isExternalStorageAvailable()){        // check if external cache exists
            Log.d("DATA_STATE", "LOADED FROM EXTERNAL CACHE");
            JSONUtils.copyFromExternToInternCache(getApplicationContext());
            displayFromCache();
        }else if (isNetworkAvailable(getApplicationContext())){     // load from internet
            Log.d("DATA_STATE", "LOADED INTO INTERNAL AND EXTERNAL CACHE");
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String mLinkToJsonObject = getString(R.string.linkToObject);
            final File internalCacheUsers = new File(getFilesDir(), JSONUtils.FILE_NAME);
            final File externalCacheUsers = new File(getExternalCacheDir(), JSONUtils.EXTERNAL_FILE_NAME);

            JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mLinkToJsonObject, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        JSONArray users = response.getJSONArray("items");
                        try{
                            Log.d("CREATE", internalCacheUsers + " ");

                            FileWriter fileWriter = new FileWriter(internalCacheUsers.getAbsoluteFile(), true);
                            BufferedWriter bw = new BufferedWriter(fileWriter);
                            bw.write(users.toString());
                            bw.close();

                            fileWriter = new FileWriter(externalCacheUsers.getAbsoluteFile(), true);
                            bw = new BufferedWriter(fileWriter);
                            bw.write(users.toString());
                            bw.close();

                            JSONUtils.startClearInternService(getApplicationContext());
                            JSONUtils.startClearExternService(getApplicationContext());

                            for (int i = 0; i< NUMBER_OF_ITEMS_DISPLAYED; i++){
                                mListUsers.add(JSONUtils.parseJson(users.getJSONObject(i).toString()));
                                mStackUserAdapter.notifyDataSetChanged();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(JsonObjectRequest);
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    void displayFromCache(){
        for (int i = 0; i < NUMBER_OF_ITEMS_DISPLAYED; i++) {
            mListUsers.add(JSONUtils.getUserFromInternalCache(getApplicationContext(), i));
            mStackUserAdapter.notifyDataSetChanged();
        }
    }
}
