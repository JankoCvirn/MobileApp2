package com.cvirn.mobileapp2.survey.search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.adapters.POIAdapter;
import com.cvirn.mobileapp2.survey.datasources.PoiDS;
import com.cvirn.mobileapp2.survey.datasources.UserDS;
import com.cvirn.mobileapp2.survey.jsonparser.JsonParser;
import com.cvirn.mobileapp2.survey.model.POI;
import com.cvirn.mobileapp2.survey.model.User;
import com.cvirn.mobileapp2.survey.poi.PoiDetailsActivity;
import com.cvirn.mobileapp2.survey.requests.ApiRequest;

import java.util.ArrayList;


public class SearchFragment extends ListFragment {

    private static final String TAG ="SearchFragment" ;

    SharedPreferences preferences;
    ImageButton btnSearc;
    EditText txtSearch;
    private User user;
    CheckBox chkOnline;
    PoiDS ds;
    ArrayList<POI> search_holder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search,
                container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences
                (getActivity().getApplicationContext());


        userData();
        btnSearc=(ImageButton)view.findViewById(R.id.imageButtonSearch);
        txtSearch=(EditText)view.findViewById(R.id.editTextSearch);
        chkOnline=(CheckBox)view.findViewById(R.id.checkBoxOnlineSearch);

        btnSearc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtSearch.getEditableText().toString().length() != 0 && chkOnline.isChecked()) {

                    new Search().execute("");
                }
                else{

                    new LocalSearch().execute("");

                }

            }
        });


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ds=new PoiDS(getActivity().getApplicationContext());

        ds.open();

    }

    private void userData(){

        UserDS uds=new UserDS(getActivity());
        uds.open();
        user=uds.getUser();

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

    class Search extends AsyncTask<String,String,String>{

        ProgressDialog dialog = ProgressDialog.show(getActivity(),
                getString(R.string.finish_title), getString(R.string.please_wait), true);
        @Override
        protected String doInBackground(String... params) {
            ApiRequest api = new ApiRequest();
            api.setUser(user);
            POI p = new POI();
            p.setLat("0");
            p.setLng("0");
            String search_result = api.search(p, txtSearch.getEditableText().toString());


            return search_result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            JsonParser parser = new JsonParser(s);
            ArrayList<POI> holder = parser.parsePOISearch();
            ArrayAdapter<POI> adapter = new POIAdapter(getActivity().getApplicationContext
                    (), holder);
            adapter.notifyDataSetChanged();
            setListAdapter(adapter);
        }
    }




    class LocalSearch extends AsyncTask<String,String,String>{

        ProgressDialog dialog = ProgressDialog.show(getActivity(),
                getString(R.string.finish_title), getString(R.string.please_wait), true);
        @Override
        protected String doInBackground(String... params) {

            search_holder=ds.searchPOI(txtSearch.getEditableText().toString());

            String res="NACK";
            if (search_holder.size()>0){
                res="ACK";
            }

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            //JsonParser parser = new JsonParser(s);
            ///ArrayList<POI> holder = parser.parsePOISearch();
            if (s.equalsIgnoreCase("ACK")){
            ArrayAdapter<POI> adapter = new POIAdapter(getActivity().getApplicationContext
                    (), search_holder);
            adapter.notifyDataSetChanged();
            setListAdapter(adapter);}
            else{

                txtSearch.setHint("NO RESULT");
            }
        }
    }



}
