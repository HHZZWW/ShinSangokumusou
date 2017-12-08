package com.hzw.shinsangokumusou.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity工具
 * Project ShinSangokumusou/com.hzw.shinsangokumusou.utils/ActivityUtils
 * Created by HZW
 * Data 2017/12/9
 * Time 2:09
 */

public class ActivityUtils extends AppCompatActivity {
    //定时跳转
    public void DelayJunm(final Activity context, Class<?> cls, long delay){
        final Intent intent = new Intent(context, cls);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                context.startActivity(intent);
                LogUtil.args_1("xxx", "不能finish的类", context.getClass().getName());
                if (context.getClass().getName().equals("com.hzw.shinsangokumusou.diaplay.Welcome")){
                    context.finish();
                }
                finish();
            }
        };
        //设置4000毫秒后跳转
        timer.schedule(timerTask, delay);
    }
}
