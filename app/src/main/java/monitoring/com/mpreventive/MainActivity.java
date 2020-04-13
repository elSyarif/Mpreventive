package monitoring.com.mpreventive;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    CardView BTcard_mesin, BTcard_jadwal, BTcard_history, BTcard_inspeksi;
    Button btn_logout;
    TextView txtv_id, txtv_username, txtv_name;
    String id, jabatan, name;
    SharedPreferences sharedpreferences;
    Toolbar toolbar;
    public static final String TAG_NAME = "name";
    public static final String TAG_ID = "id";
    public static final String TAG_JABATAN = "jabatan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        txtv_id = (TextView) findViewById(R.id.txtv_id_freg);
        txtv_username = (TextView) findViewById(R.id.txv_jabatan);
        txtv_name = (TextView) findViewById(R.id.txv_nama);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        BTcard_mesin = (CardView) findViewById(R.id.card_mesin);
        BTcard_jadwal = (CardView) findViewById(R.id.card_jadwal);
        BTcard_history = (CardView) findViewById(R.id.card_history);
        BTcard_inspeksi = (CardView) findViewById(R.id.card_inspeksi);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        id = getIntent().getStringExtra(TAG_ID);
        jabatan = getIntent().getStringExtra(TAG_JABATAN);
        name = getIntent().getStringExtra(TAG_NAME);

        txtv_username.setText(jabatan);
        txtv_name.setText(name);

        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //  TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_JABATAN, null);
                editor.putString(TAG_NAME, null);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        BTcard_mesin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  TODO Auto-generated method stub


                Intent intent = new Intent(MainActivity.this, Mesin_Activity.class);

                startActivity(intent);
            }
        });

        BTcard_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  TODO Auto-generated method stub

                Intent intent = new Intent(MainActivity.this, History_Activity.class);
                startActivity(intent);
            }
        });
        BTcard_inspeksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  TODO Auto-generated method stub
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(TAG_ID, id);
                editor.putString(TAG_NAME, name);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, Inspeksi_Activity.class);
                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_NAME, name);
                startActivity(intent);
            }
        });
        BTcard_jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  TODO Auto-generated method stub

                Intent intent = new Intent(MainActivity.this, Jadwal_Activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void onIssueAction(MenuItem issue) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(TAG_ID, id);
        editor.putString(TAG_NAME, name);
        editor.commit();

        Intent intent = new Intent(MainActivity.this, Issue_Activity.class);
        intent.putExtra(TAG_ID, id);
        intent.putExtra(TAG_NAME, name);
        startActivity(intent);
    }
}