package com.cvirn.mobileapp2.survey.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cvirn.mobileapp2.survey.model.Message;

import java.util.ArrayList;

import dbhelper.DbHelper;

/**
 * Created by janko on 4/28/14.
 */
public class MessageDS {

    private static final String TAG="MessageSource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns={

            DbHelper.M_ID,
            DbHelper.M_STATUS,
            DbHelper.M_TITLE,
            DbHelper.M_BODY,
            DbHelper.M_SID,
            DbHelper.M_FROM

    };


    public MessageDS(Context context){

        dbhelper=new DbHelper(context);
    }

    public long createMessage(Message m){

        ContentValues values=new ContentValues();
        values.put(DbHelper.M_BODY,m.getBody());
        values.put(DbHelper.M_SID, m.getMid());
        values.put(DbHelper.M_TITLE, m.getTitle());
        values.put(DbHelper.M_STATUS,m.getStatus());
        values.put(DbHelper.M_FROM,m.getFrom());

        long l=database.insert(DbHelper.T_MESSAGE,null,values);
        Log.d(TAG , "Create message:"+l);
        return l;
    }

    public ArrayList<Message> findAll(){

        ArrayList<Message> holder=new ArrayList<Message>();

        Cursor cursor=database.query(DbHelper.T_MESSAGE,allColumns,null,null,null,null,null);

        while(cursor.moveToNext()){

            Message m=new Message();
            m.setMid(cursor.getString(cursor.getColumnIndex(DbHelper.M_SID)));
            m.setBody(cursor.getString(cursor.getColumnIndex(DbHelper.M_BODY)));
            m.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.M_TITLE)));
            m.setStatus(cursor.getString(cursor.getColumnIndex(DbHelper.M_STATUS)));
            m.setFrom(cursor.getString(cursor.getColumnIndex(DbHelper.M_FROM)));
            holder.add(m);

        }


        return holder;




    }

    public int countMessages(){


        int i=0;

        Cursor cursor=database.query(DbHelper.T_MESSAGE,allColumns,null,null,null,null,null);

        if (cursor!=null){

            i=cursor.getCount();

        }


        return i;




    }

    public Message findMessage(String sid){



        Cursor cursor=database.query(DbHelper.T_MESSAGE,allColumns,DbHelper.M_SID+"=?",
                new String[]{sid},null,
                null,null);

        Message m = new Message();
        if (cursor.getCount()!=0) {

            cursor.moveToFirst();
            m.setMid(cursor.getString(cursor.getColumnIndex(DbHelper.M_SID)));
            m.setBody(cursor.getString(cursor.getColumnIndex(DbHelper.M_BODY)));
            m.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.M_TITLE)));
            m.setStatus(cursor.getString(cursor.getColumnIndex(DbHelper.M_STATUS)));
            m.setFrom(cursor.getString(cursor.getColumnIndex(DbHelper.M_FROM)));

        }



        return m;




    }








    public void open(){

        Log.d(TAG, "db opened");
        database=dbhelper.getWritableDatabase();

    }

    public void close(){

        Log.d(TAG, "db close");
        database.close();
    }

    public void deleteAll(){

        Log.d(TAG,"Deleting message data");
        database.delete(DbHelper.T_MESSAGE,null,null);
    }
}
