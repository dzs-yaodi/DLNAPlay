package com.xw.dlnaplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xw.dlnaplayer.R;
import com.xw.dlnaplayer.entity.ClingDevice;
import com.xw.dlnaplayer.event.DeviceEvent;
import com.xw.dlnaplayer.manager.DeviceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchDialogActivity extends AppCompatActivity {

    private DeviceAdapter deviceAdapter;
    private ListView listView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dialog);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }

        listView = findViewById(R.id.listView);
        deviceAdapter = new DeviceAdapter(this);
        listView.setAdapter(deviceAdapter);

        position = getIntent().getIntExtra("index",0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClingDevice device =  deviceAdapter.getClingDevices().get(i);
                DeviceManager.getInstance().setCurrClingDevice(device);

                Intent intent = new Intent();
                intent.putExtra("index",position);
                setResult(101,intent);
                finish();
            }
        });

    }

    public void refresh() {
        if (deviceAdapter == null) {
            deviceAdapter = new DeviceAdapter(this);
            listView.setAdapter(deviceAdapter);
        }
        deviceAdapter.refresh();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(DeviceEvent event) {
        refresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
