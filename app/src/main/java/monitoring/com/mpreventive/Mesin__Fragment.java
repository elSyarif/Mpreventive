package monitoring.com.mpreventive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Mesin__Fragment extends AppCompatActivity {

    TextView txtv_id, txtv_name;
    String id,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mesin_info_frag);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Data Mesin");
        String title = actionBar.getTitle().toString();

        txtv_id = (TextView) findViewById(R.id.tv_fr_id);
        txtv_name = (TextView) findViewById(R.id.tv_fr_name);


        txtv_id.setText(id);
        txtv_name.setText(name);
    }
}
