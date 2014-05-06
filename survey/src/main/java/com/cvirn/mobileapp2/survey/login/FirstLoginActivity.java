package com.cvirn.mobileapp2.survey.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cvirn.mobileapp2.survey.MainTabedActivity;
import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.MessageDS;
import com.cvirn.mobileapp2.survey.datasources.UserDS;
import com.cvirn.mobileapp2.survey.jsonparser.JsonParser;
import com.cvirn.mobileapp2.survey.model.Message;
import com.cvirn.mobileapp2.survey.model.User;
import com.cvirn.mobileapp2.survey.requests.ApiRequest;

import java.util.ArrayList;

public class FirstLoginActivity extends Activity {

    EditText eLogin;
    EditText ePass;
    EditText eServer;
    Button btnLogin;

    public static final String server="http://engineroom.realimpactanalytics.com/mobileapp/api/";

    private String serverurl;

    public String getServerurl() {
        return serverurl;
    }

    public void setServerurl(String serverurl) {
        this.serverurl = serverurl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
        setTitle("First Login");
        initilaizeUI();
    }

    private void initilaizeUI(){

        eLogin=(EditText)findViewById(R.id.editLogin);
        ePass=(EditText)findViewById(R.id.editPass);

        eServer=(EditText)findViewById(R.id.editServer);
        eServer.setText(server);

        btnLogin=(Button)findViewById(R.id.button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkNetworking()){

                attemptLogin();}
                else{
                    Toast.makeText(FirstLoginActivity.this,"No Network connection!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void attemptLogin() {

        boolean check=true;

        if (eServer.getEditableText().toString().length()==0){

            eServer.setError("*");

            check=false;
        }
        else{

            setServerurl(eServer.getEditableText().toString());
        }

        if (eLogin.getEditableText().toString().length()==0){

            eLogin.setError("*");

            check=false;
        }
        if (ePass.getEditableText().toString().length()==0){

            ePass.setError("*");
            check=false;
        }


        if (check){

            new LoginRequest().execute(getServerurl());
        }



    }


    class LoginRequest extends AsyncTask<String,Integer,Boolean>{

        ProgressDialog dialog = ProgressDialog.show(FirstLoginActivity.this,
                getString(R.string.login_title), getString(R.string.please_wait), true);

        @Override
        protected Boolean doInBackground(String... params) {


            boolean check=false;

            User u=new User();
            u.setUsername(eLogin.getEditableText().toString());
            u.setPassword(ePass.getEditableText().toString());

            ApiRequest request=new ApiRequest();
            request.LoginCheck(u);
            String json=request.Login();

            JsonParser parser=new JsonParser(json);
            ArrayList<Message> m=parser.parseMessages();

            if (!m.isEmpty()){

                deleteMessages();
                for (int i=0;i<m.size();i++){
                savemessages(m.get(i));}
                check=true;
            }


            User uretrieved=parser.parseUser();

            if (uretrieved!=null) {
                u.setFname(uretrieved.getFname());
                u.setLname(uretrieved.getLname());
                u.setEmail(uretrieved.getEmail());
                check=true;
                saveuser(u);
            }
            else {
                check=false;
            }
            return check;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();

            if (aBoolean){

                Intent intent = new Intent(FirstLoginActivity.this, MainTabedActivity.class);

                startActivity(intent);


            }
        }
    }

    private void deleteMessages() {

        MessageDS ds=new MessageDS(this);
        ds.open();
        ds.deleteAll();

    }

    private void savemessages(Message m) {

        MessageDS ds=new MessageDS(this);
        ds.open();
        ds.createMessage(m);
    }

    private void saveuser
            (User u){

        UserDS ds=new UserDS(this);
        ds.open();
        ds.createUser(u);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkNetworking(){

        try{
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);


            return cm.getActiveNetworkInfo().isConnectedOrConnecting();

        }
        catch (Exception e) {
            return false;
        }

    }
}
