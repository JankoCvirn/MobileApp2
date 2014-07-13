package com.cvirn.mobileapp2.survey.poi;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.PoiDS;
import com.cvirn.mobileapp2.survey.datasources.TypeSpecFieldsDS;
import com.cvirn.mobileapp2.survey.model.POI;
import com.cvirn.mobileapp2.survey.model.TypeSpecFields;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;

public class PoiDetailsActivity extends FragmentActivity {

    private GoogleMap mMap;
    PoiDS poids;
    POI poi;
    Intent i;
    String poi_name_passed;
    TextView txtPoiName;
    TextView txtPoiCity;
    //TextView txtLatLng;
    TextView type;
    TextView status;
    //TextView segment;
    //TextView created;
    //TextView bkey;
    TextView sid;
    TextView street;
    TextView street2;
    TextView owner;
    ImageView bigImage;
    Button btnCall;
    ImageButton back;
    ImageView mainImage;
    Button btnSurvey;

    public String getPoi_name_passed() {
        return poi_name_passed;
    }

    public void setPoi_name_passed(String poi_name_passed) {
        this.poi_name_passed = poi_name_passed;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_details);
        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        i=getIntent();
        setPoi_name_passed(i.getStringExtra("poi_name"));

        getPoiData();

        setUpMapIfNeeded();
        initializeUI();
        setTitle("");
        InitTypeSpecLayout();

    }

    private void initializeUI() {
        txtPoiCity=(TextView)findViewById(R.id.textCity);
        txtPoiName=(TextView)findViewById(R.id.textShopName);
        //txtLatLng=(TextView)findViewById(R.id.textLatLng);
        status=(TextView)findViewById(R.id.textStatus);
        street2=(TextView)findViewById(R.id.textAddress2);
        sid=(TextView)findViewById(R.id.textID);
        street=(TextView)findViewById(R.id.textAddress);
        type=(TextView)findViewById(R.id.textType);
        //bkey=(TextView)findViewById(R.id.textBKey);
        //created=(TextView)findViewById(R.id.textCreatedBy);
        //segment=(TextView)findViewById(R.id.textSdegment);
        btnCall=(Button)findViewById(R.id.buttonCall);
        back=(ImageButton)findViewById(R.id.imageButton);
        mainImage=(ImageView)findViewById(R.id.imageViewLogo);
        bigImage=(ImageView)findViewById(R.id.bigPicture);
        Log.d("PIC in DB",poi.getPic());
        setImage(poi.getPic());
        txtPoiName.setText(poi.getName());
        txtPoiCity.setText(poi.getCity());
       // txtLatLng.setText(poi.getLat()+"/"+poi.getLng());
        status.setText(poi.getStatus());
        sid.setText(poi.getSid());
        street.setText(poi.getAddress());
        street2.setText(poi.getAddress());
        type.setText(poi.getType());
        //bkey.setText(poi.getBuis_key());
        //created.setText(poi.getCreated_by());
        //segment.setText(poi.getSegment());
        btnSurvey=(Button)findViewById(R.id.button);
        TextView textSurvey=(TextView)findViewById(R.id.textView3);
        textSurvey.setPaintFlags(textSurvey.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + poi.getPhone()));
                startActivity(callIntent);
            }
        });

        btnSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PoiDetailsActivity.this, PoiSurveysActivity.class);
                intent.putExtra("poi",poi.getName());
                intent.putExtra("poi_id",poi.getSid());
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





    }

    private void InitTypeSpecLayout(){

        LinearLayout layoutholder=(LinearLayout)findViewById(R.id.typespecfields);


        ArrayList<TypeSpecFields> data=getTypeSpecData();

        if (data!=null)
        {
            if(!data.isEmpty()){

                for (int i=0;i<data.size();i++){

                    LinearLayout LL = new LinearLayout(this);
                    LL.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams LLParams=new LinearLayout.LayoutParams(LinearLayout
                            .LayoutParams.MATCH_PARENT,LinearLayout
                            .LayoutParams.MATCH_PARENT);

                    LL.setOrientation(LinearLayout.HORIZONTAL);

                    //LL.setWeightSum(6f);
                    LLParams.setMargins(10,10,10,10);
                    LL.setLayoutParams(LLParams);

                    TextView label=new TextView(this);
                    label.setText(data.get(i).getName()+" :");
                    LL.addView(label);



                    TextView value=new TextView(this);
                    value.setText(data.get(i).getValue());
                    LL.addView(value);

                    layoutholder.addView(LL);

                }


            }


        }



    }

    private ArrayList<TypeSpecFields> getTypeSpecData(){

        TypeSpecFieldsDS ds=new TypeSpecFieldsDS(this);
        ds.open();
        ArrayList<TypeSpecFields> hol=ds.findAll(poi.getSid());

        return hol;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void getPoiData(){

        poids=new PoiDS(this);
        poids.open();
        poi=poids.findPOI(getPoi_name_passed());

    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {


        try{
        double lat=Double.parseDouble(poi.getLat());
        double lng=Double.parseDouble(poi.getLng());
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,
                lng)).title(poi.getName()));
        LatLng ll=new LatLng(lat,lng);
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll, 16);
        mMap.moveCamera(update);}
        catch (Exception e){

        }
    }

    private void setImage(String url){

        try {

            String[] aray;
            aray = url.split("\\/");
            Log.d("SPLIT", aray[0] + " |  " + aray[1]);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            File path = new File(Environment.getExternalStorageDirectory()
                    + "/msurvey/" + aray[1]);
            Bitmap myBitmap2 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                    + "/msurvey/" + aray[1], options);


        /*
        try {
            ExifInterface exif = new ExifInterface(path.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            else if (orientation == 0) {
                matrix.postRotate(90);
            }
            myBitmap2 = Bitmap.createBitmap(myBitmap2, 0, 0, myBitmap2.getWidth(),
                    myBitmap2.getHeight(), matrix, true); // rotating bitmap
            profile_image.setImageBitmap(myBitmap2);*/
            mainImage.setImageBitmap(myBitmap2);
            bigImage.setImageBitmap(myBitmap2);
        }
        catch (Exception e){


        }
    }
}
