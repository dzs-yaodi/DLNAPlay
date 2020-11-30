package com.xw.dlnaplayer;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.dlnaplayer.callback.ControlCallback;
import com.xw.dlnaplayer.entity.RemoteItem;
import com.xw.dlnaplayer.manager.ClingManager;
import com.xw.dlnaplayer.manager.ControlManager;
import com.xw.dlnaplayer.utils.VMDate;

import org.fourthline.cling.support.model.item.Item;
import org.greenrobot.eventbus.EventBus;

public class PlayControlle implements View.OnClickListener {

    private static PlayControlle instance = null;
    private ImageView imagePlay;
    private ImageView imageClose;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private SeekBar seekBar;
    private Context mContext;
    private View parentView;

    private int currProgress = 0;
    private Item localItem;
    private RemoteItem remoteItem;
    private String duration;

    public static PlayControlle getInstance() {

        if (instance == null){
            synchronized (PlayControlle.class){
                if (instance == null){
                    instance = new PlayControlle();
                }
            }
        }
        return instance;
    }

    public void init(View view, Context context){

        this.mContext = context;
        this.parentView = view;
        imagePlay = view.findViewById(R.id.image_play);
        imageClose = view.findViewById(R.id.image_close);
        tvCurrentTime = view.findViewById(R.id.current);
        tvTotalTime = view.findViewById(R.id.total);
        seekBar = view.findViewById(R.id.bottom_seek_progress);

        localItem = ClingManager.getInstance().getLocalItem();
        remoteItem = ClingManager.getInstance().getRemoteItem();
        parentView.setVisibility(View.VISIBLE);

        imagePlay.setOnClickListener(this);
        imageClose.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currProgress = seekBar.getProgress();
                tvCurrentTime.setText(VMDate.toTimeString(currProgress));
                seekCast(currProgress);
            }
        });
    }

    public void destroy(){
        if (instance != null){
            stopCast();
            instance = null;
        }
    }

    /**
     * 改变投屏进度
     */
    private void seekCast(int progress) {
        String target = VMDate.toTimeString(progress);
        ControlManager.getInstance().seekCast(target, new ControlCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Seek cast failed %s", msg));
            }
        });
    }

    /**
     * 设置音量拖动监听
     */
    private void setVolumeSeekListener() {
//        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtils.d("Volume seek position: %d", progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                setVolume(seekBar.getProgress());
//            }
//        });
    }

    private void showToast(final String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.image_play){
            play();
        }else if (id == R.id.image_close){
            stop();
            parentView.setVisibility(View.GONE);
        }
    }

    /**
     * 播放开关
     */
    private void play() {
        if (ControlManager.getInstance().getState() == ControlManager.CastState.STOPED) {
            if (localItem != null) {
                newPlayCastLocalContent();
            } else {
                newPlayCastRemoteContent();
            }
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PAUSED) {
            playCast();
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PLAYING) {
            pauseCast();
        } else {
            showToast("正在连接设备，稍后操作");
        }
    }

    /**
     * 关闭投屏
     */
    private void stop() {
        ControlManager.getInstance().unInitScreenCastCallback();
        stopCast();
    }

    private void newPlayCastLocalContent() {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(localItem, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                ControlManager.getInstance().initScreenCastCallback();

                changePlayState(true);
            }

            @Override
            public void onError(int code, String msg) {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                showToast(String.format("New play cast local content failed %s", msg));
            }
        });
    }

    private void newPlayCastRemoteContent() {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(remoteItem, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                ControlManager.getInstance().initScreenCastCallback();
                changePlayState(true);
            }

            @Override
            public void onError(int code, String msg) {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                showToast(String.format("New play cast remote content failed %s", msg));
            }
        });
    }

    private void playCast() {
        ControlManager.getInstance().playCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                changePlayState(true);
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Play cast failed %s", msg));
            }
        });
    }

    private void pauseCast() {
        ControlManager.getInstance().pauseCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PAUSED);
                changePlayState(false);
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Pause cast failed %s", msg));
            }
        });
    }

    private void stopCast() {
        ControlManager.getInstance().stopCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                changePlayState(false);
//                EventBus.getDefault().post("close");
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Stop cast failed %s", msg));
            }
        });
    }

    public void changePlayState(boolean isPlaying){

        if (isPlaying){
            imagePlay.setImageResource(R.drawable.dlna_video_click_pause_selector);
        }else{
            imagePlay.setImageResource(R.drawable.dlna_video_click_play_selector);
        }
    }

    public void transitioning() {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
    }

    public void playing() {
        ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
        changePlayState(true);
    }

    public void pause() {
        ControlManager.getInstance().setState(ControlManager.CastState.PAUSED);
        changePlayState(false);
    }

    public void stops() {
        ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
        changePlayState(false);
    }

    public void setMediaDuration(String mediaDuration) {
        if (!TextUtils.isEmpty(mediaDuration)) {
            if (!TextUtils.equals(duration,mediaDuration)) {
                duration = mediaDuration;
                tvTotalTime.setText(duration);
                seekBar.setMax((int) VMDate.fromTimeString(duration));
            }
        }
    }


    public void setTimePosition(String timePosition) {
        if (!TextUtils.isEmpty(timePosition)) {
            long progress = VMDate.fromTimeString(timePosition);
            seekBar.setProgress((int) progress);
            tvCurrentTime.setText(timePosition);
        }
    }
}
