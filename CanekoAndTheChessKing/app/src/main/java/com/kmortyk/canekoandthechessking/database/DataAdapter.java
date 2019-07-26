package com.kmortyk.canekoandthechessking.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataAdapter
{
    private static final String LOG_TAG = "DataAdapter";

    private SQLiteDatabase db;
    private DataHelper dbHelper;

    public DataAdapter(Context context) {
        dbHelper = new DataHelper(context);
    }

    public void createDatabase() { dbHelper.createDataBase(); }

    public void open() throws SQLException {
        try {
            if(!dbHelper.openDataBase()) throw new SQLException("DataAdapter: Could not open database.");
            db = dbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException) {
            Log.e(LOG_TAG, "open ->"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public void close() { dbHelper.close(); }

    public Cursor getTileData(int tileId)             { return getData("SELECT * FROM tiles WHERE id LIKE " + tileId); }

    public Cursor getDecorationData(int decorationId) { return getData("SELECT * FROM decorations WHERE id LIKE " + decorationId); }

    public Cursor getItemData(int itemId)             { return getData("SELECT * FROM items WHERE id LIKE " + itemId); }

    private Cursor getData(String sql) { return db.rawQuery(sql, null); }


}