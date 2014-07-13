package com.cvirn.mobileapp2.survey.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cvirn.mobileapp2.survey.model.POI;

import java.util.ArrayList;

import dbhelper.DbHelper;

/**
 * Created by janko on 4/28/14.
 */
public class PoiDS {

    private static final String TAG="POISource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns={

            DbHelper.POI_ID,
            DbHelper.POI_NAME,
            DbHelper.POI_LAT,
            DbHelper.POI_LNG,
            DbHelper.POI_TYPE,
            DbHelper.POI_STATUS,
            DbHelper.POI_SEGMENT,
            DbHelper.POI_SID,
            DbHelper.POI_ADDRESS,
            DbHelper.POI_CITY,
            DbHelper.POI_PHONE,
            DbHelper.POI_PIc,
            DbHelper.POI_CDATE,
            DbHelper.POI_CREATED,
            DbHelper.POI_MODATE,
            DbHelper.POI_BKEY

    };


    public PoiDS(Context context){

        dbhelper=new DbHelper(context);
    }

    public long createPoi(POI m){

        ContentValues values=new ContentValues();
        values.put(DbHelper.POI_NAME,m.getName());
        values.put(DbHelper.POI_LAT,m.getLat());
        values.put(DbHelper.POI_LNG,m.getLng());
        values.put(DbHelper.POI_TYPE,m.getType());
        values.put(DbHelper.POI_SID,m.getSid());
        values.put(DbHelper.POI_STATUS,m.getStatus());
        values.put(DbHelper.POI_SEGMENT,m.getSegment());
        values.put(DbHelper.POI_ADDRESS,m.getAddress());
        values.put(DbHelper.POI_CITY,m.getCity());
        values.put(DbHelper.POI_CDATE,m.getCreate_time());
        values.put(DbHelper.POI_CREATED,m.getCreated_by());
        values.put(DbHelper.POI_MODATE,m.getMod_time());
        values.put(DbHelper.POI_PIc,m.getPic());
        values.put(DbHelper.POI_BKEY,m.getBuis_key());
        values.put(DbHelper.POI_PHONE,m.getPhone());



        long l=database.insert(DbHelper.TABLE_POI,null,values);
        Log.d(TAG , "Create poi:"+l);
        return l;
    }

    public ArrayList<POI> findAll(){

        ArrayList<POI> holder=new ArrayList<POI>();

        Cursor cursor=database.query(DbHelper.TABLE_POI,allColumns,null,null,null,null,
                DbHelper.POI_SID+" ASC");

        while(cursor.moveToNext()){

            POI m=new POI();
            m.setName(cursor.getString(cursor.getColumnIndex(DbHelper.POI_NAME)));
            m.setLat(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LAT)));
            m.setLng(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LNG)));
            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.POI_TYPE)));
            m.setAddress(cursor.getString(cursor.getColumnIndex(DbHelper.POI_ADDRESS)));
            m.setCity(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CITY)));
            m.setPhone(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PHONE)));
            m.setPic(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PIc)));
            m.setBuis_key(cursor.getString(cursor.getColumnIndex(DbHelper.POI_BKEY)));
            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SID)));
            m.setCreated_by(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CREATED)));
            m.setCreate_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CDATE)));
            m.setMod_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_MODATE)));
            m.setSegment(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SEGMENT)));

            holder.add(m);

        }


        return holder;




    }

    public ArrayList<POI> findAlWithSort(String column){

        ArrayList<POI> holder=new ArrayList<POI>();

        Cursor cursor=database.query(DbHelper.TABLE_POI,allColumns,null,null,null,null,
                column+" ASC");

        while(cursor.moveToNext()){

            POI m=new POI();
            m.setName(cursor.getString(cursor.getColumnIndex(DbHelper.POI_NAME)));
            m.setLat(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LAT)));
            m.setLng(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LNG)));
            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.POI_TYPE)));
            m.setAddress(cursor.getString(cursor.getColumnIndex(DbHelper.POI_ADDRESS)));
            m.setCity(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CITY)));
            m.setPhone(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PHONE)));
            m.setPic(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PIc)));
            m.setBuis_key(cursor.getString(cursor.getColumnIndex(DbHelper.POI_BKEY)));
            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SID)));
            m.setCreated_by(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CREATED)));
            m.setCreate_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CDATE)));
            m.setMod_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_MODATE)));
            m.setSegment(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SEGMENT)));

            holder.add(m);

        }


        return holder;




    }

    public POI findPOI(String name){



        Cursor cursor=database.query(DbHelper.TABLE_POI,allColumns,DbHelper.POI_NAME+"=?",
                new String[]{name},null,
                null,null);

        POI m = new POI();
        if (cursor.getCount()!=0) {

            cursor.moveToFirst();
            m.setName(name);

            m.setLat(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LAT)));
            m.setLng(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LNG)));
            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.POI_TYPE)));
            m.setAddress(cursor.getString(cursor.getColumnIndex(DbHelper.POI_ADDRESS)));
            m.setCity(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CITY)));
            m.setPhone(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PHONE)));
            m.setPic(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PIc)));
            m.setBuis_key(cursor.getString(cursor.getColumnIndex(DbHelper.POI_BKEY)));
            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SID)));
            m.setCreated_by(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CREATED)));
            m.setCreate_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CDATE)));
            m.setMod_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_MODATE)));
            m.setSegment(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SEGMENT)));


        }



        return m;




    }
    public POI findPOIBySID(String id){



        Cursor cursor=database.query(DbHelper.TABLE_POI,allColumns,DbHelper.POI_SID+"=?",
                new String[]{id},null,
                null,null);

        POI m = new POI();
        if (cursor.getCount()!=0) {

            cursor.moveToFirst();
            m.setName(cursor.getString(cursor.getColumnIndex(DbHelper.POI_NAME)));

            m.setLat(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LAT)));
            m.setLng(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LNG)));
            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.POI_TYPE)));
            m.setAddress(cursor.getString(cursor.getColumnIndex(DbHelper.POI_ADDRESS)));
            m.setCity(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CITY)));
            m.setPhone(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PHONE)));
            m.setPic(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PIc)));
            m.setBuis_key(cursor.getString(cursor.getColumnIndex(DbHelper.POI_BKEY)));
            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SID)));
            m.setCreated_by(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CREATED)));
            m.setCreate_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CDATE)));
            m.setMod_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_MODATE)));
            m.setSegment(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SEGMENT)));


        }



        return m;




    }

    public ArrayList<POI> searchPOI(String name){



        Cursor cursor=database.query(DbHelper.TABLE_POI,allColumns,DbHelper.POI_NAME+" LIKE ?",
                new String[]{"%"+name+"%"},null,
                null,null);

        ArrayList<POI> holder=new ArrayList<POI>();

        while(cursor.moveToNext()){
            POI m = new POI();
            m.setName(cursor.getString(cursor.getColumnIndex(DbHelper.POI_NAME)));

            m.setLat(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LAT)));
            m.setLng(cursor.getString(cursor.getColumnIndex(DbHelper.POI_LNG)));
            m.setType(cursor.getString(cursor.getColumnIndex(DbHelper.POI_TYPE)));
            m.setAddress(cursor.getString(cursor.getColumnIndex(DbHelper.POI_ADDRESS)));
            m.setCity(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CITY)));
            m.setPhone(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PHONE)));
            m.setPic(cursor.getString(cursor.getColumnIndex(DbHelper.POI_PIc)));
            m.setBuis_key(cursor.getString(cursor.getColumnIndex(DbHelper.POI_BKEY)));
            m.setSid(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SID)));
            m.setCreated_by(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CREATED)));
            m.setCreate_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_CDATE)));
            m.setMod_time(cursor.getString(cursor.getColumnIndex(DbHelper.POI_MODATE)));
            m.setSegment(cursor.getString(cursor.getColumnIndex(DbHelper.POI_SEGMENT)));

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

        Log.d(TAG,"Deleting message data");
        database.delete(DbHelper.TABLE_POI,null,null);
    }
}
