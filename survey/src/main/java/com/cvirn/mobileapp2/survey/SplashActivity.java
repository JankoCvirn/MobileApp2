package com.cvirn.mobileapp2.survey;

import android.app.Activity;
import android.content.Intent;
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
        ImageView image=(ImageView)findViewById(R.id.imageView);
        Animation animSplash= AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        image.startAnimation(animSplash);

        //DataSource
        final UserDS uds=new UserDS(this);
        uds.open();



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




