package com.imfondof.world.rank.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * zhaishuo on 2019/12/18 10:10
 * description:
 */

public class SQLiteDBUtil extends SQLiteOpenHelper {
    public static final String NAME = "easyrank";
    public static final int VERSION = 1;//因为增加一个数据库，所以更新版本号为2

    /**
     * content上下文对象 name数据库名称 factory数据库工厂 version版本
     *
     * @param context
     */
    public SQLiteDBUtil(Context context) {
        super(context, NAME, null, VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String userSQL = "create table rankTable"
                + "(id integer primary key autoincrement,"
                + "type varchar(20),"
                + "name varchar(20),"
                + "score float,"
                + "vcount int,"
                + "dcount int,"
                + "allcount int)";
        db.execSQL(userSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
