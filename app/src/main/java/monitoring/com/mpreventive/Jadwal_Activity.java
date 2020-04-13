package monitoring.com.mpreventive;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import monitoring.com.mpreventive.adapter.Adapter_Jadwal;
import monitoring.com.mpreventive.app.AppController;
import monitoring.com.mpreventive.data.Data_Jadwal;

public class Jadwal_Activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener {

    android.support.v7.app.ActionBar actionBar;

    SwipeRefreshLayout swipe;
    List<Data_Jadwal> items = new ArrayList<Data_Jadwal>();
    Adapter_Jadwal adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    ProgressDialog pDialog;

    private static final String TAG = Jadwal_Activity.class.getSimpleName();

    private static String url_select = Server.URL_getJadwal;

    public static final String TAG_ID = "id";
    public static final String TAG_TITLE = "title";
    public static final String TAG_DESK = "description";
    public static final String TAG_START = "start";
    public static final String TAG_END = "end";

    private static final String TAG_SUCCESS = "success";

    public static final String TAG_RESULTS = "results";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "value";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jadwal);

        actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Jadwal Tugas");
        String title = actionBar.getTitle().toString(); // get the title
        // actionBar.hide(); // or even hide the actionbar

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_Jad);
        ListView listView = (ListView) findViewById(R.id.list_v_jadwal);


        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new Adapter_Jadwal(Jadwal_Activity.this, items);
        listView.setAdapter(adapter);

        // menampilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           items.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, final long id) {
                // TODO Auto-generated method stub
                final String idx = items.get(position).getId();
                final String namex = items.get(position).getTitle();

                final CharSequence[] dialogitem = {"Info"};
                dialog = new AlertDialog.Builder(Jadwal_Activity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:

                                Intent intent = new Intent(Jadwal_Activity.this, Jadwal__Fragment.class);
                                intent.putExtra(TAG_ID, idx);
                                intent.putExtra(TAG_TITLE, namex);
                                startActivity(intent);
                                break;
//                            case 1:
//                                detail(idx);
//                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_spinner, menu);
//
//        MenuItem item = menu.findItem(R.id.spinner);
//        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bulan, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapter);
//        return true;
//    }

    private void callVolley() {
        items.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data_Jadwal item = new Data_Jadwal();

                        item.setId(obj.getString(TAG_ID));
                        item.setTitle(obj.getString(TAG_TITLE));
                        item.setDescription(obj.getString(TAG_DESK));
                        item.setStart(obj.getString(TAG_START));
                        item.setEnd(obj.getString(TAG_END));

                        // menambah item ke array
                        items.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(Jadwal_Activity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }
    @Override
    public void onRefresh() {
        items.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }
    @Override
    public boolean onQueryTextSubmit(String query){
        cariData(query);
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cari, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.type_name));
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }
    private void cariData(final String keywo){
        pDialog = new ProgressDialog(Jadwal_Activity.this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest str_req = new StringRequest(Request.Method.POST, Server.URL_cariJadwal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());
                try {
                        JSONObject jObj = new JSONObject(response);
                        int value = jObj.getInt(TAG_VALUE);
                        if (value == 1){
                            items.clear();
                            adapter.notifyDataSetChanged();

                            String getObject = jObj.getString(TAG_RESULTS);
                            JSONArray jArr = new JSONArray(getObject);

                            for (int i=0; i<jArr.length(); i++){
                                JSONObject job = jArr.getJSONObject(i);

                                Data_Jadwal item = new Data_Jadwal();

                                item.setId(job.getString(TAG_ID));
                                item.setTitle(job.getString(TAG_TITLE));
                                item.setDescription(job.getString(TAG_DESK));
                                item.setStart(job.getString(TAG_START));
                                item.setEnd(job.getString(TAG_END));

                                // menambah item ke array
                                items.add(item);
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("key", keywo);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(str_req, tag_json_obj);
    }
}
