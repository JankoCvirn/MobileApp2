package com.cvirn.mobileapp2.survey.poi;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.adapters.SurveyAdapter;
import com.cvirn.mobileapp2.survey.datasources.SurveyDS;
import com.cvirn.mobileapp2.survey.model.Survey;
import com.cvirn.mobileapp2.survey.survey.MainSurveyActivity;

import java.util.ArrayList;

public class PoiSurveysActivity extends ListActivity {

    private static final String TAG ="Survey for POI" ;
    Intent i;
    Survey survey;

    SurveyDS sds;
    ArrayList<Survey> sholder;
    String formid;
    String poi;
    String poi_id;

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getPoi_id() {
        return poi_id;
    }

    public void setPoi_id(String poi_id) {
        this.poi_id = poi_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_surveys);
        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        i=getIntent();
        setPoi(i.getStringExtra("poi"));
        setPoi_id(i.getStringExtra("poi_id"));
        initDataSources();

        initUI();


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final View arg3_a = v;
        String sid=""
                + ((TextView) ((LinearLayout) arg3_a).getChildAt(0))
                .getText().toString();
        Log.d(TAG, sid);
        Intent intent = new Intent(this, MainSurveyActivity.class);
        intent.putExtra("sid",sid);
        intent.putExtra("poi",getPoi());
        intent.putExtra("poi_id",getPoi_id());
        startActivity(intent);
    }



    private void initDataSources(){

        sds=new SurveyDS(this);


        sds.open();


        survey=sds.findSurvey("1");

        sholder=new ArrayList<Survey>();
        sholder.add(survey);
        ArrayAdapter<Survey> adapter=new SurveyAdapter(this,sholder);
        setListAdapter(adapter);




    }
    private void initUI(){

        TextView textSurvey=(TextView)findViewById(R.id.textView3);
        textSurvey.setPaintFlags(textSurvey.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        ImageButton btnBack=(ImageButton)findViewById(R.id.imageButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button details=(Button)findViewById(R.id.buttonDetails);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView shopName=(TextView)findViewById(R.id.textViewShopName);
        shopName.setText(getPoi());

    }
}
