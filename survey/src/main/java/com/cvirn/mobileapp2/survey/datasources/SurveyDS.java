package com.cvirn.mobileapp2.survey.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cvirn.mobileapp2.survey.model.Survey;

import java.util.ArrayList;

import dbhelper.DbHelper;

/**
 * Created by janko on 5/21/14.
 */
public class SurveyDS {

    private static final String TAG="SurveySource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns={

            DbHelper.SURVEY_ID,
            DbHelper.SURVEY_NAME,
            DbHelper.SURVEY_SID,
            DbHelper.SURVEY_DESC,
            DbHelper.SURVEY_ENABLED


    };


    public SurveyDS(Context context){

        dbhelper=new DbHelper(context);
    }

    public long createSurvey(Survey m){

        ContentValues values=new ContentValues();
        values.put(DbHelper.SURVEY_SID,m.getSid());
        values.put(DbHelper.SURVEY_NAME, m.getName());
        values.put(DbHelper.SURVEY_DESC, m.getDesc());
        values.put(DbHelper.SURVEY_ENABLED,m.getEnabled());


        long l=database.insert(DbHelper.TABLE_SURVEY,null,values);
        Log.d(TAG, "Create survey:" + l);
        return l;
    }

    public ArrayList<Survey> findAll(){

        ArrayList<Survey> holder=new ArrayList<Survey>();

        Cursor cursor=database.query(DbHelper.TABLE_SURVEY,allColumns,null,null,null,null,null);

        while(cursor.moveToNext()){

           Survey m=new Survey();
            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.SURVEY_SID)));
            m.setName(cursor.getString(cursor.getColumnIndex(DbHelper.SURVEY_NAME)));
            m.setDesc(cursor.getString(cursor.getColumnIndex(DbHelper.SURVEY_DESC)));
            m.setEnabled(cursor.getString(cursor.getColumnIndex(DbHelper.SURVEY_ENABLED)));

            holder.add(m);

        }


        return holder;




    }

    public Survey findSurvey(String sid){



        Cursor cursor=database.query(DbHelper.TABLE_SURVEY,allColumns,DbHelper.SURVEY_SID+"=?",
                new String[]{sid},null,
                null,null);

        Survey m = new Survey();
        if (cursor.getCount()!=0) {

            cursor.moveToFirst();
            m.setSid(sid);
            m.setName(cursor.getString(cursor.getColumnIndex(DbHelper.SURVEY_NAME)));
            m.setDesc(cursor.getString(cursor.getColumnIndex(DbHelper.SURVEY_DESC)));
            m.setEnabled(cursor.getString(cursor.getColumnIndex(DbHelper.SURVEY_ENABLED)));


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
        database.delete(DbHelper.TABLE_SURVEY,null,null);
    }
}
