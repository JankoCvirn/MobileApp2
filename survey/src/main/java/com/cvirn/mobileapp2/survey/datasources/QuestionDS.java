package com.cvirn.mobileapp2.survey.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cvirn.mobileapp2.survey.model.Question;

import java.util.ArrayList;

import dbhelper.DbHelper;

/**
 * Created by janko on 5/21/14.
 */
public class QuestionDS {

    private static final String TAG="QuestionSource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns={

            DbHelper.SO_ID,
            DbHelper.SO_SID,
            DbHelper.SO_TYPE,
            DbHelper.SO_TITLE,
            DbHelper.SO_ORDER,
            DbHelper.SO_PARAM,
            DbHelper.SO_PARENT,
            DbHelper.SO_SKIP,
            DbHelper.SO_ANSWER,
            DbHelper.SO_POI


    };


    public QuestionDS(Context context){

        dbhelper=new DbHelper(context);
    }

    public long createQuestion(Question m){

        ContentValues values=new ContentValues();
        values.put(DbHelper.SO_SID,m.getSid());
        values.put(DbHelper.SO_ORDER, m.getOrder());
        values.put(DbHelper.SO_PARAM, m.getParam());
        values.put(DbHelper.SO_PARENT,m.getParentid());
        values.put(DbHelper.SO_SKIP,m.getSid());
        values.put(DbHelper.SO_TITLE,m.getTitle());
        values.put(DbHelper.SO_TYPE,m.getType());


        long l=database.insert(DbHelper.TABLE_QUESTIONS,null,values);
        Log.d(TAG, "Create question:" + l);
        return l;
    }

    public ArrayList<Question> findAll(String parentid){

        ArrayList<Question> holder=new ArrayList<Question>();

        Cursor cursor=database.query(DbHelper.TABLE_QUESTIONS,allColumns,DbHelper.SO_PARENT+"=?",
                new String[] {parentid},null,
                null,DbHelper.SO_ORDER+" ASC");

        while(cursor.moveToNext()){

            Question m=new Question();
            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.SO_SID)));
            m.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.SO_TITLE)));
            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.SO_TYPE)));
            m.setOrder(cursor.getString(cursor.getColumnIndex(DbHelper.SO_ORDER)));
            m.setParentid(cursor.getString(cursor.getColumnIndex(DbHelper.SO_PARENT)));
            m.setParam(cursor.getString(cursor.getColumnIndex(DbHelper.SO_PARAM)));
            m.setSkip(cursor.getString(cursor.getColumnIndex(DbHelper.SO_SKIP)));
            m.setAnswer(cursor.getString(cursor.getColumnIndex(DbHelper.SO_ANSWER)));
            m.setPoiid(cursor.getString(cursor.getColumnIndex(DbHelper.SO_POI)));

            holder.add(m);

        }


        return holder;




    }

    public int insertAnswer(String answer,String sid,String type,String poid){

        ContentValues values=new ContentValues();
        values.put(DbHelper.SO_ANSWER,answer);
        int row=database.update(DbHelper.TABLE_QUESTIONS,values,
                DbHelper.SO_TYPE+"=? AND "+DbHelper.SO_PARENT+"=?",
                new String[]{ type,poid } );

        return row;


    }


    public Question findQuestion(String type,String parentid){

        Log.d(TAG,type);
        Cursor cursor=database.query(DbHelper.TABLE_QUESTIONS,allColumns,
                DbHelper.SO_TYPE+"=?",new String[]{type},null,
                null,null);

        Log.d(TAG,""+cursor.getCount());
        Question m = new Question();
        if (cursor.getCount()!=0) {

            cursor.moveToFirst();


            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.SO_SID)));
            m.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.SO_TITLE)));
            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.SO_TYPE)));
            m.setOrder(cursor.getString(cursor.getColumnIndex(DbHelper.SO_ORDER)));
            m.setParentid(cursor.getString(cursor.getColumnIndex(DbHelper.SO_PARENT)));
            m.setParam(cursor.getString(cursor.getColumnIndex(DbHelper.SO_PARAM)));
            m.setSkip(cursor.getString(cursor.getColumnIndex(DbHelper.SO_SKIP)));


        }


        return m;




    }

    public Question findQuestion(int order){

        Log.d(TAG,"searching for order:"+order);
        String type=""+order;
                Cursor cursor=database.query(DbHelper.TABLE_QUESTIONS,allColumns,
                DbHelper.SO_ORDER+"=?",new String[]{type},null,
                null,null);

        Log.d(TAG,""+cursor.getCount());
        Question m = new Question();
        if (cursor.getCount()!=0) {

            cursor.moveToFirst();


            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.SO_SID)));
            m.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.SO_TITLE)));
            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.SO_TYPE)));
            m.setOrder(cursor.getString(cursor.getColumnIndex(DbHelper.SO_ORDER)));
            m.setParentid(cursor.getString(cursor.getColumnIndex(DbHelper.SO_PARENT)));
            m.setParam(cursor.getString(cursor.getColumnIndex(DbHelper.SO_PARAM)));
            m.setSkip(cursor.getString(cursor.getColumnIndex(DbHelper.SO_SKIP)));


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
        database.delete(DbHelper.TABLE_QUESTIONS,null,null);
    }
}
