package com.cvirn.mobileapp2.survey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.model.Message;

import java.util.List;

/**
 * Created by janko on 5/6/14.
 */
public class MessageAdapter extends ArrayAdapter<Message> {

    private static final String TAG ="InstallAdapter" ;
    Context context;
    List<Message> prcmessages;

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, android.R.id.content, messages);
        this.context = context;
        this.prcmessages =messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.list_extended_messages, null);


        Message m=prcmessages.get(position);

        TextView tv = (TextView) view.findViewById(R.id.textMessage);
        tv.setText(m.getTitle()+"\n"+m.getFrom());

        TextView uid=(TextView)view.findViewById(R.id.textMessageSID);
        uid.setText(m.getMid());








        return view;
    }
}
