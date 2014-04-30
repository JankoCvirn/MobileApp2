package com.cvirn.mobileapp2.survey.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cvirn.mobileapp2.survey.model.User;

import dbhelper.DbHelper;

/**
 * Created by janko on 4/28/14.
 */
public class UserDS {

    private static final String TAG="ProvidersDataSource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns={

            DbHelper.T_KEY,
            DbHelper.T_USERNAME,
            DbHelper.T_PASSWORD,
            DbHelper.T_ID

    };


    public UserDS(Context context){

        dbhelper=new DbHelper(context);
    }

    public long createUser(User u){

        ContentValues values=new ContentValues();
        values.put(DbHelper.T_USERNAME,u.getUsername());
        values.put(DbHelper.T_PASSWORD,u.getPassword());
        values.put(DbHelper.T_KEY,u.getKey());

        long l=database.insert(DbHelper.T_USER,null,values);
        Log.d(TAG , "Create:"+l);
        return l;
    }

    public boolean checkUser(User u){


        Cursor cursor=database.query(DbHelper.T_USER,allColumns,DbHelper.T_USERNAME+"=? AND " +
                        ""+DbHelper.T_PASSWORD+"=?",
                new String[]{u.getUsername(),u.getPassword()},
                null,null,null);

        int count=cursor.getCount();
        Log.d(TAG,"Count "+count);

        Boolean check=false;

        if (count==1){

            check=true;
        }

        return check;

    }

    public boolean checkUserPresent(){


        Cursor cursor=database.query(DbHelper.T_USER,allColumns,null,
                null,
                null,null,null);

        int count=cursor.getCount();
        Log.d(TAG,"Count "+count);

        Boolean check=false;

        if (count==1){

            check=true;
        }

        return check;

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

        Log.d(TAG,"Deleting user data");
        database.delete(DbHelper.T_USER,null,null);
    }
}
