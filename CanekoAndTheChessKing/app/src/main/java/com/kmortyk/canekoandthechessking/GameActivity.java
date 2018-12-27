package com.kmortyk.canekoandthechessking;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.kmortyk.canekoandthechessking.game.effects.Effect;
import com.kmortyk.canekoandthechessking.game.effects.PopUpText;
import com.kmortyk.canekoandthechessking.thread.GameThread;

public class GameActivity extends AppCompatActivity {

    private GameThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // no title bar, full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        ActionBar ab = getSupportActionBar();
        if(ab != null) ab.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //getWindow().setFormat(PixelFormat.RGBA_8888);

        Intent intent = getIntent();
        String level = intent.getStringExtra("level");
        gameThread = new GameThread(this, level);
        AppView appView = new AppView(this, gameThread);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(appView);
    }

    @Override
    public void onBackPressed() {
        PopUpText.addTo(gameThread.getGameWorld(), 0, 0, "Wanna back?");
    }

    public void openWorldMap() {
        Intent intent = new Intent(GameActivity.this, MapActivity.class);
        startActivity(intent);
    }
}
