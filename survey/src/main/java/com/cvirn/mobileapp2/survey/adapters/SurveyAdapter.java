package com.cvirn.mobileapp2.survey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.model.Survey;

import java.util.List;

/**
 * Created by janko on 5/6/14.
 */
public class SurveyAdapter extends ArrayAdapter<Survey> {

    private static final String TAG ="InstallAdapter" ;
    Context context;
    List<Survey> prcmessages;

    public SurveyAdapter(Context context, List<Survey> messages) {
        super(context, android.R.id.content, messages);
        this.context = context;
        this.prcmessages =messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.list_extended_survey, null);


        Survey m=prcmessages.get(position);

        TextView tv = (TextView) view.findViewById(R.id.textSurveyName);
        tv.setText(m.getName());

        TextView uid=(TextView)view.findViewById(R.id.textSurveySID);
        uid.setText(m.getSid());

        TextView date=(TextView)view.findViewById(R.id.textSurveyDate);
        //date.setText(m.get);









        return view;
    }
}
