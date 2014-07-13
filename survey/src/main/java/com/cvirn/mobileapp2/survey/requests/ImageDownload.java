package com.cvirn.mobileapp2.survey.requests;

import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by janko on 12/4/13.
 */
public class ImageDownload {

    static final String BASE_URL="http://engineroom.realimpactanalytics.com/media/";

    public String downloadImage(String pic_url){

        String return_me="NACK";

        File path= new File(Environment.getExternalStorageDirectory()
                + "/msurvey");

        if (!path.exists()) {
            path.mkdirs();
            Log.d("dir","creating dir");
        } else {
            Log.d("dir","dir present");
        }


        try{
            int count;

            String attachurl=BASE_URL+pic_url;

            URL url = new URL(attachurl);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

            InputStream input = new BufferedInputStream(url.openStream());
            String baseName = FilenameUtils.getBaseName(attachurl);
            String extension = FilenameUtils.getExtension(attachurl);
            //String extension="png";
            String attach_dir= Environment.getExternalStorageDirectory()
                    + "/msurvey/"+baseName+"."+extension;
            //OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+map_file);
            Log.d("Image name",baseName+extension);
            OutputStream output = new FileOutputStream(attach_dir);
            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                //publishProgress(""+(int)((total*100)/lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();


            return_me="ACK";

        }
        catch(Exception e){
            Log.e("DL error",e.getLocalizedMessage());
        }

        finally {
            return return_me;
        }

    }

}
