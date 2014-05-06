package com.cvirn.mobileapp2.survey.messages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.adapters.MessageAdapter;
import com.cvirn.mobileapp2.survey.datasources.MessageDS;
import com.cvirn.mobileapp2.survey.model.Message;

import java.util.List;


public class MessageFragment extends ListFragment {

    private static final String TAG ="MessageFragment" ;
    MessageDS idsource;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_install,
                container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences
                (getActivity().getApplicationContext());


        populateUI();


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        idsource=new MessageDS(activity);
        idsource.open();


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final View arg=v;
        String cid=""
                + ((TextView) ((LinearLayout) arg).getChildAt(0))
                .getText().toString();
        Log.d(TAG, cid);
        Intent intent = new Intent(getActivity(), MessageDetailsActivity.class);
        intent.putExtra("mid", cid);
        startActivity(intent);
    }

    private void populateUI(){

        List<Message> messages=idsource.findAll();
        if(messages.size()!=0){

            ArrayAdapter<Message> adapter=new MessageAdapter(getActivity().getApplicationContext(),
                    messages);
            setListAdapter(adapter);
        }
        else{
            showAlert("Messages","You have no messages.");
        }


    }

    private void showAlert(String title,String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg)

                .setCancelable(false)

                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }





}
