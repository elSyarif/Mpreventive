package monitoring.com.mpreventive.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import monitoring.com.mpreventive.R;
import monitoring.com.mpreventive.data.Data_History;

public class Adapter_History extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Data_History> items_list;

    public Adapter_History(Activity activity, List<Data_History> items) {
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
            convertView = inflater.inflate(R.layout.adapter_view_history, null);

//        TextView id_mesin = (TextView) convertView.findViewById(R.id.tv_list_H_nama_mesin);
        TextView nama_mesin = (TextView) convertView.findViewById(R.id.tv_list_H_nama_mesin);
        TextView no_mesin = (TextView) convertView.findViewById(R.id.tv_list_H_nomor_mesin);
        TextView area = (TextView) convertView.findViewById(R.id.tv_list_H_area);
        TextView tanggal = (TextView) convertView.findViewById(R.id.tv_List_H_tanggal);
        ImageView thumnel = (ImageView) convertView.findViewById(R.id.thumbnail_hi);

        Data_History data = items_list.get(position);

//        id_mesin.setText(data.getId());
        nama_mesin.setText(data.getNama_mesin());
        no_mesin.setText(data.getNo_mesin());
        area.setText(data.getArea());
        tanggal.setText(data.getTanggal());

        //load gambar
        Glide.with(activity).load(data.getGambaran())
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumnel);


        return convertView;
    }
}
