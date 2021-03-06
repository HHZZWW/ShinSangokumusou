package com.hzw.shinsangokumusou.diaplay;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hzw.shinsangokumusou.R;
import com.hzw.shinsangokumusou.database.DataBase;
import com.hzw.shinsangokumusou.interfaces.DBUtils;
import com.hzw.shinsangokumusou.maps.Maps;
import com.hzw.shinsangokumusou.music.BGM;
import com.hzw.shinsangokumusou.music.SoundEffects;
import com.hzw.shinsangokumusou.staticvalue.SQLiteValue;
import com.hzw.shinsangokumusou.utils.ActivityUtils;

import java.util.TimerTask;

public class SelectPlayer extends BaseDisplay implements DBUtils {

    LinearLayout show_playerLL;
    ProgressBar player_HP, player_atc, player_def, player_power;
    Button player_confirm;
    private SQLiteDatabase sqLiteDatabase;
    private DataBase dataBase;
    private MediaPlayer mediaPlayer;
    BGM BGM = new BGM(mediaPlayer);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        init();
    }

    public void init(){

        //加载玩家数据
        Cursor cursor = getCursor(SQLiteValue.Query_Count_Players, null);
        while (cursor.moveToNext()){
            int count = cursor.getInt(0);
            if (count == 0){
                sqLiteDatabase.execSQL(SQLiteValue.Insert_Player, new Object[]{null, "赵云", 100, 90, 80, 80});
            }
        }
        cursor.close();

        BGM.playBGM(SelectPlayer.this, R.raw.music_select_map_or_player);

        //加载人物头像
        show_playerLL = (LinearLayout) findViewById(R.id.show_player_head);
        for (int i = 0; i < 10; i++) {

            Button imageView = new Button(this);
            imageView.setId(i);

            if ((i % 2) == 0){
                imageView.setBackgroundColor(Color.RED);
            }else {
                imageView.setBackgroundColor(Color.BLUE);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 400);
            layoutParams.setMargins(20, 20, 20, 20);
            imageView.setLayoutParams(layoutParams);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    set_Player_Progress();
                }
            });
            show_playerLL.addView(imageView);
        }

        player_HP = (ProgressBar) findViewById(R.id.player_HP);
        player_atc = (ProgressBar) findViewById(R.id.player_atc);
        player_def = (ProgressBar) findViewById(R.id.player_def);
        player_power = (ProgressBar) findViewById(R.id.player_power);
        player_confirm = (Button) findViewById(R.id.player_confirm);

        player_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SoundEffects(SelectPlayer.this, new SoundPool.Builder().build()).playSoundEffects(R.raw.soundeffects_commit);
                new ActivityUtils().DelayJunm(SelectPlayer.this, Maps.class, 1000);
                finish();
            }
        });

        player_HP.setProgress(Get_play_data("赵云", 1));
        player_atc.setProgress(Get_play_data("赵云", 2));
        player_def.setProgress(Get_play_data("赵云", 3));
        player_power.setProgress(Get_play_data("赵云", 4));

        closeDB();

    }

    /**
     * 获取玩家各项数据
     * @param player_name 玩家名
     * @param index 数据在表中位置
     * @return 数据
     */
    public int Get_play_data(String player_name, int index){

        Cursor cursor = sqLiteDatabase.rawQuery(SQLiteValue.Query_Player_Name, new String[]{player_name});
        int data = 0;
        while (cursor.moveToNext()){
            data = cursor.getInt(index + 1);
        }
        closeCursor(cursor);
        return data;
    }

    /**
     * 设置玩家（进度条）数据
     */
    public void set_Player_Progress(){
        player_HP.setProgress((int) (Math.random() * 100));
        player_atc.setProgress((int) (Math.random() * 100));
        player_def.setProgress((int) (Math.random() * 100));
        player_power.setProgress((int) (Math.random() * 100));
    }

    @Override
    protected void onPause() {
        super.onPause();
        BGM.pauseBGM();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new SoundEffects(SelectPlayer.this, new SoundPool.Builder().build()).playSoundEffects(R.raw.soundeffects_cancel);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                BGM.playBGM(SelectPlayer.this, R.raw.music_select_map_or_player);
            }
        };
        ActivityUtils.DelaTask(timerTask, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BGM.stopBGM();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BGM.stopBGM();
    }

    @Override
    public SQLiteDatabase getDB() {
        sqLiteDatabase = new DataBase(SelectPlayer.this).getWritableDatabase();
        return sqLiteDatabase;
    }

    @Override
    public Cursor getCursor(String sql, String[] strings) {
        return getDB().rawQuery(sql, strings);
    }

    @Override
    public void InsertDB(String sql, Object[] objects) {
        getDB().execSQL(sql, objects);
    }

    @Override
    public void UpdateDB(String sql, Object[] objects) {

    }

    @Override
    public void closeCursor(Cursor cursor) {
        cursor.close();
    }

    @Override
    public void closeDB() {
        getDB().close();
    }
}
