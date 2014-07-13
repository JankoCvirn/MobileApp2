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
    private static final int DATABASE_VERSION = 25 ;
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
    public static final String T_URL="url";
    public static final String T_ORG="org";
    //16-21.7 fember stjepan 40 eur

    //Message table
    public static final String T_MESSAGE="message";
    public static final String M_ID="_id";
    public static final String M_STATUS="status";
    public static final String M_TITLE="title";
    public static final String M_BODY="body";
    public static final String M_SID="sid";
    public static final String M_FROM="sender";


    public static final String TABLE_TASK="task";
    public static final String TA_ID="_id";
    public static final String TA_SID="sid";
    public static final String TA_POI="poi";
    public static final String TA_STATUS="status";
    public static final String TA_DUE="due";
    public static final String TA_FORM="form";
    public static final String TA_FORMID="formid";
    public static final String TA_POINAME="poiname";


    public static final String TABLE_POI="poi";
    public static final String POI_ID="_id";
    public static final String POI_LAT="lat";
    public static final String POI_LNG="lng";
    public static final String POI_NAME="name";
    public static final String POI_TYPE="type";
    public static final String POI_SID="sid";
    public static final String POI_STATUS="status";
    public static final String POI_BKEY="bkey";
    public static final String POI_CREATED="createdby";
    public static final String POI_MODATE="moddate";
    public static final String POI_CDATE="createdate";
    public static final String POI_SEGMENT="segment";
    public static final String POI_CITY="city";
    public static final String POI_ADDRESS="address";
    public static final String POI_PHONE="phone";
    public static final String POI_PIc="pic";


    /*"SequenceOrder": 1,
                    "Type": "text",
                    "ID": "2093",
                    "Value": "Chikola",
                    "Name": "Address*/
    public static final String TABLE_POI_SPEC="poispec";
    public static final String POISPEC_ID="_id";
    public static final String POISPEC_PARENT="poiid";
    public static final String POISPEC_TYPE="type";
    public static final String POISPEC_SID="SID";
    public static final String POISPEC_VALUE="value";
    public static final String POISPEC_NAME="name";
    public static final String POISPEC_SEQORDER="sequence";

    public static final String CREATE_POISPEC_TABLE="CREATE TABLE " + TABLE_POI_SPEC + " (" +
            POISPEC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            POISPEC_PARENT + " TEXT, " +
            POISPEC_NAME + " TEXT, " +
            POISPEC_TYPE + " TEXT, " +
            POISPEC_VALUE + " TEXT, " +
            POISPEC_SEQORDER + " TEXT, " +
            POISPEC_SID + " TEXT " +
            ")";




    public static final String TABLE_SURVEY="survey";
    public static final String SURVEY_ID="_id";
    public static final String SURVEY_NAME="name";
    public static final String SURVEY_SID="sid";
    public static final String SURVEY_ENABLED="enabled";
    public static final String SURVEY_DESC="desc";



    public static final String TABLE_QUESTIONS="questions";
    public static final String SO_ID="_id";
    public static final String SO_SID="sid";
    public static final String SO_TITLE="title";
    public static final String SO_PARAM="param";
    public static final String SO_PARENT="parentid";
    public static final String SO_ORDER="ordernumber";
    public static final String SO_TYPE="type";
    public static final String SO_SKIP="skip";
    public static final String SO_ANSWER="userinput";
    public static final String SO_POI="poid";

    public static final String TABLE_ANSWERS="answers";
    public static final String AN_ID="_id";
    public static final String AN_SID="sid";
    public static final String AN_PARENT="parentid";
    public static final String AN_TYPE="qtype";
    public static final String AN_ANSWER="userinput";

    public static final String CREATE_ANSWERS_TABLE="CREATE TABLE " + TABLE_ANSWERS + " (" +
            AN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AN_SID + " TEXT, " +
            AN_PARENT + " TEXT, " +
            AN_TYPE + " TEXT, " +
            AN_ANSWER + " TEXT " +
            ")";



    public static final String CREATE_QUESTIONS_TABLE="CREATE TABLE " + TABLE_QUESTIONS + " (" +
            SO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SO_SID + " TEXT, " +
            SO_TITLE + " TEXT, " +
            SO_PARAM + " TEXT, " +
            SO_PARENT + " TEXT, " +
            SO_ORDER + " TEXT, " +
            SO_TYPE + " TEXT, " +
            SO_ANSWER + " TEXT, " +
            SO_POI + " TEXT, " +
            SO_SKIP + " TEXT " +
            ")";



    public static final String CREATE_SURVEY_TABLE="CREATE TABLE " + TABLE_SURVEY + " (" +
            SURVEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SURVEY_SID + " TEXT, " +
            SURVEY_NAME + " TEXT, " +
            SURVEY_ENABLED + " TEXT, " +
            SURVEY_DESC + " TEXT " +

            ")";


    public static final String CREATE_POI_TABLE="CREATE TABLE " + TABLE_POI + " (" +
            POI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            POI_LAT + " TEXT, " +
            POI_LNG + " TEXT, " +
            POI_TYPE + " TEXT, " +
            POI_SID + " TEXT, " +
            POI_STATUS + " TEXT, " +
            POI_BKEY + " TEXT, " +
            POI_CREATED + " TEXT, " +
            POI_MODATE + " TEXT, " +
            POI_CDATE + " TEXT, " +
            POI_SEGMENT + " TEXT, " +
            POI_CITY + " TEXT, " +
            POI_ADDRESS + " TEXT, " +
            POI_PHONE + " TEXT, " +
            POI_PIc + " TEXT, " +
            POI_NAME + " TEXT " +

            ")";




    public static final String CREATE_TASK_TABLE="CREATE TABLE " + TABLE_TASK + " (" +
            TA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TA_SID + " TEXT, " +
            TA_POI + " TEXT, " +
            TA_STATUS + " TEXT, " +
            TA_FORMID + " TEXT, " +
            TA_DUE + " TEXT, " +
            TA_POINAME + " TEXT, " +
            TA_FORM + " TEXT " +

            ")";


    public static final String CREATE_MESSAGE_TABLE="CREATE TABLE " + T_MESSAGE + " (" +
            M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            M_BODY + " TEXT, " +
            M_SID + " TEXT, " +
            M_FROM + " TEXT, " +
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
            T_URL + " TEXT, " +
            T_ORG + " TEXT, " +
            T_KEY + " TEXT " +

            ")";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "on create db version "+DATABASE_VERSION);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
        db.execSQL(CREATE_TASK_TABLE);
        db.execSQL(CREATE_POI_TABLE);
        db.execSQL(CREATE_SURVEY_TABLE);
        db.execSQL(CREATE_QUESTIONS_TABLE);
        db.execSQL(CREATE_ANSWERS_TABLE);
        db.execSQL(CREATE_POISPEC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG,"update db to version:"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS "+ T_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ T_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_POI);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SURVEY);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_POI_SPEC);

        onCreate(db);

    }
}
