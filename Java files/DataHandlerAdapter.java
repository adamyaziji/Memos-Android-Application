package com.example.adamyaziji.reminderapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by adamyaziji on 19/04/2015.
 */
public class DataHandlerAdapter {

    static DataHandler dataHandler;

    public DataHandlerAdapter(Context context) {
        dataHandler = new DataHandler(context);
    }

    public long insertData(String name, String date, String urgency) {

        SQLiteDatabase sqLiteDatabase = dataHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataHandler.MEMO_NAME, name);
        contentValues.put(DataHandler.MEMO_DATE, date);
        contentValues.put(DataHandler.MEMO_URGENCY, urgency);
        long id = sqLiteDatabase.insert(DataHandler.TABLE_NAME, null, contentValues);
        return id;
    }

    class DataHandler extends SQLiteOpenHelper {
        private static final String DATA_BASE_NAME = "Memo Database";
        private static final String TABLE_NAME = "Memotable";
        private static final int DATABASE_VERSION = 2;
        private static final String MEMO_ID = "_id";
        public static final String MEMO_NAME = "Memoname";
        public static final String MEMO_DATE = "Memodate";
        public static final String MEMO_URGENCY = "Memourgency";
        public String[] columns = {MEMO_ID, MEMO_NAME, MEMO_DATE, MEMO_URGENCY};
        SQLiteDatabase db;
        CreateMemo memo = new CreateMemo();

        private static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + " ( "
                + MEMO_ID + " integer primary key autoincrement, "
                + MEMO_NAME + " text, "
                + MEMO_DATE + " text, "
                + MEMO_URGENCY + " text)";

        public DataHandler(Context context) {
            super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
        }

        public DataHandler open() {
            dataHandler.getWritableDatabase();
            return this;
        }

        public Cursor getallRows() {
            SQLiteDatabase db = this.getReadableDatabase();
            Log.d("DataBase", "Get rows");
            Cursor c = db.query(DataHandler.TABLE_NAME, columns, null, null, null, null, null, null);
            db.rawQuery("SELECT * FROM Memotable", null);
            if (c != null) {
                c.moveToFirst();
            }
            return c;
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE " + TABLE_NAME + "IF EXISTS");
            onCreate(db);
        }

        public void deleteAllDB() {

            SQLiteDatabase db = this.getReadableDatabase();
            db.delete("Memotable", null, null);

        }

        public int deleteRow() {
            SQLiteDatabase db = dataHandler.getWritableDatabase();
            String[] whereArgs = {memo.getTaskName};
            int count = db.delete(dataHandler.TABLE_NAME, dataHandler.MEMO_NAME, whereArgs);
            return count;
        }
    }
}
