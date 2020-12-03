package com.xw.dlnaplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xw.dlnaplayer.PlayControlle;
import com.xw.dlnaplayer.entity.RemoteItem;
import com.xw.dlnaplayer.listener.ControlListener;
import com.xw.dlnaplayer.manager.ClingManager;
import com.xw.dlnaplayer.ui.SearchDialogActivity;

public class MainActivity extends AppCompatActivity {

    Button button;
    String  url1="http://hcsoss.gztv.com/u-/transcode/2020/11/25/HD-MP4/2C2430E8008B3CBCE03DF51FA64EC44B.mp4";
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);
        frameLayout = findViewById(R.id.frame_dlna_control);

        ClingManager.getInstance().startClingService();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchDialogActivity.class);
                intent.putExtra("video_url",url1);
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == 101){
            if (data != null) {
                int position = data.getIntExtra("indx", 0);
            }

            button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RemoteItem itemurl1 = new RemoteItem("一路之下", "425703", "张杰",
                            107362668, "00:04:33", "1280x720", url1);
                    ClingManager.getInstance().setRemoteItem(itemurl1);
                    PlayControlle.getInstance().init(frameLayout, MainActivity.this,PlayControlle.VIEW_MODE);
                    PlayControlle.getInstance().setCloseListener(new ControlListener() {
                        @Override
                        public void getDlnaPlayPosition(long progress) {
                            Toast.makeText(MainActivity.this, "播放进度" + progress, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            },500);
        }
    }

    @Override
    protected void onDestroy() {
        ClingManager.getInstance().destroy();
        super.onDestroy();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventPlayer(String event) {
//        if ("init".equals(event)){
//            RemoteItem itemurl1 = new RemoteItem("一路之下", "425703", "张杰",
//                    107362668, "00:04:33", "1280x720", url1);
//            ClingManager.getInstance().setRemoteItem(itemurl1);
//            PlayControlle.getInstance().init(frameLayout,MainActivity.this);

//            button.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (frameLayout.getVisibility() != View.VISIBLE){
//                        frameLayout.setVisibility(View.VISIBLE);
//                    }
//                }
//            },2000);
//        }else if ("close".equals(event)){
//            if (frameLayout.getVisibility() == View.VISIBLE){
//                frameLayout.setVisibility(View.GONE);
//            }
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventBus(ControlEvent event) {
//        AVTransportInfo avtInfo = event.getAvtInfo();
//        if (avtInfo != null) {
//
//            if (!TextUtils.isEmpty(avtInfo.getState())) {
//                if (avtInfo.getState().equals("TRANSITIONING")) {
//                    PlayControlle.getInstance().transitioning();
//                } else if (avtInfo.getState().equals("PLAYING")) {
//                    PlayControlle.getInstance().playing();
//                } else if (avtInfo.getState().equals("PAUSED_PLAYBACK")) {
//                    PlayControlle.getInstance().pause();
//                } else if (avtInfo.getState().equals("STOPPED")) {
//                    PlayControlle.getInstance().stops();
//                } else {
//                    PlayControlle.getInstance().stops();
//                }
//            }
//
//            PlayControlle.getInstance().setMediaDuration(avtInfo.getMediaDuration());
//            PlayControlle.getInstance().setTimePosition(avtInfo.getTimePosition());
//
//        }
//    }
}
