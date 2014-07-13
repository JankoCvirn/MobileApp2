package com.cvirn.mobileapp2.survey.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cvirn.mobileapp2.survey.model.Task;

import java.util.ArrayList;

import dbhelper.DbHelper;

/**
 * Created by janko on 4/28/14.
 */
public class TaskDS {

    private static final String TAG="TaskSource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns={

            DbHelper.TA_ID,
            DbHelper.TA_FORM,
            DbHelper.TA_DUE,
            DbHelper.TA_STATUS,
            DbHelper.TA_SID,
            DbHelper.TA_POI,
            DbHelper.TA_FORMID,
            DbHelper.TA_POINAME

    };


    public TaskDS(Context context){

        dbhelper=new DbHelper(context);
    }

    public long createTask(Task t){

        ContentValues values=new ContentValues();
        values.put(DbHelper.TA_DUE, t.getDue_date());
        values.put(DbHelper.TA_FORM,t.getForm());
        values.put(DbHelper.TA_POI,t.getPoi());
        values.put(DbHelper.TA_STATUS,t.getStatus());
        values.put(DbHelper.TA_SID,t.getSid());
        values.put(DbHelper.TA_FORMID,t.getForm_id());
        values.put(DbHelper.TA_POINAME,t.getPoi_name());

        long l=database.insert(DbHelper.TABLE_TASK,null,values);
        Log.d(TAG , "Create task:"+l);
        return l;
    }

    public ArrayList<Task> findAll(){

        ArrayList<Task> holder=new ArrayList<Task>();

        Cursor cursor=database.query(DbHelper.TABLE_TASK,allColumns,null,null,null,null,
                DbHelper.TA_DUE+" ASC");

        while(cursor.moveToNext()){

            Task t=new Task();
            t.setId(cursor.getString(cursor.getColumnIndex(DbHelper.TA_ID)));
            t.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.TA_SID)));
            t.setStatus(cursor.getString(cursor.getColumnIndex(DbHelper.TA_STATUS)));
            t.setDue_date(cursor.getString(cursor.getColumnIndex(DbHelper.TA_DUE)));
            t.setForm(cursor.getString(cursor.getColumnIndex(DbHelper.TA_FORM)));
            t.setPoi(cursor.getString(cursor.getColumnIndex(DbHelper.TA_POI)));
            t.setForm_id(cursor.getString(cursor.getColumnIndex(DbHelper.TA_FORMID)));
            t.setPoi_name(cursor.getString(cursor.getColumnIndex(DbHelper.TA_POINAME)));

            holder.add(t);

        }


        return holder;




    }

    public ArrayList<Task> findAllWithSort(String column){

        ArrayList<Task> holder=new ArrayList<Task>();

        Cursor cursor=database.query(DbHelper.TABLE_TASK,allColumns,null,null,null,null,
                column+" ASC");

        while(cursor.moveToNext()){

            Task t=new Task();
            t.setId(cursor.getString(cursor.getColumnIndex(DbHelper.TA_ID)));
            t.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.TA_SID)));
            t.setStatus(cursor.getString(cursor.getColumnIndex(DbHelper.TA_STATUS)));
            t.setDue_date(cursor.getString(cursor.getColumnIndex(DbHelper.TA_DUE)));
            t.setForm(cursor.getString(cursor.getColumnIndex(DbHelper.TA_FORM)));
            t.setPoi(cursor.getString(cursor.getColumnIndex(DbHelper.TA_POI)));
            t.setForm_id(cursor.getString(cursor.getColumnIndex(DbHelper.TA_FORMID)));
            t.setPoi_name(cursor.getString(cursor.getColumnIndex(DbHelper.TA_POINAME)));

            holder.add(t);

        }


        return holder;




    }
    public int countTasks(){


        int i=0;

        Cursor cursor=database.query(DbHelper.TABLE_TASK,allColumns,null,null,null,null,null);

        if (cursor!=null){

            i=cursor.getCount();

        }


        return i;




    }

    public Task findTask(String sid){



        Cursor cursor=database.query(DbHelper.TABLE_TASK,allColumns,DbHelper.TA_SID+"=?",
                new String[]{sid},null,
                null,null);

        Task t=new Task();
        if (cursor.getCount()!=0) {

            cursor.moveToFirst();
            t.setId(cursor.getString(cursor.getColumnIndex(DbHelper.TA_ID)));
            t.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.TA_SID)));
            t.setStatus(cursor.getString(cursor.getColumnIndex(DbHelper.TA_STATUS)));
            t.setDue_date(cursor.getString(cursor.getColumnIndex(DbHelper.TA_DUE)));
            t.setForm(cursor.getString(cursor.getColumnIndex(DbHelper.TA_FORM)));
            t.setPoi(cursor.getString(cursor.getColumnIndex(DbHelper.TA_POI)));

        }



        return t;




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

        Log.d(TAG,"Deleting task data");
        database.delete(DbHelper.TABLE_TASK,null,null);
    }
}
