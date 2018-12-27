package com.kmortyk.canekoandthechessking.scores.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

final class ScoreContract {

    private ScoreContract() {}

    public static class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "scores";
        public static final String TIME_COLUMN = "time";
        public static final String STEPS_COLUMN = "steps";
        public static final String MAP_NAME_COLUMN = "mapName";
    }

    public static final String SQL_CREATE_ENTRIES =
                   "CREATE TABLE IF NOT EXISTS " + ScoreEntry.TABLE_NAME + " (" +
                    ScoreEntry._ID + " INTEGER PRIMARY KEY," +
                    ScoreEntry.TIME_COLUMN     + " INTEGER," +
                    ScoreEntry.STEPS_COLUMN    + " INTEGER," +
                    ScoreEntry.MAP_NAME_COLUMN + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
                    "DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME;

}

public class ScoresHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "scores.db";

    public ScoresHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScoreContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ScoreContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
