package com.kmortyk.canekoandthechessking.scores.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Scores {

    public class ScoreEntry{
        public int id, time, steps;
        public String mapName;

        ScoreEntry(int id, int time, int steps, String mapName) {
            this.id = id;
            this.time = time;
            this.steps = steps;
            this.mapName = mapName;
        }

    }

    private ScoresHelper dbHelper;

    private static Scores instance;

    private Scores(Context context) { dbHelper = new ScoresHelper(context); }

    public static void Initialize(Context context) { instance = new Scores(context); }

    public static Scores getInstance() {
        if(instance.dbHelper == null) {
            throw new NullPointerException("getInstance error: Scores was not initialized");
        }
        instance.dbHelper.getReadableDatabase();
        instance.dbHelper.close();
        return instance;
    }


    public void addEntry(int time, int steps, String mapName) {

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put("time", time);
        cv.put("steps", steps);
        cv.put("mapName", mapName);

        long ID = db.insert("scores", null, cv);

        Log.d("Scores ID", ID + "");
        Log.d("Scores", dbHelper.getDatabaseName());

        dbHelper.close();

    }

    public ArrayList<ScoreEntry> getEntries() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("scores", null, null, null, null, null, null);
        ArrayList<ScoreEntry> scoreEntries = new ArrayList<>();

        if(cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex("_id");
            int timeIndex = cursor.getColumnIndex("time");
            int stepsIndex = cursor.getColumnIndex("steps");
            int mapNameIndex = cursor.getColumnIndex("mapName");

            do{

                int id = cursor.getInt(idIndex);
                int time = cursor.getInt(timeIndex);
                int steps = cursor.getInt(stepsIndex);
                String mapName = cursor.getString(mapNameIndex);

                scoreEntries.add(new ScoreEntry(id, time, steps, mapName));

            }while(cursor.moveToNext());

        }

        cursor.close();
        dbHelper.close();

        return scoreEntries;
    }

}
