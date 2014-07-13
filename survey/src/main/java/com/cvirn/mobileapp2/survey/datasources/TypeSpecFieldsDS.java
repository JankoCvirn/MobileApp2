package com.cvirn.mobileapp2.survey.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cvirn.mobileapp2.survey.model.TypeSpecFields;

import java.util.ArrayList;

import dbhelper.DbHelper;

/**
 * Created by janko on 4/28/14.
 */
public class TypeSpecFieldsDS {

    private static final String TAG="TypeSpecSource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns={

            DbHelper.POISPEC_SEQORDER,
            DbHelper.POISPEC_SID,
            DbHelper.POISPEC_TYPE,
            DbHelper.POISPEC_NAME,
            DbHelper.POISPEC_PARENT,
            DbHelper.POISPEC_VALUE,
            DbHelper.POISPEC_ID

    };


    public TypeSpecFieldsDS(Context context){

        dbhelper=new DbHelper(context);
    }

    public long createTSFields(TypeSpecFields m){

        ContentValues values=new ContentValues();
        values.put(DbHelper.POISPEC_NAME,m.getName());
        values.put(DbHelper.POISPEC_PARENT,m.getParentid());
        values.put(DbHelper.POISPEC_SEQORDER,m.getSeq_order());
        values.put(DbHelper.POISPEC_TYPE,m.getType());
        values.put(DbHelper.POISPEC_SID,m.getSid());
        values.put(DbHelper.POISPEC_VALUE,m.getValue());

        long l=database.insert(DbHelper.TABLE_POI_SPEC,null,values);
        Log.d(TAG , "Create tsfield:"+l);
        return l;
    }

    public ArrayList<TypeSpecFields> findAll(String poiid){

        ArrayList<TypeSpecFields> holder=new ArrayList<TypeSpecFields>();

        Cursor cursor=database.query(DbHelper.TABLE_POI_SPEC,allColumns,
                DbHelper.POISPEC_PARENT+"=?",
                new String[] {poiid},null,
                null,DbHelper.POISPEC_SEQORDER+" ASC");

        while(cursor.moveToNext()){

            TypeSpecFields m=new TypeSpecFields();
            m.setName(cursor.getString(cursor.getColumnIndex(DbHelper.POISPEC_NAME)));
            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.POISPEC_SID)));
            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.POISPEC_TYPE)));
            m.setValue(cursor.getString(cursor.getColumnIndex(DbHelper.POISPEC_VALUE)));
            m.setSeq_order(cursor.getString(cursor.getColumnIndex(DbHelper.POISPEC_SEQORDER)));
            m.setParentid(cursor.getString(cursor.getColumnIndex(DbHelper.POISPEC_PARENT)));

            holder.add(m);

        }


        return holder;




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

        Log.d(TAG,"Deleting typespec data");
        database.delete(DbHelper.TABLE_POI_SPEC,null,null);
    }
}
