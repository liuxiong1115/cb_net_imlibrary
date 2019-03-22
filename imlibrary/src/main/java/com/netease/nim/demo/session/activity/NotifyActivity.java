package com.netease.nim.demo.session.activity;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.netease.nim.demo.R;
import com.netease.nim.uikit.common.activity.UI;

/**
 * 新访客
 * Created by winnie on 2018/3/14.
 */

public class NotifyActivity extends UI{

    Button know;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        initView();
    }

    /**
     * 绑定控件
     */
    private void initView () {
        know = findViewById(R.id.notitfy_know);
        //播放系统提示铃声
     /*   Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
        rt.play();*/
       final  MediaPlayer mediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mediaPlayer.setLooping(true);
        try {
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        //震动
        final Vibrator vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        long[] patter = {1000, 1000, 1000, 1000};
        vibrator.vibrate(patter, 0);
        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.cancel();
                mediaPlayer.stop();
                finish();
            }
        });
    }
    //获取系统默认铃声的Uri
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_NOTIFICATION);
    }

}
