package monitoring.com.mpreventive;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import monitoring.com.mpreventive.adapter.MessageListAdapter;
import monitoring.com.mpreventive.app.AppController;
import monitoring.com.mpreventive.data.BaseMessage;

public class Issue_Activity extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;

    private RecyclerView mRecyclerView;
    private List<BaseMessage> messagesList;
    private MessageListAdapter messageListAdapter;

    private static final String TAG = Inspeksi_Fragment.class.getSimpleName();

    private static String url_select = Server.URL_Chat_ambil;

    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama_lengkap";
    public static final String TAG_PESAN = "pesan";
    public static final String TAG_WAKTU = "time";

    TextView namaMesage, textMessage, timeMessage;
    EditText txtChat;

    String tag_json_obj = "json_obj_req";
    Context context;
    Button chatSend;
    int success;
    //isi user
    String id, name;
    ProgressDialog pd;
    public static final String TAG_ID_USER = "id";
    public static final String TAG_NAME = "name";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    SharedPreferences sharedpreferences;
    Boolean session = false;

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue);

        actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Chat Emergency");
        String title = actionBar.getTitle().toString();
//        actionBar.hide();

        textMessage = (TextView) findViewById(R.id.text_message_body);
        timeMessage = (TextView) findViewById(R.id.text_message_time);
        chatSend = (Button) findViewById(R.id.button_chatbox_send);
        txtChat = (EditText) findViewById(R.id.edittext_chatbox);

        mRecyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messagesList = new ArrayList<>();

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        id = getIntent().getStringExtra(TAG_ID);
        name = getIntent().getStringExtra(TAG_NAME);

        ambilPesan();

        chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtChat.length() == 0) {

                } else {
                    postChat();
                }

            }
        });
    }

    private void postChat() {

        final String id_pengirim = id;
        final String isiPesan = txtChat.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL_Chat_Post,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            success = object.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                Log.e("Tambah", object.toString());


                                messagesList.clear();
                                txtChat.setText("");
                                ambilPesan();
                            }else {
                                Toast.makeText(Issue_Activity.this, object.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Issue_Activity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(Server.KEY_ID_PENGIRIM, id_pengirim);
                params.put(Server.KEY_PESAN, isiPesan);
//                params.put("time ",date);
                Log.e("PESAN", "Emergency" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void ambilPesan() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_select, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        BaseMessage messageList = new BaseMessage();

                        messageList.setNamaNik(object.getString(TAG_NAMA));
                        messageList.setPesan(object.getString(TAG_PESAN));
                        messageList.setWaktu(object.getString(TAG_WAKTU));

                        messagesList.add(messageList);
                    }
                    MessageListAdapter messageListAdapter = new MessageListAdapter(Issue_Activity.this, messagesList);
                    mRecyclerView.setAdapter(messageListAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Pesan", "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}