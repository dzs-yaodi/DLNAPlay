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
import android.view.View;

import com.xw.dlnaplayer.R;
import com.xw.dlnaplayer.entity.ClingDevice;
import com.xw.dlnaplayer.event.DeviceEvent;
import com.xw.dlnaplayer.manager.ClingManager;
import com.xw.dlnaplayer.manager.DeviceManager;
import com.xw.dlnaplayer.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchDialogActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private int position;
    private String videoUrl = "";
    private DeviceSelectAdapter deviceAdapter;
    public static final String DLNA_LIVE_STATE = "is_live_show";
    public static final String DLNA_PLAY_URL = "video_url";
    private boolean isLiveShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dialog);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }

        Utils.initTitleBar(this, findViewById(R.id.title_container));
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ClingManager.getInstance().startClingService();
        EventBus.getDefault().register(this);
        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setNestedScrollingEnabled(false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#eeeeee")));
        mRecycler.addItemDecoration(itemDecoration);

        deviceAdapter = new DeviceSelectAdapter(this);
        mRecycler.setAdapter(deviceAdapter);

        position = getIntent().getIntExtra("index",0);
        videoUrl = getIntent().getStringExtra("video_url");
        isLiveShow = getIntent().getBooleanExtra(SearchDialogActivity.DLNA_LIVE_STATE,false);

        deviceAdapter.setOnItemClickListener(new DeviceSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemListener(int position) {
                ClingDevice device =  deviceAdapter.getClingDevices().get(position);
                DeviceManager.getInstance().setCurrClingDevice(device);

                Intent intent = new Intent(SearchDialogActivity.this,DlnaControlActivity.class);
                intent.putExtra(SearchDialogActivity.DLNA_PLAY_URL,videoUrl);
                intent.putExtra(SearchDialogActivity.DLNA_LIVE_STATE,isLiveShow);
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
