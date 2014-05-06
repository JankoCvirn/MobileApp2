package com.cvirn.mobileapp2.survey.messages;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.MainTabedActivity;
import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.MessageDS;
import com.cvirn.mobileapp2.survey.model.Message;

public class MessageDetailsActivity extends Activity {

    TextView textFrom;
    TextView textContent;
    Intent i;

    private String message_id;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        final ActionBar actionBar = getActionBar();

        setTitle("RealImpact");
        // Set up the action bar.
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        i=getIntent();
        setMessage_id(i.getStringExtra("mid"));
        initializeUI();
    }

    private void initializeUI() {

        textFrom=(TextView)findViewById(R.id.textFrom);
        textContent=(TextView)findViewById(R.id.textViewContent);

        MessageDS ds=new MessageDS(this);
        ds.open();

        Message m=ds.findMessage(getMessage_id());

        textFrom.setText(m.getTitle());
        textContent.setText(m.getBody());


    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_details, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            finish();
            return true;
        }*/
        switch (id) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainTabedActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
                break;


        }

        return super.onOptionsItemSelected(item);
    }
}
