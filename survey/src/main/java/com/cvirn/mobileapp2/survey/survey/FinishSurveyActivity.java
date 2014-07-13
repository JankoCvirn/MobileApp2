package com.cvirn.mobileapp2.survey.survey;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cvirn.mobileapp2.survey.GPSTracker;
import com.cvirn.mobileapp2.survey.MainTabedActivity;
import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.AnswersDS;
import com.cvirn.mobileapp2.survey.datasources.PoiDS;
import com.cvirn.mobileapp2.survey.datasources.SurveyDS;
import com.cvirn.mobileapp2.survey.datasources.UserDS;
import com.cvirn.mobileapp2.survey.jsonparser.JsonParser;
import com.cvirn.mobileapp2.survey.model.CheckIn;
import com.cvirn.mobileapp2.survey.model.POI;
import com.cvirn.mobileapp2.survey.model.Question;
import com.cvirn.mobileapp2.survey.model.Survey;
import com.cvirn.mobileapp2.survey.model.User;
import com.cvirn.mobileapp2.survey.requests.ApiRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class FinishSurveyActivity extends Activity {
    private static final String TAG ="Test" ;
    Intent i;
    String survey_id;
    String poi_object_id;

    Survey survey;
    SurveyDS sds;

    private String SignalStrengh;
    private CheckIn chin;

    public String getSignalStrengh() {
        return SignalStrengh;
    }

    public void setSignalStrengh(String signalStrengh) {
        SignalStrengh = signalStrengh;
    }

    int question_nr;
    private TelephonyManager Tel;
    private GsmCellLocation gcl;
    private SignalStrength ss;
    private LocationListener lm;
    MyPhoneStateListener    MyListener;
    boolean isGPSEnabled = false;
    GPSTracker gps;
    POI p;
    private String image_encoded;

    String[] answers;

    User user;
    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    public String getImage_encoded() {
        return image_encoded;
    }

    public void setImage_encoded(String image_encoded) {
        this.image_encoded = image_encoded;
    }

    public String getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(String survey_id) {
        this.survey_id = survey_id;
    }

    public String getPoi_object_id() {
        return poi_object_id;
    }

    public void setPoi_object_id(String poi_object_id) {
        this.poi_object_id = poi_object_id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_survey);
        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setTitle("");

        i=getIntent();
        setSurvey_id(i.getStringExtra("q_sid"));
        setPoi_object_id(i.getStringExtra("parent_id"));

        //alertboxDBackUpOnline("Passed value","POI:"+getPoi_object_id());



        userData();
        MyListener=new MyPhoneStateListener();
        Tel= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        gcl=new GsmCellLocation();
        Tel.listen(MyListener,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);




        Log.d(TAG,getPoi_object_id());
        encodeImage();
        openDS();
        initUI();
        getParametersForCheckIn();

    }


    private void initUI(){


        Button btnStart=(Button)findViewById(R.id.buttonFinishSurvey);

        ImageButton back=(ImageButton)findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title=(TextView)findViewById(R.id.textViewSTitle);
        TextView desc=(TextView)findViewById(R.id.textViewDesc);

        title.setText(getSurvey().getName());
        desc.setText(getSurvey().getDesc());

        TextView poi=(TextView)findViewById(R.id.textViewObject);
        poi.setText(p.getName());

        TextView  qnr=(TextView)findViewById(R.id.textViewQuestionNr);
        qnr.setText("number of questions : "+question_nr);



        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                */

                if (checkNetworking()){

                    if (answers!=null && answers.length>0){

                    new SendSurvey().execute("");}
                    else{
                        alertboxDBackUpOnline("Warning","No answers fetched from the database!");
                    }
                }
                else{

                    Toast.makeText(FinishSurveyActivity.this,"No network!",Toast.LENGTH_LONG).show();
                }

            }
        });



        //parameters




    }

    private void encodeImage() {

        try {
            File path = new File(Environment.getExternalStorageDirectory()
                    + "/survey/img/poi_" + getPoi_object_id() + "_image.jpg");

            long l = path.length();
            Log.d(TAG, "On result image size :" + l);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap myBitmap2 = BitmapFactory.decodeFile(path.getAbsolutePath(), options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //mImageView.setImageBitmap(myBitmap2);
            myBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();


            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            setImage_encoded(encodedImage);
            //Log.d(TAG,getImage_encoded());
        }
        catch (Exception e){

            Toast.makeText(FinishSurveyActivity.this,"Error encoding image.Have you taken the " +
                    "image?",Toast.LENGTH_LONG).show();
        }
    }

    private void openDS(){

        /*try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            if (sd.canWrite()) {
                String currentDBPath = "/data/com.cvirn.mobileapp2.survey/databases/mps.db";
                String backupDBPath = "ccc2_backup_"+ts+".db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.d("DB-B", backupDB.toString());
                    Toast.makeText(getBaseContext(), "Database has been copied!", Toast.LENGTH_LONG).show();
                }
                else{

                    Log.d("DB-B", "Database not found!"+currentDB);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            Log.d("DB-B",e.toString());
        }*/

        try {

            Log.d(TAG, "Opening Survey DS");
            sds = new SurveyDS(this);
            sds.open();
            survey = sds.findSurvey(getSurvey_id());
            sds.close();
        }
        catch (Exception e){
            Toast.makeText(FinishSurveyActivity.this,"Error opening survey ds Error:"+e.getMessage(),
                    Toast.LENGTH_LONG).show();

        }

        try {
            Log.d(TAG, "Opening POI DS");
            PoiDS pds = new PoiDS(this);
            pds.open();
            p = pds.findPOIBySID(getPoi_object_id());
            pds.close();
        }
        catch (Exception e){
            Toast.makeText(FinishSurveyActivity.this,"Error opening poi ds Error:"+e.getMessage(),
                    Toast.LENGTH_LONG).show();

        }

        try {
            Log.d(TAG,"Opening Answer DS");
            AnswersDS ads = new AnswersDS(this);
            ads.open();

            Log.d(TAG, getPoi_object_id());

            ArrayList<Question> holder = ads.findBypoiSID(getPoi_object_id());
            question_nr = holder.size();

            Log.d(TAG, "Holder size:" + question_nr);
            for (int i=0;i<holder.size();i++){

                Log.d(TAG,"Answer "+holder.get(i).getType()+": "+holder.get(i).getAnswer());
            }


             if (question_nr > 0) {
                answers = new String[5];
                answers[0] = "test";
                if (holder.get(0).getAnswer()!=null){
                    answers[1] = holder.get(0).getAnswer();
                    Log.d(TAG, holder.get(0).getAnswer());
                }


                if (holder.get(1).getAnswer()!=null) {
                    answers[2] = holder.get(1).getAnswer();
                    Log.d(TAG,"ANswer 2:"+ holder.get(1).getAnswer());
                }
                else{
                    answers[2]="none";
                    Log.d(TAG,"ANswer 2:"+ answers[2]);
                }
                if (holder.get(2).getAnswer()!=null) {
                    answers[3] = holder.get(2).getAnswer();
                    Log.d(TAG, holder.get(2).getAnswer());
                }
                else{
                    answers[3]="none";
                    Log.d(TAG,"ANswer 3:"+ answers[3]);
                }

                if (holder.get(3).getAnswer()!=null){

                answers[4] = getImage_encoded();
                Log.d(TAG, answers[4]);
                }
                else{
                    answers[4]="0";
                    Log.d(TAG,"ANswer 4:"+ answers[4]);
                }
            }
        }
        catch (Exception e){
            Toast.makeText(FinishSurveyActivity.this,"Error reading survey answers data source " +
                            "for " +
                            "POI:"+getPoi_object_id()+" Error:"+e.getMessage(),
                    Toast.LENGTH_LONG).show();


        }

    }



    private void getParametersForCheckIn(){

       chin=new CheckIn();
        try {
            chin.setGsmcellid("" + gcl.getCid());
            chin.setGsmsigstren(getSignalStrengh());
            chin.setSimoperator(Tel.getSimOperatorName());
            Log.d(TAG, "OP:" + Tel.getSimOperatorName());
            chin.setGsmcellid(Tel.getCellLocation().toString());
            gps = new GPSTracker(FinishSurveyActivity.this);

            // check if GPS enabled
            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                chin.setGpssigstren(gps.getAccuracy());
                Log.d(TAG, "TEST" + getSignalStrengh());

                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                //gps.showSettingsAlert();
            }
            //Log.d(TAG,"GSM signal"+chin.getGsmsigstren());
        }
        catch (Exception e){
            chin.setGsmcellid("null" );
            chin.setGsmsigstren("0");
            chin.setSimoperator("null");
            chin.setGpssigstren("0");
            chin.setGsmcellid("0");
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener
    {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength)
        {
            super.onSignalStrengthsChanged(signalStrength);
            /*Toast.makeText(getApplicationContext(), "Go to Firstdroid!!! GSM Cinr = "
                    + String.valueOf(signalStrength.getGsmSignalStrength()),
                    Toast.LENGTH_SHORT).show();*/
            setSignalStrengh(""+signalStrength.getGsmSignalStrength());

        }

    };

    private void userData(){

        UserDS uds=new UserDS(this);
        uds.open();
        user=uds.getUser();
        uds.close();

    }




    private boolean checkNetworking(){

        try{
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);


            return cm.getActiveNetworkInfo().isConnectedOrConnecting();

        }
        catch (Exception e) {
            return false;
        }

    }


    class SendSurvey extends AsyncTask<String,String,String>{
        ProgressDialog dialog = ProgressDialog.show(FinishSurveyActivity.this,
                getString(R.string.finish_title), getString(R.string.please_wait), true);

        @Override
        protected String doInBackground(String... params) {


            String ack = "false";
            try {

                ApiRequest api = new ApiRequest();
                api.setUser(user);
                api.checkin(chin);

                String json = api.setSurvey(answers, getPoi_object_id());

                JsonParser parser = new JsonParser(json);
                if (parser.uploadResponse(json)) {
                    ack = "true";
                }

            }
            catch (Exception e){
                alertboxDBackUpOnline("Error","Error on upload"+e.getMessage());
            }

            return ack;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            //TODO
            deleteAnswers();

            if(s.equalsIgnoreCase("true")){
                Toast.makeText(FinishSurveyActivity.this,"Survey data uploaded!",Toast.LENGTH_LONG).show();
            }
            else{

                Toast.makeText(FinishSurveyActivity.this,"Survey data upload failed!",
                        Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(FinishSurveyActivity.this, MainTabedActivity.class);
            //intent.putExtra("parent_id",getSurvey_id());
            //intent.putExtra("q_sid",holder.get(2).getSid());
            startActivity(intent);
            finish();
        }
    }

    private void deleteAnswers(){

        try {
            AnswersDS ads = new AnswersDS(this);
            ads.open();
            ads.deleteAll();
            ads.close();
        }
        catch (Exception e){


        }
    }

    protected void alertboxDBackUpOnline(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mymessage)
                .setCancelable(false)
                .setTitle(title)

                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }



    }




