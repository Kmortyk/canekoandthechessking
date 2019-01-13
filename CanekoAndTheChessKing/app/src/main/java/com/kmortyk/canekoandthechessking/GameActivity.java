package com.kmortyk.canekoandthechessking;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.kmortyk.canekoandthechessking.thread.GameThread;

public class GameActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private GameThread gameThread;
    private AppView appView;

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
        appView = new AppView(this, gameThread);
        setContentView(appView);
    }

    /*@Override
    public void onBackPressed() {
        PopUpText.addTo(gameThread.getGameWorld(), 0, 0, "Wanna back?");
    }*/

    public void openWorldMap() {
        Intent intent = new Intent(GameActivity.this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewGroup vg = (ViewGroup)(appView.getParent());
        if(vg !=  null) { vg.removeView(appView); }
        appView = null;
    }
}
