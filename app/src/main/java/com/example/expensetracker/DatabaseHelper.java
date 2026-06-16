package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    private static final String DATABASE_NAME = "expense_tracker.db";
    private static final int DATABASE_VERSION = 3;

    // Table & Columns
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_DATE = "date";
    public static final String COL_TYPE = "type"; // "income" or "expense"

    public static final String TYPE_INCOME = "income";
    public static final String TYPE_EXPENSE = "expense";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_AMOUNT + " REAL NOT NULL, "
                + COL_DATE + " TEXT NOT NULL, "
                + COL_TYPE + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE);

        String CREATE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_NAME + " TEXT NOT NULL, "
                + COL_USERNAME + " TEXT UNIQUE NOT NULL, "
                + COL_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(CREATE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ---------- CRUD ----------

    public boolean insertTransaction(String name, double amount, String date, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,   name);
        cv.put(COL_AMOUNT, amount);
        cv.put(COL_DATE,   date);
        cv.put(COL_TYPE,   type);
        long result = db.insert(TABLE_TRANSACTIONS, null, cv);
        db.close();
        return result != -1;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COL_ID + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Transaction(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Transaction> getRecentTransactions(int limit) {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COL_ID + " DESC LIMIT " + limit, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Transaction(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Transaction> getTopExpenses(int limit) {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_TRANSACTIONS +
                        " WHERE " + COL_TYPE + "='" + TYPE_EXPENSE + "'" +
                        " ORDER BY " + COL_AMOUNT + " DESC LIMIT " + limit, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Transaction(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public double getTotalIncome() {
        return getSum(TYPE_INCOME);
    }

    public double getTotalExpense() {
        return getSum(TYPE_EXPENSE);
    }

    private double getSum(String type) {
        double sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + COL_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                        " WHERE " + COL_TYPE + "=?", new String[]{type});
        if (cursor.moveToFirst()) sum = cursor.getDouble(0);
        cursor.close();
        db.close();
        return sum;
    }

    public double getTotalBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    public boolean registerUser(String name, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USERNAME, username);
        cv.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, cv);
        db.close();
        return result != -1;
    }

    public String loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_USER_NAME + " FROM " + TABLE_USERS +
                        " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            db.close();
            return name; // returns full name on success
        }
        cursor.close();
        db.close();
        return null; // null means login failed
    }

    public void clearAllTransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TRANSACTIONS);
        db.close();
    }
}