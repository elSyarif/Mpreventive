package monitoring.com.mpreventive;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Jadwal__Fragment extends AppCompatActivity {
    public static final String TAG_ID = "ID";
    public static final String TAG_NAMA_MESIN = "Nama_Mesin";
    TextView txtv_id, txtv_name;
    String id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jadwal_info_frag);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Jadwal Tugas");
        String title = actionBar.getTitle().toString();

        txtv_id = (TextView) findViewById(R.id.tv_fr_id);
        txtv_name = (TextView) findViewById(R.id.tv_fr_name);

        id = getIntent().getStringExtra(TAG_ID);
        name = getIntent().getStringExtra(TAG_NAMA_MESIN);

        txtv_id.setText(id);
        txtv_name.setText(name);
    }
}
