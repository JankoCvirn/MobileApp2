package com.cvirn.mobileapp2.survey.survey;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.PoiDS;
import com.cvirn.mobileapp2.survey.datasources.QuestionDS;
import com.cvirn.mobileapp2.survey.datasources.SurveyDS;
import com.cvirn.mobileapp2.survey.model.POI;
import com.cvirn.mobileapp2.survey.model.Question;
import com.cvirn.mobileapp2.survey.model.Survey;

import java.util.ArrayList;

public class MainSurveyActivity extends Activity {
    Intent i;
    String survey_id;
    String poi_object_id;

    Survey survey;
    SurveyDS sds;

    int question_nr;
    private POI p;

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
        setContentView(R.layout.activity_main_survey);
        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setTitle("");

        i=getIntent();
        setSurvey_id(i.getStringExtra("sid"));
        setPoi_object_id(i.getStringExtra("poi_id"));

        openDS();
        initUI();
        Log.d("Passed Survey ID: ",getSurvey_id());
        Log.d("Passed POI id:",getPoi_object_id());
    }


    private void initUI(){


        Button btnStart=(Button)findViewById(R.id.buttonStartSurvey);

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



                startSurvey();
            }
        });

    }

    private void openDS(){

        sds=new SurveyDS(this);
        sds.open();
        survey=sds.findSurvey(getSurvey_id());
        sds.close();

        QuestionDS qds=new QuestionDS(this);
        qds.open();
        ArrayList<Question> holder=qds.findAll(survey_id);
        question_nr=holder.size();

        PoiDS pds=new PoiDS(this);
        pds.open();
        p=pds.findPOIBySID(getPoi_object_id());
        pds.close();

    }

    private void startSurvey(){


        ArrayList<Question> holder=new ArrayList<Question>();


        QuestionDS qds=new QuestionDS(this);
        qds.open();
        holder=qds.findAll(survey.getSid());
        qds.close();
        String check=holder.get(0).getType();
        int counter=1;

        Log.d("MainSurvey","Passing parentid:"+getPoi_object_id()+" and qsid"+getSurvey_id()+" " +
                "type:"+check);


        overridePendingTransition(0, 0);
        /*Intent intent = new Intent(MainSurveyActivity.this, QuestionVisibleActivity.class);
        intent.putExtra("parent_id",getPoi_object_id());
        intent.putExtra("q_sid",holder.get(2).getSid());
        startActivity(intent);*/


        if (check.equals("bool")){

            Intent intent = new Intent(MainSurveyActivity.this, QuestionVisibleActivity.class);
            intent.putExtra("parent_id",getPoi_object_id());
            intent.putExtra("q_sid",getSurvey_id());
            intent.putExtra("counter",counter);
            startActivity(intent);

        }
        else if(check.equals("range")){

            Intent intent = new Intent(MainSurveyActivity.this, QuestionLargeActivity.class);
            intent.putExtra("parent_id",getPoi_object_id());
            intent.putExtra("q_sid",getSurvey_id());
            intent.putExtra("counter",counter);
            startActivity(intent);

        }
        else if(check.equals("barcode")){

            Intent intent = new Intent(MainSurveyActivity.this, QuestionScanActivity.class);
            intent.putExtra("parent_id",getPoi_object_id());
            intent.putExtra("q_sid",getSurvey_id());
            intent.putExtra("counter",counter);
            startActivity(intent);

        }
        else if(check.equals("image")){

            Intent intent = new Intent(MainSurveyActivity.this, QuestionPictureActivity.class);
            intent.putExtra("parent_id",getPoi_object_id());
            intent.putExtra("q_sid",getSurvey_id());
            intent.putExtra("counter",counter);
            startActivity(intent);

        }
        else{

            finish();
        }




    }



}
