package com.snhu.cs360_project_miller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class loginDBHelper extends SQLiteOpenHelper {
    private static String currentEmail = null;
    private static final String DATABASE_NAME = "login.db";
    private static final int VERSION = 1;
    private static loginDBHelper instance;

    private loginDBHelper (Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static synchronized loginDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new loginDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    private static final class LoginTable {
        private static final String TABLE = "login";
        private static final String ID = "_id";
        private static final String USERNAME = "username";
        private static final String PASSWORD = "password";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LoginTable.TABLE + " (" +
                LoginTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LoginTable.USERNAME + " text, " +
                LoginTable.PASSWORD + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + LoginTable.TABLE);
        onCreate(db);
    }


    // Insert User
    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LoginTable.USERNAME, email);
        values.put(LoginTable.PASSWORD, password);

        long result = db.insert(LoginTable.TABLE, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Check if user exists
    public boolean isUserValid(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + LoginTable.TABLE + " WHERE " +
                        LoginTable.USERNAME + "=? AND " + LoginTable.PASSWORD + "=?",
                new String[]{email, password}
        );

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        if (isValid) {
            currentEmail = email;
        }
        return isValid;
    }

    // Delete User
    public boolean deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(LoginTable.TABLE, LoginTable.USERNAME
                + "=?", new String[]{email}) > 0;
    }

    public boolean changePassword(String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LoginTable.PASSWORD, newPassword);

        int rowsUpdated = db.update(LoginTable.TABLE, values, LoginTable.USERNAME + "=?", new String[]{currentEmail});
        return rowsUpdated > 0;
    }

        // Get current email
    public static String getCurrentEmail() {
        return currentEmail;
    }

}
