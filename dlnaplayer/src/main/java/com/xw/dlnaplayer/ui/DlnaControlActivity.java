package com.xw.dlnaplayer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xw.dlnaplayer.PlayControlle;
import com.xw.dlnaplayer.R;
import com.xw.dlnaplayer.entity.RemoteItem;
import com.xw.dlnaplayer.event.CurrentEvent;
import com.xw.dlnaplayer.listener.ControlListener;
import com.xw.dlnaplayer.manager.ClingManager;
import com.xw.dlnaplayer.utils.Utils;

import org.greenrobot.eventbus.EventBus;

public class DlnaControlActivity extends AppCompatActivity {

    private FrameLayout frameDlnaControl;
    private boolean isLiveShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlna_control_layout);
        Utils.setTranslucentStatus(this);

        frameDlnaControl = findViewById(R.id.frame_dlna_control);

        String url = getIntent().getStringExtra(SearchDialogActivity.DLNA_PLAY_URL);
        isLiveShow = getIntent().getBooleanExtra(SearchDialogActivity.DLNA_LIVE_STATE,false);

        RemoteItem itemurl1 = new RemoteItem("", "425703", "sobey",
                107362668, "00:04:33", "1280x720", url);
        ClingManager.getInstance().setRemoteItem(itemurl1);

        PlayControlle.getInstance().init(frameDlnaControl,this,PlayControlle.ACTIVITY_MODE,isLiveShow);

        PlayControlle.getInstance().setCloseListener(new ControlListener() {
            @Override
            public void getDlnaPlayPosition(long progress) {
                EventBus.getDefault().post(new CurrentEvent(progress));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        PlayControlle.getInstance().destroy();
        ClingManager.getInstance().destroy();
        super.onDestroy();
    }
}
