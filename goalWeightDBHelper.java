package com.snhu.cs360_project_miller;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class goalWeightDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "goalWeight.db";
    private static final int VERSION = 1;
    private static goalWeightDBHelper instance;

    private goalWeightDBHelper (Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static synchronized goalWeightDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new goalWeightDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    public static final class GoalWeightTable {
        private static final String TABLE = "goalWeight";
        private static final String ID = "_id";
        private static final String GOAL = "goal";
        private static final String GOAL_TYPE = "goalType";
        private static final String USERNAME = "username";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + GoalWeightTable.TABLE + " (" +
                GoalWeightTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GoalWeightTable.GOAL + " REAL NOT NULL ," +
                GoalWeightTable.GOAL_TYPE + " TEXT NOT NULL ," +
                GoalWeightTable.USERNAME + " INTEGER NOT NULL) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GoalWeightTable.TABLE);
        onCreate(db);
    }

    public long insertGoal(int goal, String goalType, String username) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GoalWeightTable.GOAL, goal);
        values.put(GoalWeightTable.GOAL_TYPE, goalType);
        values.put(GoalWeightTable.USERNAME, username);

        return db.insert(GoalWeightTable.TABLE, null, values);
    }

    public GoalWeightEntry getGoalWeight(String username) {
        SQLiteDatabase db = getReadableDatabase();
        GoalWeightEntry goalEntry = null;

        Cursor cursor = db.rawQuery("SELECT goal, goalType FROM " + GoalWeightTable.TABLE + " WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            int goalWeight = cursor.getInt(cursor.getColumnIndexOrThrow("goal"));
            String goalType = cursor.getString(cursor.getColumnIndexOrThrow("goalType"));
            goalEntry = new GoalWeightEntry(goalWeight, goalType);
        }

        cursor.close();
        return goalEntry;
    }
}
