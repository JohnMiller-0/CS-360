package com.snhu.cs360_project_miller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class weightTrackerDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weightTracker.db";
    private static final int VERSION = 1;
    private static weightTrackerDBHelper instance;

    private weightTrackerDBHelper (Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static synchronized weightTrackerDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new weightTrackerDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    public static final class WeightTable {
        private static final String TABLE = "weights";
        private static final String ID = "_id";
        private static final String DATE = "date";
        private static final String WEIGHT = "weight";
        private static final String USERNAME = "username";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + WeightTable.TABLE + " (" +
                WeightTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeightTable.DATE + " TEXT NOT NULL, " +
                WeightTable.WEIGHT + " INTEGER NOT NULL, " +
                WeightTable.USERNAME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + WeightTable.TABLE);
        onCreate(db);
    }

    public long addWeighIn (int weight, String username){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WeightTable.WEIGHT, weight);
        values.put(WeightTable.DATE, new Date().toString());
        values.put(WeightTable.USERNAME, username);

        return db.insert(WeightTable.TABLE, null, values);
    }

    private Cursor getAllWeights(String username) {
        SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery("SELECT * FROM " + WeightTable.TABLE +
                            " WHERE " + WeightTable.USERNAME + " = ?" +
                            " ORDER BY " + WeightTable.DATE + " DESC",
                    new String[]{username});
        }
    public List<WeightEntry> getWeightEntries(String username) {
        List<WeightEntry> weightEntries = new ArrayList<>();
        Cursor cursor = getAllWeights(username);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(WeightTable.ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(WeightTable.DATE));
                int weight = cursor.getInt(cursor.getColumnIndexOrThrow(WeightTable.WEIGHT));
                weightEntries.add(new WeightEntry(id, date, weight));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return weightEntries;
    }
    public boolean deleteWeight(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(WeightTable.TABLE, WeightTable.ID + "=" + id, null) > 0;
    }

    public boolean editWeight(long id, int weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WeightTable.WEIGHT, weight);
        return db.update(WeightTable.TABLE, values, WeightTable.ID + "=" + id, null) > 0;
    }

}
