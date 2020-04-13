package monitoring.com.mpreventive.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import monitoring.com.mpreventive.R;
import monitoring.com.mpreventive.data.Data;

public class Adapter_inspeksi extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items_list;

    public Adapter_inspeksi(Activity activity, List<Data> items) {
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.adapter_view_mesin, null);

//        TextView id_mesin = (TextView) convertView.findViewById(R.id.tv_list_id);
        TextView nama_mesin = (TextView) convertView.findViewById(R.id.tv_List_nama);
        TextView area = (TextView) convertView.findViewById(R.id.tv_list_area);
        TextView no_mesin = (TextView) convertView.findViewById(R.id.tv_list_nomor_mesin);

        Data data = items_list.get(position);

//        id_mesin.setText(data.getId());
        nama_mesin.setText(data.getNama_mesin());
        no_mesin.setText(data.getNo_mesin());
        area.setText(data.getArea());


        return convertView;
    }
    private void kosong(){
//        txt_id.setText(null);
//        txt_nama.setText(null);
//        txt_alamat.setText(null);
    }
}
