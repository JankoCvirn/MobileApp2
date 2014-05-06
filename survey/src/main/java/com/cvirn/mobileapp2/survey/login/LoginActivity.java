package com.cvirn.mobileapp2.survey.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cvirn.mobileapp2.survey.MainTabedActivity;
import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.UserDS;
import com.cvirn.mobileapp2.survey.model.User;

public class LoginActivity extends Activity {

    EditText eLogin;
    EditText ePass;

    Button btnLogin;

    int login_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_counter=0;
        setTitle("Login");
        initilaizeUI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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


    private void initilaizeUI(){

        eLogin=(EditText)findViewById(R.id.editLogin);
        ePass=(EditText)findViewById(R.id.editPass);



        btnLogin=(Button)findViewById(R.id.button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_counter=login_counter+1;

                if(login_counter>=3){

                    Toast.makeText(LoginActivity.this,"Logon failed 3 times.Deleting user data.",
                            Toast.LENGTH_LONG).show();

                }

                else{
                if(checkLogin()){

                    Intent intent = new Intent(LoginActivity.this, MainTabedActivity.class);

                    startActivity(intent);


                }
                else {

                    eLogin.setError("error");
                    ePass.setError("error");

                }
                }

            }
        });



    }

    private boolean checkLogin(){

        boolean check=true;
        boolean login=false;
        if (eLogin.getEditableText().toString().length()==0){

            eLogin.setError("*");

            check=false;
        }
        if (ePass.getEditableText().toString().length()==0){

            ePass.setError("*");
            check=false;
        }

        if (check){

            User u=new User();
            u.setUsername(eLogin.getEditableText().toString());
            u.setPassword(ePass.getEditableText().toString());
            UserDS ds=new UserDS(this);
            ds.open();
            login=ds.checkUser(u);

        }


        return login;
    }

}
