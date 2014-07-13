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
import android.widget.Spinner;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.AnswersDS;
import com.cvirn.mobileapp2.survey.datasources.QuestionDS;
import com.cvirn.mobileapp2.survey.model.Question;

import java.util.ArrayList;

public class QuestionVisibleActivity extends Activity {

    TextView nrQuestion;
    TextView txtQuestionText;
    Spinner spnValues;
    Intent i;
    QuestionDS qds;
    String parent_id;
    String q_sid;
    String type;
    int counter;
    Button btnnext;
    ImageButton back;
    private Question q;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getQ_sid() {
        return q_sid;
    }

    public void setQ_sid(String q_sid) {
        this.q_sid = q_sid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_visible);
        qds=new QuestionDS(this);
        qds.open();
        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

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
        btnnext=(Button)findViewById(R.id.buttonNext);

        spnValues=(Spinner)findViewById(R.id.spinner);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAnswer(spnValues.getSelectedItem().toString(),"bool");
                /*Intent intent = new Intent(QuestionVisibleActivity.this,
                        QuestionLargeActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                startActivity(intent);*/
                startNextSurvey();
                finish();


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

            Intent intent = new Intent(QuestionVisibleActivity.this,
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

                Intent intent = new Intent(QuestionVisibleActivity.this, QuestionVisibleActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else if(check.equals("range")){

                Intent intent = new Intent(QuestionVisibleActivity.this, QuestionLargeActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else if(check.equals("barcode")){

                Intent intent = new Intent(QuestionVisibleActivity.this, QuestionScanActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else if(check.equals("image")){

                Intent intent = new Intent(QuestionVisibleActivity.this, QuestionPictureActivity.class);
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
