package monitoring.com.mpreventive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import monitoring.com.mpreventive.app.AppController;

public class LoginActivity extends AppCompatActivity {

    android.support.v7.app.ActionBar actionBar;
    ProgressDialog pDialog;
    Button btn_login;
    EditText txt_username, txt_password;
    Intent intent;
    int success;
    ConnectivityManager conMgr;

    public String url = Server.URL_Login;

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_NAME = "name";
    public final static String TAG_JABATAN = "jabatan";
    public final static String TAG_ID = "id";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, jabatan, name;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Login");
        String title = actionBar.getTitle().toString(); // get the title
        actionBar.hide(); // or even hide the actionbar

        btn_login = (Button) findViewById(R.id.btn_login);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            assert conMgr != null;
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        session = sharedpreferences.getBoolean(session_status, false);

        id = sharedpreferences.getString(TAG_ID, null);
        jabatan = sharedpreferences.getString(TAG_JABATAN, null);
        name = sharedpreferences.getString(TAG_NAME, null);

        if (session) {
            intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_JABATAN, jabatan);
            intent.putExtra(TAG_NAME, name);
            finish();
            startActivity(intent);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                String Username = txt_username.getText().toString();
                String Password = txt_password.getText().toString();

                if (Username.trim().length() > 0 && Password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(Username, Password);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog);
        pDialog.setCancelable(true);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest str_Req = new StringRequest(Request.Method.POST, url, new Response.Listener <String>() {
            @Override
            public void onResponse(String Response) {
                Log.e(TAG, "Login Response: " + Response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(Response);
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        String jabatan = jObj.getString(TAG_JABATAN);
                        String id = jObj.getString(TAG_ID);
                        String name = jObj.getString(TAG_NAME);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_JABATAN, jabatan);
                        editor.putString(TAG_NAME, name);
                        editor.commit();

                        //memanggil Main Avtivity
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_JABATAN, jabatan);
                        intent.putExtra(TAG_NAME, name);

                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(str_Req, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
