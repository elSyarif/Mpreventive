package monitoring.com.mpreventive;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import monitoring.com.mpreventive.data.Data_HIstory_Detail;

public class History__Fragment extends AppCompatActivity {
    public static final String TAG_ID = "ID";
    public static final String TAG_NAMA_MESIN = "Nama_Mesin";

    public static final String TAG_OV_H = "Drive_end_OV_H";
    public static final String TAG_OV_V = "Drive_end_OV_V";
    public static final String TAG_BV_H = "Drive_end_BV_H";
    public static final String TAG_BV_V = "Drive_end_BV_V";
    public static final String TAG_Non_OV_H = "Non_Drive_end_OV_H";
    public static final String TAG_Non_OV_V = "Non_Drive_end_OV_V";
    public static final String TAG_Non_BV_H = "Non_Drive_end_BV_H";
    public static final String TAG_Non_BV_V = "Non_Drive_end_BV_V";
    LineChart mChart;

    TextView txtv_id, txtv_name;
    String id, name;
    private static String url_select = Server.URL_getHistory;

    private static final String TAG = History_Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_info_frag);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Data History");
        String title = actionBar.getTitle().toString();

        txtv_id = (TextView) findViewById(R.id.tv_fr_id_h);
        txtv_name = (TextView) findViewById(R.id.tv_fr_name_h);

        id = getIntent().getStringExtra(TAG_ID);
        name = getIntent().getStringExtra(TAG_NAMA_MESIN);

        txtv_id.setText(id);
        txtv_name.setText(name);

        mChart = (LineChart) findViewById(R.id.histrory_chart);
        setData(40, 15);
        mChart.animateXY(3000, 3000);


    }

    private void setData(int count, int range) {

        ArrayList<Entry> yVals1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            yVals1.add(new Entry(i, val));
        }

        ArrayList<Entry> yVals2 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range + 15);
            yVals2.add(new Entry(i, val));
        }

        ArrayList<Entry> yVals3 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range + 20);
            yVals3.add(new Entry(i, val));
        }

        LineDataSet dataSet1, dataSet2, dataSet3;

        dataSet1 = new LineDataSet(yVals1, "Data 1");
        dataSet1.setColor(Color.GREEN);
        dataSet1.setDrawCircles(true);
        dataSet1.setLineWidth(2.5f);

        dataSet2 = new LineDataSet(yVals2, "Data 2");
        dataSet3 = new LineDataSet(yVals3, "Data 3");

        LineData data = new LineData(dataSet1, dataSet2, dataSet3);


        mChart.setData(data);

    }


}
