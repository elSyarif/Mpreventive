package monitoring.com.mpreventive.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

import monitoring.com.mpreventive.R;
import monitoring.com.mpreventive.data.BaseMessage;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.myViewHolder> {

    private Activity activity;
    private List<BaseMessage> messagesList;
    private ArrayList<BaseMessage> PesanList;
    private Context mContext;
    private String[] mDataSet;

    public MessageListAdapter(Context mContext, List<BaseMessage> PesanList) {
        this.mContext = mContext;
        this.messagesList = PesanList;

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @Override
    public MessageListAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.adapter_view_issue, null);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        BaseMessage baseMessage = messagesList.get(position);

        holder.nameMessage.setText(baseMessage.getNamaNik());
        holder.textMessege.setText(baseMessage.getPesan());
        holder.timeMessage.setText(baseMessage.getWaktu());
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView nameMessage, textMessege, timeMessage;

        public myViewHolder(View itemView) {
            super(itemView);
            nameMessage = (TextView) itemView.findViewById(R.id.text_message_name);
            textMessege = (TextView) itemView.findViewById(R.id.text_message_body);
            timeMessage = (TextView) itemView.findViewById(R.id.text_message_time);

        }
    }


}