package monitoring.com.mpreventive;

import android.app.ActionBar;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import monitoring.com.mpreventive.adapter.Adapter;
import monitoring.com.mpreventive.app.AppController;
import monitoring.com.mpreventive.data.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mesin_Activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    android.support.v7.app.ActionBar actionBar;
    SwipeRefreshLayout swipe;
    List<Data> items = new ArrayList<Data>();
    Adapter adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id, txt_no_mesin, txt_nama_mesin, txt_area;
//    String id, no_mesin, nama_mesin, area;

    private static final String TAG = Mesin_Activity.class.getSimpleName();

    private static String url_select = Server.URL_getMesin;

    public static final String TAG_ID = "ID";
    public static final String TAG_NAMA_MESIN = "Nama_Mesin";
    public static final String TAG_NOMOR_MESIN = "No_Mesin";
    public static final String TAG_AREA = "Area";
    public static final String TAG_GAMBAR = "Gambar";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mesin);

        actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Data Mesin");
        String title = actionBar.getTitle().toString(); // get the title
       // actionBar.hide(); // or even hide the actionbar

        // menghubungkan variablel pada layout dan pada java
//        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        ListView listView = (ListView) findViewById(R.id.list_v_mesin);


        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new Adapter(Mesin_Activity.this, items);
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

        // fungsi floating action button memanggil form biodata
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogForm("", "", "", "SIMPAN");
//            }
//        });

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, final long id) {
                // TODO Auto-generated method stub
                final String idx = items.get(position).getId();
                final String namex = items.get(position).getNama_mesin();

                final CharSequence[] dialogitem = {"Info"};
                dialog = new AlertDialog.Builder(Mesin_Activity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:

                                Intent intent = new Intent(Mesin_Activity.this, Mesin__Fragment.class);
                                intent.putExtra(TAG_ID,idx);
                                intent.putExtra(TAG_NAMA_MESIN, namex);
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

    @Override
    public void onRefresh() {
        items.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    // untuk mengosongi edittext pada form
    private void kosong() {
        txt_id.setText(null);
        txt_no_mesin.setText(null);
        txt_nama_mesin.setText(null);
        txt_area.setText(null);
    }

    // untuk menampilkan dialog from biodata
//    private void DialogForm(String idx, String no_mesinx, String nama_mesinx, String areax, String button) {
//        dialog = new AlertDialog.Builder(Mesin_Activity.this);
//        inflater = getLayoutInflater();
//        dialogView = inflater.inflate(R.layout.form_mesin, null);
//        dialog.setView(dialogView);
//        dialog.setCancelable(true);
//        dialog.setIcon(R.mipmap.ic_launcher);
//        dialog.setTitle("Form Biodata");
//
//        txt_id = (EditText) dialogView.findViewById(R.id.txt_id);
//        txt_no_mesin = (EditText) dialogView.findViewById(R.id.txt_no_mesin);
//        txt_nama_mesin = (EditText) dialogView.findViewById(R.id.txt_nama_mesin);
//        txt_area = (EditText) dialogView.findViewById(R.id.txt_area);
//
//        if (!idx.isEmpty()) {
//            txt_id.setText(idx);
//            txt_no_mesin.setText(no_mesinx);
//            txt_nama_mesin.setText(nama_mesinx);
//            txt_area.setText(areax);
//        } else {
//            kosong();
//        }
//
//        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                id = txt_id.getText().toString();
//                no_mesin = txt_no_mesin.getText().toString();
//                nama_mesin = txt_nama_mesin.getText().toString();
//                area = txt_area.getText().toString();
//
//                simpan_update();
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                kosong();
//            }
//        });
//
//        dialog.show();
//    }

    // untuk menampilkan semua data pada listview
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

                        Data item = new Data();

                        item.setId(obj.getString(TAG_ID));
                        item.setNo_mesin(obj.getString(TAG_NOMOR_MESIN));
                        item.setNama_mesin(obj.getString(TAG_NAMA_MESIN));
                        item.setArea(obj.getString(TAG_AREA));
                        item.setGambaran(obj.getString(TAG_GAMBAR));

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
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // fungsi untuk menyimpan atau update
//    private void simpan_update() {
//        String url;
//        // jika id kosong maka simpan, jika id ada nilainya maka update
//        if (id.isEmpty()) {
//            url = url_insert;
//        } else {
//            url = url_update;
//        }
//
//        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Response: " + response.toString());
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    success = jObj.getInt(TAG_SUCCESS);
//
//                    // Cek error node pada json
//                    if (success == 1) {
//                        Log.d("Add/update", jObj.toString());
//
//                        callVolley();
//                        kosong();
//
//                        Toast.makeText(Mesin_Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                        adapter.notifyDataSetChanged();
//
//                    } else {
//                        Toast.makeText(Mesin_Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(Mesin_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters ke post url
//                Map<String, String> params = new HashMap<String, String>();
//                // jika id kosong maka simpan, jika id ada nilainya maka update
//                if (id.isEmpty()) {
//                    params.put("no_mesin", no_mesin);
//                    params.put("nama_mesin", nama_mesin);
//                    params.put("area",area);
//                } else {
//                    params.put("id", id);
//                    params.put("no_mesin", no_mesin);
//                    params.put("nama_mesin", nama_mesin);
//                    params.put("area",area);
//                }
//
//                return params;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
//    }

    // fungsi untuk get edit data
//    private void edit(final String idx) {
//        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Response: " + response.toString());
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    success = jObj.getInt(TAG_SUCCESS);
//
//                    // Cek error node pada json
//                    if (success == 1) {
//                        Log.d("get edit data", jObj.toString());
//                        String idx = jObj.getString(TAG_ID);
//                        String no_mesinx= jObj.getString(TAG_NOMOR_MESIN);
//                        String nama_mesinx = jObj.getString(TAG_NAMA_MESIN);
//                        String areax = jObj.getString(TAG_AREA);
//
//                        DialogForm(idx, no_mesinx, nama_mesinx,areax, "UPDATE");
//
//                        adapter.notifyDataSetChanged();
//
//                    } else {
//                        Toast.makeText(Mesin_Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(Mesin_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters ke post url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", idx);
//
//                return params;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
//    }

    // fungsi untuk menghapus
//    private void delete(final String idx) {
//        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Response: " + response.toString());
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    success = jObj.getInt(TAG_SUCCESS);
//
//                    // Cek error node pada json
//                    if (success == 1) {
//                        Log.d("delete", jObj.toString());
//
//                        callVolley();
//
//                        Toast.makeText(Mesin_Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//
//                        adapter.notifyDataSetChanged();
//
//                    } else {
//                        Toast.makeText(Mesin_Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(Mesin_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters ke post url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", idx);
//
//                return params;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
//    }
        private void info (final String idx){
            Intent intent = new Intent(Mesin_Activity.this, Mesin__Fragment.class);
            startActivity(intent);
        }
}