package com.cvirn.mobileapp2.survey.survey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.barcode.ScanditSDKSampleBarcodeActivity;
import com.cvirn.mobileapp2.survey.datasources.AnswersDS;
import com.cvirn.mobileapp2.survey.datasources.QuestionDS;
import com.cvirn.mobileapp2.survey.model.Question;
import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

import java.util.ArrayList;

public class QuestionScanActivity extends Activity implements ScanditSDKListener {
    private static final int ZBAR_SCANNER_REQUEST = 1;
    private String contantsString;

    Button btnScan;
    EditText editCode;
    TextView nrQuestion;
    TextView txtQuestionText;

    Button btnprev;
    Button btnnext;
    ImageButton back;

    Intent i;
    QuestionDS qds;
    String parent_id;
    String q_sid;
    String type;

    int counter;
    private ScanditSDK mBarcodePicker;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    public static final String sScanditSdkAppKey = "2695WOa9EeOZk0gcG+lV/MHFKQBMH6CtbnFeX+d+zDM";
    private Question q;

    public String getQ_sid() {
        return q_sid;
    }

    public void setQ_sid(String q_sid) {
        this.q_sid = q_sid;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_scan);

        qds=new QuestionDS(this);
        qds.open();

        i=getIntent();
        setCounter(i.getIntExtra("counter",0));
        setParent_id(i.getStringExtra("parent_id"));
        //setType(i.getStringExtra("type"));
        setQ_sid(i.getStringExtra("q_sid"));

        q=qds.findQuestion(getCounter());

        txtQuestionText=(TextView)findViewById(R.id.txtQuestionTitle);
            txtQuestionText.setText(q.getTitle());
        nrQuestion=(TextView)findViewById(R.id.txtQuestionorder);
            nrQuestion.setText(q.getOrder()+".");

        editCode=(EditText)findViewById(R.id.editCode);


        btnprev=(Button)findViewById(R.id.buttonPrev);
        btnnext=(Button)findViewById(R.id.buttonNext);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAnswer(editCode.getEditableText().toString(),"barcode");
               /* Intent intent = new Intent(QuestionScanActivity.this,
                        QuestionPictureActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                startActivity(intent);*/
                startNextSurvey();
                finish();
            }
        });

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*saveAnswer(editCode.getEditableText().toString(),"barcode");
                Intent intent = new Intent(QuestionScanActivity.this, QuestionLargeActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                startActivity(intent);
                finish();*/
            }
        });

        btnScan=(Button)findViewById(R.id.buttonScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(QuestionScanActivity.this,ScanditSDKSampleBarcodeActivity
                        .class);
                startActivityForResult(i,1);


            }
        });
        back=(ImageButton)findViewById(R.id.imageButton);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Log.d("Passed POI id:", getParent_id());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {




        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("barcode")) {
                Toast.makeText(this, data.getExtras().getString("barcode"),
                        Toast.LENGTH_SHORT).show();
                editCode.setText(data.getExtras().getString("barcode"));
            }
        }

    }

    @Override
    public void didScanBarcode(String barcode, String symbology ) {

        String cleanedBarcode = "";
        for (int i = 0 ; i < barcode.length(); i++) {
            if (barcode.charAt(i) > 30) {
                cleanedBarcode += barcode.charAt(i);
            }
        }

        editCode.setText(cleanedBarcode);
        Toast.makeText(this, symbology + ": " + cleanedBarcode, Toast.LENGTH_LONG).show();
    }

    @Override
    public void didManualSearch(String s) {

    }

    @Override
    public void didCancel() {

    }

    public void initializeAndStartBarcodeScanning() {
        // Switch to full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // We instantiate the automatically adjusting barcode picker that will
        // choose the correct picker to instantiate. Be aware that this picker
        // should only be instantiated if the picker is shown full screen as the
        // legacy picker will rotate the orientation and not properly work in
        // non-fullscreen.
        ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
                this, sScanditSdkAppKey, ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);

        // Add both views to activity, with the scan GUI on top.
        setContentView(picker);
        mBarcodePicker = picker;

        // Register listener, in order to be notified about relevant events
        // (e.g. a successfully scanned bar code).
        mBarcodePicker.getOverlayView().addListener(this);

        // Show a search bar in the scan user interface.
        mBarcodePicker.getOverlayView().showSearchBar(true);
    }

    private long saveAnswer(String answer,String type){

        AnswersDS ads=new AnswersDS(this);
        ads.open();
        q.setAnswer(answer);
        q.setParentid(getParent_id());
        long i=ads.createAnswer(q);

        ads.close();
        return i;



    }

    private void startNextSurvey(){

        Log.d("Start Next Survey","Qsid"+getQ_sid());
        Log.d("Start Next Survey","Counter"+counter);

        ArrayList<Question> holder=new ArrayList<Question>();

        counter=getCounter()+1;
        QuestionDS qds=new QuestionDS(this);
        qds.open();
        holder=qds.findAll(getQ_sid());
        qds.close();
        int holder_size=holder.size();

        if (counter>holder_size) {

            Intent intent = new Intent(QuestionScanActivity.this,
                    FinishSurveyActivity.class);
            intent.putExtra("parent_id",getParent_id());
            intent.putExtra("q_sid",getQ_sid());
            startActivity(intent);

            finish();

        }
        else {






            String check=holder.get(counter-1).getType();
            //int counter=getCounter()+1;

            Log.d("MainSurvey","Passing parentid:"+getParent_id()+" and counter "+getCounter() + " " +
                    "and type: "+getType() );


            overridePendingTransition(0, 0);
        /*Intent intent = new Intent(MainSurveyActivity.this, QuestionVisibleActivity.class);
        intent.putExtra("parent_id",getPoi_object_id());
        intent.putExtra("q_sid",holder.get(2).getSid());
        startActivity(intent);*/


            if (check.equals("bool")){

                Intent intent = new Intent(QuestionScanActivity.this, QuestionVisibleActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else if(check.equals("range")){

                Intent intent = new Intent(QuestionScanActivity.this, QuestionLargeActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else if(check.equals("barcode")){

                Intent intent = new Intent(QuestionScanActivity.this, QuestionScanActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else if(check.equals("image")){

                Intent intent = new Intent(QuestionScanActivity.this, QuestionPictureActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else{

                finish();
            }
        }




    }


}
