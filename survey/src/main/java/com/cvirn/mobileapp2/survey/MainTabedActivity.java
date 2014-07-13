package com.cvirn.mobileapp2.survey;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cvirn.mobileapp2.survey.about.AboutActivity;
import com.cvirn.mobileapp2.survey.adapters.TabsAccountPageAdapter;
import com.cvirn.mobileapp2.survey.datasources.MessageDS;
import com.cvirn.mobileapp2.survey.datasources.TaskDS;
import com.cvirn.mobileapp2.survey.datasources.UserDS;
import com.cvirn.mobileapp2.survey.jsonparser.JsonParser;
import com.cvirn.mobileapp2.survey.login.LoginActivity;
import com.cvirn.mobileapp2.survey.model.Message;
import com.cvirn.mobileapp2.survey.model.Task;
import com.cvirn.mobileapp2.survey.model.User;
import com.cvirn.mobileapp2.survey.requests.ApiRequest;
import com.cvirn.mobileapp2.survey.settings.SettingsActivity;

import java.util.ArrayList;


public class MainTabedActivity extends FragmentActivity implements ActionBar.TabListener {




    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private String[] tabs;
    private TabsAccountPageAdapter mAdapter;

    User u;
    private int taskCount;
    private int msgCount;
    private ActionBar actionBar;

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_account_main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        mViewPager=(ViewPager)findViewById(R.id.pager);
         actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        mAdapter=new TabsAccountPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);






        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        getCounts();
        tabs=new String[]  { "POI"," Messages "+msgCount ,"Tasks "+taskCount,"Search"
        };
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        setTitle(getString(R.string.app_version));


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, LoginActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
                break;
            case R.id.action_exit:
                finish();
                break;
            case R.id.action_about:

                Intent intent2 = new Intent(MainTabedActivity.this, AboutActivity.class);

                startActivity(intent2);

                break;

            case R.id.action_settings:

                Intent intent = new Intent(MainTabedActivity.this, SettingsActivity.class);

                startActivity(intent);

                break;
            case R.id.action_refresh:

                if (checkNetworking()){
                new LoginRequest().execute("");}
                else{
                    showAlert(getString(R.string.networking),getString(R.string.no_net_connection));
                }
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    private void getUserData(){

        UserDS ds=new UserDS(this);
        ds.open();
        setU(ds.getUser());

    }


    class LoginRequest extends AsyncTask<String,Integer,Boolean> {

        ProgressDialog dialog = ProgressDialog.show(MainTabedActivity.this,
                getString(R.string.action_refresh), getString(R.string.please_wait), true);

        @Override
        protected Boolean doInBackground(String... params) {

            getUserData();

            boolean check=false;

            User u=new User();
            u.setUsername(getU().getUsername());
            u.setPassword(getU().getPassword());

            Log.d("Test","username:"+u.getUsername());

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
            ArrayList<Task> t=parser.parseTasks();

            if (!t.isEmpty()){

                deleteTasks();
                for (int i=0;i<t.size();i++){
                    savetasks(t.get(i));}
                check=true;
            }


            User uretrieved=parser.parseUser();

            if (uretrieved!=null) {
                u.setFname(uretrieved.getFname());
                u.setLname(uretrieved.getLname());
                u.setEmail(uretrieved.getEmail());
                u.setOrg(uretrieved.getOrg());
                u.setUrl(getU().getUrl());
                check=true;
                deleteUser();
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

                showAlert(getString(R.string.action_refresh),getString(R.string.refresh_ack));
                recreate();

               tabs = new String[]{"POI", " Messages "+msgCount, "Tasks "+taskCount, "Search"};
                for (String tab_name : tabs) {
                    actionBar.removeAllTabs();
                    actionBar.addTab(actionBar.newTab().setText(tab_name)
                            .setTabListener(MainTabedActivity.this));
                }


            }
            else
            {

                showAlert(getString(R.string.action_refresh),getString(R.string.refresh_nack));
            }
        }
    }

    private void deleteUser() {
        UserDS ds=new UserDS(this);
        ds.open();
        ds.deleteAll();
        ds.close();
    }

    private void savetasks(Task task) {
        TaskDS ds=new TaskDS(this);
        ds.open();
        ds.createTask(task);
        taskCount=ds.countTasks();
    }

    private void getCounts(){
        TaskDS ds=new TaskDS(this);
        ds.open();
        //ds.createTask(task);
        taskCount=ds.countTasks();
        ds.close();

        MessageDS ds2=new MessageDS(this);
        ds2.open();
        //ds.createMessage(m);
        msgCount=ds2.countMessages();
        ds2.close();


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

    private void savemessages(Message m) {

        MessageDS ds=new MessageDS(this);
        ds.open();
        ds.createMessage(m);
        msgCount=ds.countMessages();
    }

    private void saveuser
            (User u){

        UserDS ds=new UserDS(this);
        ds.open();
        ds.createUser(u);

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

    private void showAlert(String title,String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg)

                .setCancelable(true)


                .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }




}
