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
public class AnswersDS {

    private static final String TAG="AnswerSource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns={

            DbHelper.AN_ID,
            DbHelper.AN_ANSWER,
            DbHelper.AN_PARENT,
            DbHelper.AN_SID,
            DbHelper.AN_TYPE



    };


    public AnswersDS(Context context){

        dbhelper=new DbHelper(context);
    }

    public long createAnswer(Question m){

        ContentValues values=new ContentValues();
        values.put(DbHelper.AN_SID,m.getSid());
        values.put(DbHelper.AN_PARENT, m.getParentid());
        values.put(DbHelper.AN_TYPE, m.getType());
        values.put(DbHelper.AN_ANSWER,m.getAnswer());


        long l=database.insert(DbHelper.TABLE_ANSWERS,null,values);
        Log.d(TAG, "Create answer:" + l);
        return l;
    }

    public ArrayList<Question> findAll(){

        ArrayList<Question> holder=new ArrayList<Question>();

        Cursor  cursor = database.query(DbHelper.TABLE_ANSWERS,allColumns,null,
                null,null,
                null,null);
        Log.d(TAG,"count:"+cursor.getCount());

        if (cursor.getCount()!=0){

        while(cursor.moveToNext()){

            Question m=new Question();
            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.AN_SID)));

            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.AN_TYPE)));

            m.setParentid(cursor.getString(cursor.getColumnIndex(DbHelper.AN_PARENT)));

            m.setAnswer(cursor.getString(cursor.getColumnIndex(DbHelper.AN_ANSWER)));


            holder.add(m);

        }}


        return holder;




    }

    public int insertAnswer(String answer,String sid,String type,String poid){

        ContentValues values=new ContentValues();
        values.put(DbHelper.AN_ANSWER,answer);
        int row=database.update(DbHelper.TABLE_ANSWERS,values,
                DbHelper.AN_TYPE+"=? AND "+DbHelper.AN_PARENT+"=?",
                new String[]{ type,poid } );

        return row;


    }
    public ArrayList<Question> findBypoiSID(String poisid){

        ArrayList<Question> holder=new ArrayList<Question>();

        int poiid=Integer.parseInt(poisid);

        Cursor cursor=database.rawQuery("SELECT * FROM ANSWERS WHERE parentid ="+poiid+"",null);


        if (cursor!=null &cursor.getCount()>0){

            Log.d(TAG,""+cursor.getCount());
            if (cursor.moveToFirst()){

                do {
                    Log.d(TAG,"next cursor");
                    Question m=new Question();
                    m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.AN_SID)));

                    m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.AN_TYPE)));

                    m.setParentid(cursor.getString(cursor.getColumnIndex(DbHelper.AN_PARENT)));

                    m.setAnswer(cursor.getString(cursor.getColumnIndex(DbHelper.AN_ANSWER)));


                    holder.add(m);
                }
                while (cursor.moveToNext());
            }
            /*
            while(cursor.moveToNext()){

                Question m=new Question();
                m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.AN_SID)));

                m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.AN_TYPE)));

                m.setParentid(cursor.getString(cursor.getColumnIndex(DbHelper.AN_PARENT)));

                m.setAnswer(cursor.getString(cursor.getColumnIndex(DbHelper.AN_ANSWER)));


                holder.add(m);*/

            }


        return holder;




    }


    public Question findAnswer(String type,String parentid){

        Log.d(TAG,type);
        Cursor cursor=database.query(DbHelper.TABLE_ANSWERS,allColumns,
                DbHelper.AN_TYPE+"=? AND "+DbHelper.AN_PARENT+"=? ",new String[]{type,parentid},null,
                null,null);

        Log.d(TAG,""+cursor.getCount());
        Question m = new Question();
        if (cursor.getCount()!=0) {

            cursor.moveToFirst();


            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.AN_SID)));

            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.AN_TYPE)));
            m.setAnswer(cursor.getString(cursor.getColumnIndex(DbHelper.AN_ANSWER)));
            m.setParentid(cursor.getString(cursor.getColumnIndex(DbHelper.AN_PARENT)));



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
        database.delete(DbHelper.TABLE_ANSWERS,null,null);
    }
}
