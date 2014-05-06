package dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by janko on 4/28/14.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="mps.db" ;
    private static final int DATABASE_VERSION = 3 ;
    private static final String TAG ="DB Helper" ;


    //User table
    public static final String T_USER="user";
    public static final String T_USERNAME="username";
    public static final String T_PASSWORD="password";
    public static final String T_KEY="key";
    public static final String T_ID="_id";
    public static final String T_FNAME="fname";
    public static final String T_LNAME="lname";
    public static final String T_EMAIL="email";

    //Message table
    public static final String T_MESSAGE="message";
    public static final String M_ID="_id";
    public static final String M_STATUS="status";
    public static final String M_TITLE="title";
    public static final String M_BODY="body";
    public static final String M_SID="sid";



    public static final String CREATE_MESSAGE_TABLE="CREATE TABLE " + T_MESSAGE + " (" +
            M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            M_BODY + " TEXT, " +
            M_SID + " TEXT, " +
            M_TITLE + " TEXT, " +
            M_STATUS + " TEXT " +

            ")";



    public static final String CREATE_USER_TABLE="CREATE TABLE " + T_USER + " (" +
            T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            T_USERNAME + " TEXT, " +
            T_PASSWORD + " TEXT, " +
            T_FNAME + " TEXT, " +
            T_LNAME + " TEXT, " +
            T_EMAIL + " TEXT, " +
            T_KEY + " TEXT " +

            ")";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "on create db");
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ T_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ T_MESSAGE);
        onCreate(db);

    }
}
