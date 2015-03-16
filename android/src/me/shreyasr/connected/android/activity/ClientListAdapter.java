package me.shreyasr.connected.android.activity;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import me.shreyasr.connected.android.R;
import me.shreyasr.connected.android.util.Client;

public class ClientListAdapter extends BaseAdapter {

    List<Client> clients = new LinkedList<Client>();
    Handler handler = new Handler();

    Context context;
    private ListView listView;

    public ClientListAdapter(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    public void add(final Client client) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!clients.contains(client))
                    clients.add(client);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public Object getItem(int position) {
        return clients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.client_list_item, null);
        }

        TextView clientIp = (TextView) view.findViewById(R.id.client_ip);
        clientIp.setText(clients.get(position).ip);

        return view;
    }
}
