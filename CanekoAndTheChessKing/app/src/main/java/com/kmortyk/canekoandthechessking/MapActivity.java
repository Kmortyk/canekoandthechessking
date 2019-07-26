package com.kmortyk.canekoandthechessking;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.kmortyk.canekoandthechessking.thread.MapThread;

public class MapActivity extends AppCompatActivity {

    private MapThread mapThread;
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

        mapThread = new MapThread(this);
        appView = new AppView(this, mapThread);

        setContentView(appView);

    }

    public void startLevel(String level) {
        Intent intent = new Intent(MapActivity.this, GameActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapThread.setRun(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapThread.setRun(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapThread.dispose();

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    @Override //TODO open menu
    public void onBackPressed() { startActivity(new Intent(MapActivity.this, MainMenuActivity.class)); }

}
