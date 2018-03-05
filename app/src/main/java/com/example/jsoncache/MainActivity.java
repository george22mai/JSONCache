package com.example.jsoncache;

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
import com.example.jsoncache.Utilities.JSONUtils;
import com.example.jsoncache.Utilities.StackUser;
import com.example.jsoncache.Utilities.StackUserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;

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
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mStackUserAdapter = new StackUserAdapter(getApplicationContext(), R.layout.stack_user_view, mListUsers);
        mListView.setAdapter(mStackUserAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mLinkToJsonObject, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray users = response.getJSONArray("items");
                    for (int i = 0; i< NUMBER_OF_ITEMS_DISPLAYED; i++){
                        mListUsers.add(JSONUtils.parseJson(users.getJSONObject(i).toString()));
                        mStackUserAdapter.notifyDataSetChanged();
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
    }
}
