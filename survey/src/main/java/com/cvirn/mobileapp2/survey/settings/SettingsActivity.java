package com.cvirn.mobileapp2.survey.settings;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.UserDS;
import com.cvirn.mobileapp2.survey.model.User;

public class SettingsActivity extends Activity {


    ImageButton back;
    UserDS ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        setTitle("");


        ds=new UserDS(this);
        ds.open();
        initializeUI();

    }

    private void initializeUI() {

        User u=ds.getUser();



        back=(ImageButton)findViewById(R.id.imageButton);
        TextView username=(TextView)findViewById(R.id.textUserName);
        username.setText(u.getUsername());
        TextView url=(TextView)findViewById(R.id.textServerAddress);
        url.setText(u.getUrl());
        TextView org=(TextView)findViewById(R.id.textOrganization);
        org.setText(u.getOrg());


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ds.close();
            }
        });

    }



}
