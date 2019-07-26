package com.kmortyk.canekoandthechessking.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "DataHelper";

    private static String DB_PATH;
    private static String DB_NAME ="game.db";
    private static int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;

    private final Context context;

    public DataHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        // if SDK_INT < 4.1 use:
        // DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";

        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.context = context;
    }

    public void createDataBase() {
        Log.d(LOG_TAG, "create data base");

        if(exists()) {
            context.deleteDatabase(DB_PATH + DB_NAME);
        }

        this.getReadableDatabase();
        close();
        try { copyDataBase(); }
        catch (IOException e) { Log.e(LOG_TAG, "createDataBase error: unable to copy data base"); }
    }

    private boolean exists() { return new File(DB_PATH + DB_NAME).exists(); }

    private void copyDataBase() throws IOException {
        InputStream input = context.getAssets().open("databases/" + DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream output = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer))>0) { output.write(buffer, 0, length); }
        output.flush(); output.close(); input.close();
    }

    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        Log.d(LOG_TAG, mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if(mDataBase != null) { mDataBase.close(); }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

}
