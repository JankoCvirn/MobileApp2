package com.cvirn.mobileapp2.survey.survey;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.AnswersDS;
import com.cvirn.mobileapp2.survey.datasources.QuestionDS;
import com.cvirn.mobileapp2.survey.model.Question;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class QuestionPictureActivity extends Activity {

    private static final String TAG ="ImageTakeSurvey" ;
    private static final int TAKE_PICTURE =1 ;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ =0 ;
    private String contantsString;

    Button btnCamera;
    ImageButton back;
    TextView nrQuestion;
    TextView txtQuestionText;
    ImageView mImageView;
    Intent i;
    QuestionDS qds;
    String parent_id;
    String q_sid;
    String type;
    Button btnprev;
    Button btnnext;
    int counter;
    private String captured_image;
    private File file;
    private Uri outputFileUri;
    private String imageFilePath;

    private String image_encoded;
    private Question q;
    Uri fileUri = null;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getImage_encoded() {
        return image_encoded;
    }

    public void setImage_encoded(String image_encoded) {
        this.image_encoded = image_encoded;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQ_sid() {
        return q_sid;
    }

    public void setQ_sid(String q_sid) {
        this.q_sid = q_sid;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_picture);
        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        qds=new QuestionDS(this);
        qds.open();

        i=getIntent();

        setParent_id(i.getStringExtra("parent_id"));
        //setType(i.getStringExtra("type"));
        setQ_sid(i.getStringExtra("q_sid"));
        setCounter(i.getIntExtra("counter",0));

        q=qds.findQuestion(getCounter());

        txtQuestionText=(TextView)findViewById(R.id.txtQuestionTitle);
        txtQuestionText.setText(q.getTitle());
        nrQuestion=(TextView)findViewById(R.id.txtQuestionorder);
        nrQuestion.setText(q.getOrder()+".");

        mImageView=(ImageView)findViewById(R.id.imageView2);



        btnCamera=(Button)findViewById(R.id.buttonCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cameracall();

            }
        });

        back=(ImageButton)findViewById(R.id.imageButton);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnprev=(Button)findViewById(R.id.buttonPrev);
        btnnext=(Button)findViewById(R.id.buttonNext);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (captured_image!=null){
                saveAnswer(captured_image,"image");}
               /* Intent intent = new Intent(QuestionPictureActivity.this,
                        FinishSurveyActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                startActivity(intent);*/
                startNextSurvey();
                finish();
            }
        });

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (captured_image!=null){
                saveAnswer(captured_image,"image");}
                Intent intent = new Intent(QuestionPictureActivity.this,
                        QuestionScanActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                startActivity(intent);
                finish();*/
            }
        });

        //TODO disable in live
        //checkEmulator();
        Log.d("Passed POI id:",getParent_id());
    }


    private void checkEmulator(){

        boolean inEmulator = "generic".equals(Build.BRAND.toLowerCase());
        if (inEmulator){
            setImage_encoded("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");
            Toast.makeText(this,"Running in emulator",Toast.LENGTH_LONG).show();
            Log.d(TAG,getImage_encoded());
        }
        else {

        }
    }



    private boolean Cameracall(){

        Boolean camera_result=false;

        try{
            Intent my_pict = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //my_pict.putExtra("crop", "true");
            my_pict.putExtra("outputX", 400);
            my_pict.putExtra("outputY", 600);
            my_pict.putExtra("aspectX", 1);
            my_pict.putExtra("aspectY", 1);
            my_pict.putExtra("scale", true);
            //photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
            my_pict.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());


            File path = new File(Environment.getExternalStorageDirectory()
                    + "/survey/img");

            if (!path.exists()) {
                path.mkdirs();
                Log.d(TAG, "creating dir");
            } else {
                Log.d(TAG,"dir present");
            }

            captured_image = "poi_"+getParent_id()+"_image.jpg";
            File file=new File(path,captured_image);

            Log.d(TAG, file.getAbsolutePath());



            file = new File(path, captured_image);


            captured_image = file.getAbsolutePath();
            setImageFilePath(captured_image);
            outputFileUri = Uri.fromFile(file);
            my_pict.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            my_pict.putExtra("return_date", true);
            startActivityForResult(my_pict, TAKE_PICTURE);
            camera_result=true;
            Log.d(TAG,captured_image);
        }
        catch (Exception e){

            Toast.makeText(QuestionPictureActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        }

        return camera_result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {


            File path = new File(Environment.getExternalStorageDirectory()
                    + "/survey/img/poi_"+getParent_id()+"_image.jpg");
            //captured_image = "poi_"+getParent_id()+"_image.jpg";


            //path=new File(path,captured_image);


            long l=path.length();
            Log.d(TAG,"On result image size :"+l);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap myBitmap2 = BitmapFactory.decodeFile(path.getAbsolutePath(),options);
            saveToInternalSorage(myBitmap2,captured_image);

            mImageView.setImageBitmap(myBitmap2);



        }
    }

    private long saveAnswer(String answer,String type){

        AnswersDS ads=new AnswersDS(this);
        ads.open();
        q.setAnswer(answer);
        q.setParentid(getParent_id());
        long i=ads.createAnswer(q);

        ads.close();
        return i;



    }

    private String saveToInternalSorage(Bitmap bitmapImage,String image_name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,image_name);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    private void startNextSurvey(){

        Log.d("Start Next Survey","Qsid"+getQ_sid());
        Log.d("Start Next Survey","Counter"+counter);

        ArrayList<Question> holder=new ArrayList<Question>();

        counter=getCounter()+1;
        QuestionDS qds=new QuestionDS(this);
        qds.open();
        holder=qds.findAll(getQ_sid());
        qds.close();
        int holder_size=holder.size();

        if (counter>holder_size) {

            Intent intent = new Intent(QuestionPictureActivity.this,
                    FinishSurveyActivity.class);
            intent.putExtra("parent_id",getParent_id());
            intent.putExtra("q_sid",getQ_sid());
            startActivity(intent);

            finish();

        }
        else {






            String check=holder.get(counter-1).getType();
            //int counter=getCounter()+1;

            Log.d("MainSurvey","Passing parentid:"+getParent_id()+" and counter "+getCounter() + " " +
                    "and type: "+getType() );


            overridePendingTransition(0, 0);
        /*Intent intent = new Intent(MainSurveyActivity.this, QuestionVisibleActivity.class);
        intent.putExtra("parent_id",getPoi_object_id());
        intent.putExtra("q_sid",holder.get(2).getSid());
        startActivity(intent);*/


            if (check.equals("bool")){

                Intent intent = new Intent(QuestionPictureActivity.this, QuestionVisibleActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else if(check.equals("range")){

                Intent intent = new Intent(QuestionPictureActivity.this, QuestionLargeActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else if(check.equals("barcode")){

                Intent intent = new Intent(QuestionPictureActivity.this, QuestionScanActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else if(check.equals("image")){

                Intent intent = new Intent(QuestionPictureActivity.this, QuestionPictureActivity.class);
                intent.putExtra("parent_id",getParent_id());
                intent.putExtra("q_sid",getQ_sid());
                intent.putExtra("counter",counter);
                startActivity(intent);

            }
            else{

                finish();
            }
        }




    }
}
