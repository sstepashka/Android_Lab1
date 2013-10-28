package com.example.lab_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper {
	public static final String KEY_NAME = "text";
    public static final String KEY_ROWID = "_id";
    private static final String DATABASE_NAME = "database";
    private static final String DATABASE_TABLE = "Notes";
    private static final int DATABASE_VERSION = 8;
    public static boolean isUpgraded;
    private boolean isReady = false;

    private DatabaseHelper mDbHelper;
    static private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "CREATE TABLE "	
                    + DATABASE_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT NOT NULL);";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

            DatabaseHelper(Context context) {
                    super(context, DATABASE_NAME, null, DATABASE_VERSION);

            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                    db.execSQL(DATABASE_CREATE);
                    isUpgraded = true;
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                    db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
                    onCreate(db);
            }
    }

    static public void initMetroTable() {
    	createRecord("1");
    	createRecord("2");
    	createRecord("3");
    }

    public DataBaseHelper(Context ctx) {
            this.mCtx = ctx;
    }

    public DataBaseHelper open() throws SQLException {
            isUpgraded = false;
            mDbHelper = new DatabaseHelper(mCtx);
            mDb = mDbHelper.getWritableDatabase();
            if (isUpgraded)
                    initMetroTable();
            isReady = true;
            return this;
    }

    public void close() {
            mDbHelper.close();
    }

    static public long createRecord(String text) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_NAME, text);
            return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    synchronized public Cursor fetchAll() {
        Cursor ccc = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
                        KEY_NAME},"",
                        null, null, null, KEY_NAME, null);
        return ccc;
    }
    
    synchronized public void updateRecord(long id, String text) {
    	String strFilter = KEY_ROWID + "=" + id;
    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, text);
    	mDb.update(DATABASE_TABLE, values, strFilter, null);
    }
    

    synchronized public Cursor fetchRecordsByQuery(String query) {
            Cursor ccc = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
                            KEY_NAME}, KEY_NAME + " LIKE" + "'" + query + "%'",
                            null, null, null, KEY_NAME, null);
            return ccc;
    }
    
    public Cursor getById(long id) {
        Cursor ccc = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
                        KEY_NAME},
                        KEY_ROWID + "=" + Long.toString(id), null, null, null, null,
                        null);
        return ccc;
    }
    
    public void removeItem(long id) {
    	String strFilter = KEY_ROWID + "=" + id;
    	mDb.delete(DATABASE_TABLE, strFilter, null);
    }
    
    public boolean isReady() {
            return isReady;
    }

}
