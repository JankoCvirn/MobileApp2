package com.cvirn.mobileapp2.survey.poi;


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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.adapters.POIAdapter;
import com.cvirn.mobileapp2.survey.datasources.AnswersDS;
import com.cvirn.mobileapp2.survey.datasources.PoiDS;
import com.cvirn.mobileapp2.survey.model.POI;

import java.util.List;

import dbhelper.DbHelper;


public class PoiFragment extends ListFragment  {

    private static final String TAG ="POIFragment" ;
    PoiDS idsource;
    AnswersDS ds;
    SharedPreferences preferences;
    Switch s;
    ImageButton btnNewPoi;
    Spinner spnSort;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_poi,
                container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences
                (getActivity().getApplicationContext());


        populateUI();

        s=(Switch)view.findViewById(R.id.switch1);

        s.setChecked(false);

                s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                            Intent intent = new Intent(getActivity(), MapsActivity.class);

                            startActivity(intent);
                        }
                    }
                });

        btnNewPoi=(ImageButton)view.findViewById(R.id.imageButtonNewPoi);
        btnNewPoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPoiActivity.class);

                startActivity(intent);
            }
        });

        spnSort=(Spinner)view.findViewById(R.id.spinner2);
        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();

                /*<item>name</item>
        <item>priority</item>
        <item>distance</item>
        <item>type</item>*/

                if (label.equalsIgnoreCase("name")){

                    populateUISort(DbHelper.POI_NAME);
                }
                else if (label.equalsIgnoreCase("distance")){

                    populateUISort(DbHelper.POI_NAME);

                }
                else if (label.equalsIgnoreCase("priority")){

                    populateUISort(DbHelper.POI_SID);

                }
                else if (label.equalsIgnoreCase("type")){

                    populateUISort(DbHelper.POI_TYPE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        return view;
    }





    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        idsource=new PoiDS(activity);
        idsource.open();

        ds=new AnswersDS(activity);
        ds.open();
        ds.findBypoiSID("4");



    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final View arg=v;
        String poi_name=""
                + ((TextView) ((LinearLayout) arg).getChildAt(0))
                .getText().toString();
        //Log.d(TAG, cid);
        Intent intent = new Intent(getActivity(), PoiDetailsActivity.class);
        intent.putExtra("poi_name", poi_name);
        startActivity(intent);
    }

    private void populateUI(){

        List<POI> poiList=idsource.findAll();
        if(poiList.size()!=0){

            ArrayAdapter<POI> adapter=new POIAdapter(getActivity().getApplicationContext(),
                    poiList);
            setListAdapter(adapter);
        }
        else{
            showAlert("POI","You have no points of interest.");
        }




    }

    private void populateUISort(String column){

        List<POI> poiList=idsource.findAlWithSort(column);
        if(poiList.size()!=0){

            ArrayAdapter<POI> adapter=new POIAdapter(getActivity().getApplicationContext(),
                    poiList);
            setListAdapter(adapter);
        }
        else{
            showAlert("POI","You have no points of interest.");
        }




    }


    @Override
    public void onResume() {
        super.onResume();
        s.setChecked(false);
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
