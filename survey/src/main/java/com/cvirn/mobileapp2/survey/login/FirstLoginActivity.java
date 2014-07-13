package com.cvirn.mobileapp2.survey.login;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cvirn.mobileapp2.survey.MainTabedActivity;
import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.MessageDS;
import com.cvirn.mobileapp2.survey.datasources.PoiDS;
import com.cvirn.mobileapp2.survey.datasources.QuestionDS;
import com.cvirn.mobileapp2.survey.datasources.SurveyDS;
import com.cvirn.mobileapp2.survey.datasources.TaskDS;
import com.cvirn.mobileapp2.survey.datasources.TypeSpecFieldsDS;
import com.cvirn.mobileapp2.survey.datasources.UserDS;
import com.cvirn.mobileapp2.survey.jsonparser.JsonParser;
import com.cvirn.mobileapp2.survey.model.Message;
import com.cvirn.mobileapp2.survey.model.POI;
import com.cvirn.mobileapp2.survey.model.Question;
import com.cvirn.mobileapp2.survey.model.Survey;
import com.cvirn.mobileapp2.survey.model.Task;
import com.cvirn.mobileapp2.survey.model.TypeSpecFields;
import com.cvirn.mobileapp2.survey.model.User;
import com.cvirn.mobileapp2.survey.requests.ApiRequest;
import com.cvirn.mobileapp2.survey.requests.ImageDownload;

import java.util.ArrayList;

public class FirstLoginActivity extends Activity {

    private static final String TAG ="FirstLoginActivity" ;
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
        //setTitle("First Login");
        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setTitle("");
        initilaizeUI();


    }

    private void initilaizeUI(){

        eLogin=(EditText)findViewById(R.id.editLogin);
        ePass=(EditText)findViewById(R.id.editPass);

        eServer=(EditText)findViewById(R.id.editServer);
        //eServer.setText(server);

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

            setServerurl("http://"+eServer.getEditableText().toString()+"/mobileapp/api/");
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
            u.setUrl(getServerurl());

            ApiRequest request=new ApiRequest();
            request.LoginCheck(u);
            String json=request.Login();


            boolean login_check;

            if (json.contains("User or password incorrect")){

                login_check=false;

            }
            else {
                login_check=true;
            }

            if (login_check) {

                try{
                JsonParser parser = new JsonParser(json);
                ArrayList<Message> m = parser.parseMessages();

                //messages
                if (!m.isEmpty()) {

                    deleteMessages();
                    for (int i = 0; i < m.size(); i++) {
                        savemessages(m.get(i));
                    }
                    check = true;
                }

                //tasks
                ArrayList<Task> t = parser.parseTasks();

                if (!t.isEmpty()) {

                    deleteTasks();
                    for (int i = 0; i < t.size(); i++) {
                        savetasks(t.get(i));
                    }
                    check = true;
                }
                //parse POI's
                ArrayList<POI> poi_list = parser.parsePOIs();
                if (!poi_list.isEmpty()) {

                    deletePois();
                    for (int i = 0; i < poi_list.size(); i++) {
                        savepois(poi_list.get(i));
                        ImageDownload id = new ImageDownload();
                        id.downloadImage(poi_list.get(i).getPic());
                    }
                    check = true;
                }
                //Save TypeSpecFields

                if (!parser.getTypespecholder().isEmpty()){

                    deletetypeSpecFields();

                    for (int j=0;j<parser.getTypespecholder().size();j++){


                        saveTypeSpecFields(parser.getTypespecholder().get(j));

                    }


                }

                //Surveys and questions
                parser.parseForm();
                deleteSurveys();
                deleteQuestions();

                if (!parser.getSholder().isEmpty()) {

                    saveSurveys(parser.getSholder().get(0));

                }
                try {
                    if (!parser.getQholder().isEmpty()) {

                        for (int i = 0; i < parser.getQholder().size(); i++) {

                            saveQuestion(parser.getQholder().get(i));
                        }

                    }
                } catch (Exception e) {
                    Log.d(TAG, "Parsing Question holder exception." + e.getLocalizedMessage());

                }


                //user data
                User uretrieved = parser.parseUser();

                if (uretrieved != null) {
                    u.setFname(uretrieved.getFname());
                    u.setLname(uretrieved.getLname());
                    u.setEmail(uretrieved.getEmail());
                    u.setOrg(uretrieved.getOrg());
                    u.setUrl(getServerurl());
                    check = true;
                    saveuser(u);
                } else {
                    check = false;
                }}
                catch (Exception e){
                    check=false;
                }
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
                finish();


            }
            else{

                Toast.makeText(FirstLoginActivity.this,"Login failed!",Toast.LENGTH_LONG).show();
                eLogin.setError("!");
                eServer.setError("!");
                ePass.setError("!");
            }
        }
    }

    private void deleteSurveys(){
        SurveyDS ds=new SurveyDS(this);
        ds.open();
        ds.deleteAll();
        ds.close();
    }
    private void deleteQuestions(){
        QuestionDS ds=new QuestionDS(this);
        ds.open();
        ds.deleteAll();
        ds.close();
    }

    private void saveSurveys(Survey s){
        SurveyDS ds=new SurveyDS(this);
        ds.open();
        ds.createSurvey(s);
        ds.close();

    }
    private void saveQuestion(Question q){
        QuestionDS ds=new QuestionDS(this);
        ds.open();
        ds.createQuestion(q);
        ds.close();
    }

    private void deletePois() {
        PoiDS ds=new PoiDS(this);
        ds.open();
        ds.deleteAll();
        ds.close();
    }
    private void savepois(POI p) {
        PoiDS ds=new PoiDS(this);
        ds.open();
        ds.createPoi(p);
        ds.close();
    }

    private void savetasks(Task task) {
        TaskDS ds=new TaskDS(this);
        ds.open();
        ds.createTask(task);
        ds.close();
    }

    private void deleteTasks() {
        TaskDS ds=new TaskDS(this);
        ds.open();
        ds.deleteAll();
        ds.close();
    }

    private void deleteMessages() {

        MessageDS ds=new MessageDS(this);
        ds.open();
        ds.deleteAll();
        ds.close();
    }

    private void deletetypeSpecFields(){

        TypeSpecFieldsDS ds=new TypeSpecFieldsDS(this);
        ds.open();
        ds.deleteAll();
        ds.close();

    }

    private void saveTypeSpecFields(TypeSpecFields t){

        TypeSpecFieldsDS ds=new TypeSpecFieldsDS(this);
        ds.open();
        ds.createTSFields(t);
        ds.close();

    }

    private void savemessages(Message m) {

        MessageDS ds=new MessageDS(this);
        ds.open();
        ds.createMessage(m);
        ds.close();
    }

    private void saveuser
            (User u){

        UserDS ds=new UserDS(this);
        ds.open();
        ds.createUser(u);
        ds.close();

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
