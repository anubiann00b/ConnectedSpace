package me.shreyasr.connected.android.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.shreyasr.connected.android.R;
import me.shreyasr.connected.android.networking.ConnectionThread;
import me.shreyasr.connected.android.networking.LobbyManager;
import me.shreyasr.connected.android.util.Client;

public class LobbyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        ListView listView = (ListView) this.findViewById(R.id.client_list);
        final ClientListAdapter adapter = new ClientListAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client client = (Client) adapter.getItem(position);
                try {
                    new Thread(new ConnectionThread(LobbyActivity.this, InetAddress.getByName(client.ip))).start();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

        new LobbyManager(this, adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lobby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
