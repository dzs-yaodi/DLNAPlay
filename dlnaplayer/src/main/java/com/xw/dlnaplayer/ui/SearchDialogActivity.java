package com.xw.dlnaplayer.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xw.dlnaplayer.R;
import com.xw.dlnaplayer.entity.ClingDevice;
import com.xw.dlnaplayer.event.DeviceEvent;
import com.xw.dlnaplayer.manager.ClingManager;
import com.xw.dlnaplayer.manager.DeviceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchDialogActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private int position;
    private String videoUrl = "";
    private DeviceSelectAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dialog);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }

        ClingManager.getInstance().startClingService();
        EventBus.getDefault().register(this);
        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#eeeeee")));
        mRecycler.addItemDecoration(itemDecoration);

        deviceAdapter = new DeviceSelectAdapter(this);
        mRecycler.setAdapter(deviceAdapter);

        position = getIntent().getIntExtra("index",0);
        videoUrl = getIntent().getStringExtra("video_url");

        deviceAdapter.setOnItemClickListener(new DeviceSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemListener(int position) {
                ClingDevice device =  deviceAdapter.getClingDevices().get(position);
                DeviceManager.getInstance().setCurrClingDevice(device);

                Intent intent = new Intent(SearchDialogActivity.this,DlnaControlActivity.class);
                intent.putExtra("video_url",videoUrl);
                startActivity(intent);
                finish();
            }
        });
    }

    public void refresh() {
        if (deviceAdapter == null) {
            deviceAdapter = new DeviceSelectAdapter(this);
            mRecycler.setAdapter(deviceAdapter);
        }
        deviceAdapter.notifyDataSetChanged();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(DeviceEvent event) {
        refresh();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
