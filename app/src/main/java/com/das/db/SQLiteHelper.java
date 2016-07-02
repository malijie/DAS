package com.das.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by malijie on 2016/6/30.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    public SQLiteHelper(Context context) {
        super(context, DBConfig.DATABASE_NAME,null , DBConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(SQLContainer.getCreateNodeTableSQL());
//        db.execSQL(SQLContainer.getCreatePathTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLContainer.getDropNodeTableSQL());
        db.execSQL(SQLContainer.getDropPathTableSQL());
        onCreate(db);

    }
}
