package com.example.listphone_tts.unity;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Mydatabase extends SQLiteOpenHelper {
    public Mydatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // truy vấn ko trả kết quả
    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    // truy vấn  trả kết quả
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }
    // delete all record from table
    public void Deleteallrecord(String table){
        String sql = "DELETE FROM "+table;
        queryData(sql);
    }
    // hàm delete 1 record trong table
    public void deleteonerecord(String hoten,String table){
        String sql = "DELETE FROM " + table + " WHERE hoten = '" + hoten + "'";
        queryData(sql);
    }
    // hàm add 1 record
    public void addperson(String name,String sdt){
       // queryData("INSERT INTO " + table + "  VALUES('" + person.getName()+ "','" + person.getPhonenumber() + "')");
        String sql = "INSERT INTO listphone VALUES('" + name +"','" +  sdt + "')";
        queryData(sql);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
