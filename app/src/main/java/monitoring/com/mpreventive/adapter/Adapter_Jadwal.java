package monitoring.com.mpreventive.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import monitoring.com.mpreventive.R;
import monitoring.com.mpreventive.data.Data_Jadwal;

public class Adapter_Jadwal extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Data_Jadwal> items_list;
    private DateFormat format;

    public Adapter_Jadwal(Activity activity, List<Data_Jadwal> items) {
        this.activity = activity;
        this.items_list = items;
    }

    @Override
    public int getCount() {
        return items_list.size();
    }

    @Override
    public Object getItem(int location) {
        return items_list.get(location);
    }

    @Override
    public long getItemId(int posisi) {
        return posisi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.adapter_view_jadwal, null);

//        TextView id = (TextView) convertView.findViewById(R.id.);
        TextView title = (TextView) convertView.findViewById(R.id.tv_List_J_title);
        TextView description = (TextView) convertView.findViewById(R.id.tv_list_J_Desk);
        TextView starte = (TextView) convertView.findViewById(R.id.tv_list_J_start);
        TextView end = (TextView) convertView.findViewById(R.id.tv_list_J_end);

        Data_Jadwal data = items_list.get(position);



//        id.setText(data.getId());
        title.setText(data.getTitle());
        description.setText(data.getDescription());
       starte.setText(data.getStart());
//
        end.setText(data.getEnd());

        return convertView;
    }
}
