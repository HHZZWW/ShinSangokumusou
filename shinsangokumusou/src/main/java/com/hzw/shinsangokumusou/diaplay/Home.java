package com.hzw.shinsangokumusou.diaplay;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.hzw.shinsangokumusou.R;
import com.hzw.shinsangokumusou.music.Music;
import com.hzw.shinsangokumusou.video.PlayMV;

public class Home extends AppCompatActivity implements View.OnClickListener{

    Button btn_select_map, btn_show_player, btn_show_MV, btn_game_setting, btn_game_exit;
    MediaPlayer mediaPlayer;
    Music music;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

    }

    public void init(){
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_select_map = (Button) findViewById(R.id.select_map);
        btn_show_player = (Button) findViewById(R.id.show_player);
        btn_show_MV = (Button) findViewById(R.id.show_MV);
        btn_game_setting = (Button) findViewById(R.id.game_setting);
        btn_game_exit = (Button) findViewById(R.id.game_exit);

        btn_select_map.setOnClickListener(this);
        btn_show_player.setOnClickListener(this);
        btn_show_MV.setOnClickListener(this);
        btn_game_setting.setOnClickListener(this);
        btn_game_exit.setOnClickListener(this);

        music = new Music(mediaPlayer);
        music.playBGM(Home.this, R.raw.select_mode);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_map:
                startActivity(new Intent(Home.this, SelectMap.class));
                break;
            case R.id.show_player:
                break;
            case R.id.show_MV:
                startActivity(new Intent(Home.this, PlayMV.class));
                break;
            case R.id.game_setting:
                break;
            case R.id.game_exit:
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("是否要退出？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
            default:break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if ((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT)
                        .show();
                exitTime = System.currentTimeMillis();
            }else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        music.stopBGM();
    }

    @Override
    protected void onPause() {
        super.onPause();
        music.pauseBGM();
    }

    @Override
    protected void onStop() {
        super.onStop();
        music.pauseBGM();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        music.playBGM(Home.this, R.raw.select_mode);
    }


}