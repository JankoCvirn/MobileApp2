package com.cvirn.mobileapp2.survey.poi;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.PoiDS;
import com.cvirn.mobileapp2.survey.datasources.UserDS;
import com.cvirn.mobileapp2.survey.model.POI;
import com.cvirn.mobileapp2.survey.model.User;
import com.cvirn.mobileapp2.survey.requests.ApiRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NewPoiDetailsActivity extends FragmentActivity {

    private static final int TAKE_PICTURE =1 ;
    Intent i;
    private double lat;
    private double lng;
    private GoogleMap mMap;
    PoiDS ds;

    private POI poi;

    EditText name;
    EditText bkey;
    EditText latlng;
    EditText type;
    EditText status;
    EditText address;
    EditText city;
    EditText phone;
    ImageView mImageView;
    private String TAG="New POI Activity";
    private String captured_image;
    private File file;
    private Uri outputFileUri;
    private String imageFilePath;

    private String image_encoded;
    private User user;

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getImage_encoded() {
        return image_encoded;
    }

    public void setImage_encoded(String image_encoded) {
        this.image_encoded = image_encoded;
    }

    public POI getPoi() {
        return poi;
    }

    public void setPoi(POI poi) {
        this.poi = poi;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poi_details);
        i=getIntent();

        setLat(i.getDoubleExtra("lat",0));
        setLng(i.getDoubleExtra("lng",0));


        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        setTitle("");
        setUpMapIfNeeded();


        initUI();

    }

    private void initUI() {

         name=(EditText)findViewById(R.id.etextShopName);
       bkey=(EditText)findViewById(R.id.etextBKey);
       latlng=(EditText)findViewById(R.id.etextLatLng);
         type=(EditText)findViewById(R.id.etextType);
        type.setText("POS");
        type.setEnabled(false);
        status=(EditText)findViewById(R.id.etextStatus);
        address=(EditText)findViewById(R.id.etextAddress);
         city=(EditText)findViewById(R.id.etextCity);
         phone=(EditText)findViewById(R.id.etextPhone);
        Button  btnPhoto=(Button)findViewById(R.id.buttonTakePic);
        Button btnSave=(Button)findViewById(R.id.buttonSave);
        mImageView=(ImageView)findViewById(R.id.imageViewLogo);

        latlng.setText(getLat()+","+getLng());

        latlng.setEnabled(false);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cameracall();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               poi=new POI();
               poi.setName(name.getEditableText().toString());
               poi.setLng(""+getLng());
               poi.setLat(""+getLat());
               poi.setType(type.getEditableText().toString());
               poi.setAddress(address.getEditableText().toString());
               poi.setCity(city.getEditableText().toString());
               poi.setPhone(phone.getEditableText().toString());
               poi.setPic(getImage_encoded());
               poi.setBuis_key(bkey.getEditableText().toString());
               poi.setStatus(status.getEditableText().toString());



               //savePOI();
               userData();
               new  UploadPOI().execute("");



            }
        });



    }
    private boolean Cameracall(){

        Boolean camera_result=false;

        try{
            Intent my_pict = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //my_pict.putExtra("crop", "true");
            my_pict.putExtra("outputX", 800);
            my_pict.putExtra("outputY", 1200);
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

            Log.d(TAG,path.getAbsolutePath());

            captured_image = "newpoiimage.jpg";

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

            Toast.makeText(NewPoiDetailsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        }

        return camera_result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {

            File path = new File(Environment.getExternalStorageDirectory()
                    + "/survey/img/newpoiimage.jpg");

            long l=path.length();
            Log.d(TAG,"On result image size :"+l);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap myBitmap2 = BitmapFactory.decodeFile(path.getAbsolutePath(),options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mImageView.setImageBitmap(myBitmap2);
            myBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            setImage_encoded(encodedImage);
            Log.d(TAG,getImage_encoded());
            //poi.setPic(getImage_encoded());


        }
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



        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,
                lng)));
        LatLng ll=new LatLng(lat,lng);
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll, 16);
        mMap.moveCamera(update);
    }

    private void savePOI(){



        ds=new PoiDS(this);
        ds.open();

        ds.createPoi(poi);
        ds.close();



    }

    class UploadPOI extends AsyncTask<String,String,String>{

        ProgressDialog dialog = ProgressDialog.show(NewPoiDetailsActivity.this,
                getString(R.string.finish_title), getString(R.string.please_wait), true);
        @Override
        protected String doInBackground(String... params) {
            String result="";

            ApiRequest api=new ApiRequest();

            api.setUser(user);
            result=api.setNewPoi(getPoi());

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.d(TAG,s);
        }
    }

    private void userData(){

        UserDS uds=new UserDS(this);
        uds.open();
        user=uds.getUser();

    }

}
