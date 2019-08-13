package com.dreamscape.test.dreamscape.views;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dreamscape.test.dreamscape.R;
import com.dreamscape.test.dreamscape.adapter.ItemsAdapter;
import com.dreamscape.test.dreamscape.model.Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RequestQueue queue;
    String URL = "https://www.flickr.com/services/feeds/photos_public.gne?format=json";
    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Items> itemsList;
    private RecyclerView.Adapter adapter;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = findViewById(R.id.item_list);

        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        itemsList = new ArrayList<>();
        adapter = new ItemsAdapter(MainActivity.this,itemsList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                String res = response.substring(15, response.length()-1);
                try {
                    JSONObject json = new JSONObject(res);
                    JSONArray jsonArray = json.getJSONArray("items");
                    String title = "a";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Items item = new Items();
                            item.setTitle(jsonObject.getString("title"));
                            item.setDescription(jsonObject.getString("description"));
                            item.setPublished((jsonObject.getString("published")));
                            item.setTags(jsonObject.getString("tags"));
                            item.setAuthor(jsonObject.getString("author"));
                            item.setDate_taken(jsonObject.getString("date_taken"));

                            JSONObject media = jsonObject.getJSONObject("media");
                            item.setMedia(media.getString("m"));


                            itemsList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });
        queue.add(request);
    }
}
