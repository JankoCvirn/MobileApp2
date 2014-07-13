package com.cvirn.mobileapp2.survey;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cvirn.mobileapp2.survey.datasources.UserDS;
import com.cvirn.mobileapp2.survey.login.FirstLoginActivity;
import com.cvirn.mobileapp2.survey.login.LoginActivity;


public class SplashActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final UserDS uds=new UserDS(this);
        uds.open();
        ImageView image=(ImageView)findViewById(R.id.imageView);
        Animation animSplash= AnimationUtils.loadAnimation(this, R.anim.splash_anim);

        setTitle("");
        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.hide();
        //DataSource
        image.startAnimation(animSplash);



        animSplash.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {


                if (uds.checkUserPresent()){

                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    SplashActivity.this.finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this,FirstLoginActivity.class));
                    SplashActivity.this.finish();
                }


            }


        });


    }
}




