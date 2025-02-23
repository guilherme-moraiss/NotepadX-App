package com.example.mindlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteHelper extends SQLiteOpenHelper {

    private static final String DATABASE="DATABASE";
    private  static final int VERSION=2;

    public NoteHelper(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }





    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {



        sqLiteDatabase.execSQL("create table my_table(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,description TEXT)");





    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {



        sqLiteDatabase.execSQL("drop table if exists my_table");



    }

    public void insertData(String title,String description){


        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("description",description);
        database.insert("my_table",null,values);





    }


    public Cursor showData(){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor =  sqLiteDatabase.rawQuery("select * from my_table order by id desc ",null);

        return cursor;
    }

    public void deleteData(String id){


        SQLiteDatabase database = this.getWritableDatabase();

        database.delete("my_table","id=?",new String[]{id});




    }

    public void updateData(String title,String description,String id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("description",description);

        sqLiteDatabase.update("my_table",values,"id=?",new String[]{id});






    }


}
