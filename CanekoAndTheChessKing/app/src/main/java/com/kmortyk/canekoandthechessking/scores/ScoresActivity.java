package com.kmortyk.canekoandthechessking.scores;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.database.Scores;

import java.util.ArrayList;

public class ScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // no title bar, full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        ActionBar ab = getSupportActionBar();
        if(ab != null) ab.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_scores);

        ArrayList<Scores.ScoreEntry> entries = Scores.getInstance().getEntries();

        RecyclerView recyclerView = findViewById(R.id.scores_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScoreViewAdapter scoreViewAdapter = new ScoreViewAdapter(this, entries);
        recyclerView.setAdapter(scoreViewAdapter);

    }

}
