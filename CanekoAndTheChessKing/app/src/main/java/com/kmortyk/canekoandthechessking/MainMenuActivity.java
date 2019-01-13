package com.kmortyk.canekoandthechessking;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.kmortyk.canekoandthechessking.menu.MenuButton;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.scores.ScoresActivity;
import com.kmortyk.canekoandthechessking.scores.database.Scores;

public class MainMenuActivity extends AppCompatActivity {

    final private static boolean DEBUG_MODE = true;

    MenuButton start, scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // no title bar, full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        ActionBar ab = getSupportActionBar();
        if(ab != null) ab.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ResourceManager.Initialize(getApplicationContext());
        Scores.Initialize(getApplicationContext());

        if(DEBUG_MODE) {
            //Intent intent = new Intent(MainMenuActivity.this, MapActivity.class);
            //startActivity(intent);

            startLevel("map1");
        }

        start = new MenuButton(findViewById(R.id.start_textView), () -> {
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            startActivity(intent);
        });

        scores = new MenuButton(findViewById(R.id.options_textView), () -> {
            Intent intent = new Intent(MainMenuActivity.this, ScoresActivity.class);
            startActivity(intent);
        });

        Button button = new Button(this);

        /*MenuButton credits = new MenuButton(findViewById(R.id.credits_textView), () -> {
            @Override
            public void run() {
                Toast.makeText(MainMenuActivity.this, "Credits", Toast.LENGTH_SHORT).show();
            }
        });*/


    }

    @Override
    protected void onPause() {
        super.onPause();
        start.fadeIn();
        scores.fadeIn();
    }

    public void startLevel(String level) {
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

}