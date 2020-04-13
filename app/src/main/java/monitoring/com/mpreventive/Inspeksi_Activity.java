package monitoring.com.mpreventive;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import monitoring.com.mpreventive.adapter.Adapter_inspeksi;
import monitoring.com.mpreventive.app.AppController;
import monitoring.com.mpreventive.helper.PermissionHelper;
import monitoring.com.mpreventive.record.NdefMessageParser;
import monitoring.com.mpreventive.record.ParsedNdefRecord;

public class Inspeksi_Activity extends AppCompatActivity {

    PermissionHelper permissionHelper;
    Intent intent;
    android.support.v7.app.ActionBar actionBar;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView text, txt_tgl;
    EditText tag_in;
    private static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateTimeInstance();
    Button sending;
    private String KEY_TAG = "TAG_IN";

    String id, name;
    ProgressDialog pd;
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";

    // Cek Data Mesin
    ConnectivityManager conMgr;
    public String url = Server.URL_Cek;
    private static final String TAG = Inspeksi_Activity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static final String TAG_ID_M = "ID";
    public static final String TAG_NO_M = "No_Mesin";

    int success;
    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id_mesin, no_mesin;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspeksi);

        actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Inspeksi");
        String title = actionBar.getTitle().toString();

        permissionHelper = new PermissionHelper(this);
        checkAndRequestPermissions();

        //NFC TAGING
//        text = (TextView) findViewById(R.id.text);
        txt_tgl = (TextView) findViewById(R.id.Tgl);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tag_in = (EditText) findViewById(R.id.tag_ing);
        sending = (Button) findViewById(R.id.send_tag);

//        cek Data mesin
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

//        User Ref
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        id = getIntent().getStringExtra(TAG_ID);
        name = getIntent().getStringExtra(TAG_NAME);
        id_mesin = sharedpreferences.getString(TAG_ID_M, null);
        no_mesin = sharedpreferences.getString(TAG_NO_M, null);

//        if (session) {
//            Intent intent = new Intent(Inspeksi_Activity.this, Inspeksi_Fragment.class);
//            intent.putExtra(TAG_ID_M, id_mesin);
//            intent.putExtra(TAG_NO_M, no_mesin);
//            finish();
//            startActivity(intent);
//        }
//        tag_in.setText("PU-110211A");

//        if (nfcAdapter == null) {
//            Toast.makeText(this, "Android Anda Belum Suport NFC", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass()).
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        sending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String cek_no_mesin = tag_in.getText().toString();
                if (cek_no_mesin.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkMesin(cek_no_mesin);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplication(), "TAG Harus Di Tempelkan!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean checkAndRequestPermissions() {
        permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionCheckDone() {

            }
        });

        permissionHelper.checkAndRequestPermissions();

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
    }

    public void checkMesin(final String cek_No_Mesin) {
        pd = new ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog);
        pd.setCancelable(true);
        pd.setIndeterminate(true);
        pd.setMessage("Checking...");
        showDialog();

        StringRequest str_req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String Response) {
                Log.e(TAG, "Check Response: " + Response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(Response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        String ID = jObj.getString(TAG_ID_M);
                        String No_Mesin = jObj.getString(TAG_NO_M);

                        Log.e("Checking Success!", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);

                        editor.putString(TAG_ID_M, ID);
                        editor.putString(TAG_NO_M, No_Mesin);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_NAME, name);
                        editor.commit();

                        //memanggil
                        Intent intent = new Intent(getApplicationContext(), Inspeksi_Fragment.class);
                        intent.putExtra(TAG_ID_M, ID);
                        intent.putExtra(TAG_NO_M, No_Mesin);
                        intent.putExtra(TAG_ID, id);
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
                Log.e(TAG, "Checking Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("No_Mesin", cek_No_Mesin);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(str_req, tag_json_obj);
    }

    private void showDialog() {
        if (!pd.isShowing())
            pd.show();
    }

    private void hideDialog() {
        if (pd.isShowing())
            pd.dismiss();
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();


            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }

            displayMsgs(msgs);
        }
    }

    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        Date now = new Date();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("");
        }
        tag_in.setText(builder.toString());
        txt_tgl.setText(DATE_FORMAT.format(now));


    }

    private void showWirelessSettings() {
        Toast.makeText(this, "Hidupkan NFC Pada Android Anda", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

}
