package com.cvirn.mobileapp2.survey.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.adapters.TaskAdapter;
import com.cvirn.mobileapp2.survey.datasources.TaskDS;
import com.cvirn.mobileapp2.survey.model.Task;
import com.cvirn.mobileapp2.survey.survey.MainSurveyActivity;

import java.util.List;

import dbhelper.DbHelper;


public class TasksFragment extends ListFragment {

    private static final String TAG ="TaskFragment" ;
    TaskDS idsource;
    SharedPreferences preferences;
    Spinner spnSort;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task,
                container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences
                (getActivity().getApplicationContext());


        spnSort=(Spinner)view.findViewById(R.id.spinner);
        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();

                /*<item>name</item>
        <item>priority</item>
        <item>distance</item>
        <item>type</item>*/

                if (label.equalsIgnoreCase("name")){

                    populateUISort(DbHelper.TA_SID);
                }
                else if (label.equalsIgnoreCase("distance")){

                    populateUISort(DbHelper.TA_ID);

                }
                else if (label.equalsIgnoreCase("priority")){

                    populateUISort(DbHelper.TA_DUE);

                }
                else if (label.equalsIgnoreCase("type")){

                    populateUISort(DbHelper.TA_STATUS);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        populateUI();


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        idsource=new TaskDS(getActivity());
        idsource.open();


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final View arg=v;
        String cid=""
                + ((TextView) ((LinearLayout) arg).getChildAt(0))
                .getText().toString();


        String sid=""
                + ((TextView) ((LinearLayout) arg).getChildAt(4))
                .getText().toString();
        String poi_id=""
                + ((TextView) ((LinearLayout) arg).getChildAt(3))
                .getText().toString();



        //Log.d(TAG, cid);
        Intent intent = new Intent(getActivity(), MainSurveyActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("poi_id",poi_id);
        startActivity(intent);
    }

    private void populateUI(){

        List<Task> tasks=idsource.findAll();
        if(tasks.size()!=0){

            ArrayAdapter<Task> adapter=new TaskAdapter(getActivity().getApplicationContext(),
                    tasks);
            setListAdapter(adapter);
        }
        else{
            showAlert("Messages","You have no tasks.");
        }


    }
    private void populateUISort(String column){

        List<Task> tasks=idsource.findAll();
        if(tasks.size()!=0){

            ArrayAdapter<Task> adapter=new TaskAdapter(getActivity().getApplicationContext(),
                    tasks);
            setListAdapter(adapter);
        }
        else{
            showAlert("Messages","You have no tasks.");
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
