package com.cvirn.mobileapp2.survey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.model.Task;

import java.util.List;

/**
 * Created by janko on 5/6/14.
 */
public class TaskAdapter extends ArrayAdapter<Task> {

    private static final String TAG ="TaskAdapter" ;
    Context context;
    List<Task> prtask;

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, android.R.id.content, tasks);
        this.context = context;
        this.prtask=tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.list_extended_tasks, null);


        Task m=prtask.get(position);

        TextView tv = (TextView) view.findViewById(R.id.textMessage);
        tv.setText(m.getForm()+"\n"+m.getStatus()+"\n"+m.getPoi_name());

        TextView uid=(TextView)view.findViewById(R.id.textMessageSID);
        uid.setText(m.getSid());

        TextView due = (TextView) view.findViewById(R.id.textDueDate);
        due.setText(m.getDue_date());

        TextView formid = (TextView) view.findViewById(R.id.textViewFORMID);
        formid.setText(m.getForm_id());

        TextView poiid = (TextView) view.findViewById(R.id.textViewPOIID);
        poiid.setText(m.getPoi());








        return view;
    }
}
