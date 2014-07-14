package com.cvirn.mobileapp2.survey.jsonparser;

import android.util.Log;

import com.cvirn.mobileapp2.survey.model.Message;
import com.cvirn.mobileapp2.survey.model.POI;
import com.cvirn.mobileapp2.survey.model.Question;
import com.cvirn.mobileapp2.survey.model.Survey;
import com.cvirn.mobileapp2.survey.model.Task;
import com.cvirn.mobileapp2.survey.model.TypeSpecFields;
import com.cvirn.mobileapp2.survey.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by janko on 5/6/14.
 */
public class JsonParser {

    private static final String TAG ="FLA" ;
    protected String toParse;

    ArrayList<Survey> sholder;
    ArrayList<Question> qholder;
    ArrayList<TypeSpecFields> typespecholder;

    public ArrayList<TypeSpecFields> getTypespecholder() {
        return typespecholder;
    }

    public void setTypespecholder(ArrayList<TypeSpecFields> typespecholder) {
        this.typespecholder = typespecholder;
    }

    public ArrayList<Survey> getSholder() {
        return sholder;
    }

    public void setSholder(ArrayList<Survey> sholder) {
        this.sholder = sholder;
    }

    public ArrayList<Question> getQholder() {
        return qholder;
    }

    public void setQholder(ArrayList<Question> qholder) {
        this.qholder = qholder;
    }

    public String getToParse() {
        return toParse;
    }

    public void setToParse(String toParse) {
        this.toParse = toParse;
    }

    public JsonParser(String json){
        this.toParse=json;

    }




    public ArrayList<Message> parseMessages(){

        Log.d(TAG,"Starting parse");
        ArrayList <Message> holder=new ArrayList<Message>();

        try {
            JSONArray localJSONArray1 = new JSONObject(toParse).getJSONArray("Message");
            String test=localJSONArray1.getString(0);

            for (int i=0;i<localJSONArray1.length();i++){

                JSONObject oneObject = localJSONArray1.getJSONObject(i);
                String body=oneObject.getString("text");
                String sid=oneObject.getString("ID");
                String from=oneObject.getString("sender");
                String status=oneObject.getString("status");
                String title=oneObject.getString("title");

                Message m=new Message();
                m.setBody(body);
                m.setMid(sid);
                m.setStatus(status);
                m.setTitle(title);
                m.setFrom(from);

                Log.d(TAG,body);
                Log.d(TAG,title);
                Log.d(TAG,sid);
                Log.d(TAG,status);


                holder.add(m);


            }

            Log.d(TAG,test);



        }
        catch (Exception e){

            Log.e(TAG,"json parser exec. "+e.getLocalizedMessage());

        }

        return holder;
    }

    public ArrayList<Task> parseTasks(){

        ArrayList<Task> holder=new ArrayList<Task>();
        Log.e(TAG,"starting to parse tasks ");

        try {
            JSONArray localJSONArray1 = new JSONObject(toParse).getJSONArray("task");
            //String test = localJSONArray1.getString(0);
            Log.e(TAG,"tasks: " +localJSONArray1.length());
            for (int i = 0; i < localJSONArray1.length(); i++) {

                JSONObject oneObject = localJSONArray1.getJSONObject(i);
                String status = oneObject.getString("Status");

                //String form = oneObject.getString("Form");

                //****///
                String id = oneObject.getString("ID");

                 JSONObject formObject=oneObject.getJSONObject("Form");
                 String form_name=formObject.getString("Name");
                 String form_id=formObject.getString("ID");
                 String form_desc=formObject.getString("Description");


                //****///

                String duedate = oneObject.getString("DueDate");

                /*"POI": {
                "BusinessKey": "3225282325",
                        "Status": "",
                        "Name": "Vue sur Flagey",
                        "Type": "POS",
                        "Segment": "3",
                        "ID": "1"
            }*/

                JSONObject poidetails=oneObject.getJSONObject("POI");


                String poi = poidetails.getString("ID");

                String poi_name=poidetails.getString("Name");



                String creationdate = oneObject.getString("CreationDate");


                Task t=new Task();
                t.setStatus(status);
                t.setForm(form_name);
                t.setDue_date(duedate);
                t.setPoi(poi);
                t.setSid(id);
                t.setForm_id(form_id);
                t.setPoi_name(poi_name);

                Log.d(TAG, status);
                Log.d(TAG, form_name);
                Log.d(TAG, duedate);
                Log.d(TAG, poi);


                holder.add(t);


            }
        }
        catch (Exception e){

            Log.e(TAG,"json parser exec. "+e.getLocalizedMessage());
        }



        return holder;
    }

    public void parseForm(){

        /*"form": [
        {
            "Description": "Does this POS have all the marketing material it needs?",
            "enabled": true,
            "ID": "1",
            "Questions": [
                {
                    "Title": "Is this shop visible from the street?",
                    "Param": "",
                    "SequenceOrder": "1",
                    "Type": "bool",
                    "ID": "1",
                    "skippable": false
                },
                {
                    "Title": "How large is the shop in meter?",
                    "Param": "1,30",
                    "SequenceOrder": "2",
                    "Type": "range",
                    "ID": "2",
                    "skippable": true
                },
                {
                    "Title": "Scan a barcode from an article in the shop?",
                    "Param": "",
                    "SequenceOrder": "3",
                    "Type": "barcode",
                    "ID": "3",
                    "skippable": false
                },
                {
                    "Title": "Picture of the shop?",
                    "Param": "",
                    "SequenceOrder": "4",
                    "Type": "image",
                    "ID": "4",
                    "skippable": true
                }
            ],
            "Name": "Marketing survey"
        }
    ],*/


        sholder=new ArrayList<Survey>();

        try {
            JSONArray localJSONArray1 = new JSONObject(toParse).getJSONArray("form");

            JSONObject oneObject = localJSONArray1.getJSONObject(0);

            String enabled="";
            String description="";
            String id="";
            String name="";

            enabled=oneObject.getString("enabled");
            description=oneObject.getString("Description");
            id=oneObject.getString("ID");
            name=oneObject.getString("Name");

            Log.d(TAG,"Form:"+name+" desc:"+description+" enabled:"+enabled+id);

            JSONArray localQuestionsArray=oneObject.getJSONArray("Questions");
            Log.d("TAG","FOund Questions"+localQuestionsArray.length());

            Survey s=new Survey();
            s.setSid(id);
            s.setEnabled(enabled);
            s.setDesc(description);
            s.setName(name);

            sholder.add(s);


            qholder=new ArrayList<Question>();
            for (int i=0;i<localQuestionsArray.length();i++){

                JSONObject oneObjectsub = localQuestionsArray.getJSONObject(i);
                Question q=new Question();
                q.setTitle(oneObjectsub.getString("Title"));
                q.setParam(oneObjectsub.getString("Param"));
                q.setOrder(oneObjectsub.getString("SequenceOrder"));
                q.setType(oneObjectsub.getString("Type"));
                q.setSid(oneObjectsub.getString("ID"));
                q.setSkip(oneObjectsub.getString("skippable"));
                q.setParentid(id);

                qholder.add(q);
            }

            Log.d(TAG,"QHolder size"+qholder.size());


        }
        catch (Exception e){

            Log.d(TAG,"Parsing form execp."+e.getLocalizedMessage());

        }

    }


    public ArrayList<POI> parsePOIs(){

        ArrayList<POI> holder=new ArrayList<POI>();
        typespecholder=new ArrayList<TypeSpecFields>();
        try{
        JSONArray localJSONArray1 = new JSONObject(toParse).getJSONArray("POI");
        //String test = localJSONArray1.getString(0);
        Log.e(TAG,"number of poi's: " +localJSONArray1.length());
        for (int i = 0; i < localJSONArray1.length(); i++) {

            JSONObject oneObject = localJSONArray1.getJSONObject(i);
            String name = oneObject.getString("Name");
            String lat = oneObject.getString("Latitude");
            String lng = oneObject.getString("Longitude");
            String bkey= oneObject.getString("BusinessKey");
            String type=oneObject.getString("Type");
            String sid=oneObject.getString("ID");
            String status=oneObject.getString("Status");
            String created_by=oneObject.getString("CreatedBy");
            String mod_t=oneObject.getString("ModificationDate");
            String c_t=oneObject.getString("CreationDate");
            String seg=oneObject.getString("Segment");

            POI p=new POI();
            p.setName(name);
            p.setLat(lat);
            p.setLng(lng);
            p.setBuis_key(bkey);
            p.setType(type);
            p.setSid(sid);
            p.setStatus(status);
            p.setCreated_by(created_by);
            p.setMod_time(mod_t);
            p.setCreate_time(c_t);
            p.setSegment(seg);


            //parse array


            JSONArray typeSpecArray = oneObject.getJSONArray("TypeSpecificFields");



            for (int y=0;y<typeSpecArray.length();y++){

                TypeSpecFields t=new TypeSpecFields();
                JSONObject oneObjectAddress = typeSpecArray.getJSONObject(y);
                String seqorder=oneObjectAddress.getString("SequenceOrder");
                String seqname=oneObjectAddress.getString("Name");
                String value=oneObjectAddress.getString("Value");
                String sidseq=oneObjectAddress.getString("ID");
                String seqtype=oneObjectAddress.getString("Type");

                t.setParentid(sid);
                t.setValue(value);
                t.setName(seqname);
                t.setSid(sidseq);
                t.setSeq_order(seqorder);
                t.setType(seqtype);

                typespecholder.add(t);

            }

            String address="";
            String phone="";
            String city="";
            String pic="";
            try {
                JSONObject oneObjectAddress = typeSpecArray.getJSONObject(0);
                address = oneObjectAddress.getString("Value");

            }
                catch (Exception e){


                }
            try {
                JSONObject oneObjectPhone = typeSpecArray.getJSONObject(1);
                phone = oneObjectPhone.getString("Value");
            }
            catch (Exception e){


            }

            try {
                JSONObject oneObjectCity = typeSpecArray.getJSONObject(2);
                city = oneObjectCity.getString("Value");
            }
            catch (Exception e){


            }

            try {
                JSONObject oneObjectPic = typeSpecArray.getJSONObject(3);
                pic = oneObjectPic.getString("Value");
            }
            catch (Exception e){


            }









            p.setCity(city);
            p.setPhone(phone);
            p.setAddress(address);
            p.setPic(pic);




            Log.d(TAG, name);
            Log.d(TAG, lat);
            Log.d(TAG, lng);



            holder.add(p);


        }
    }
    catch (Exception e){

        Log.e(TAG,"json parser exec. POI object "+e.getLocalizedMessage());
    }





    return holder;
    }

    public ArrayList<POI> parsePOISearch(){

        ArrayList<POI> holder=new ArrayList<POI>();
        try{
            JSONArray localJSONArray1 = new JSONObject(toParse).getJSONArray("ClosestPOITop10");
            //String test = localJSONArray1.getString(0);
            Log.e(TAG,"number of poi's: " +localJSONArray1.length());
            for (int i = 0; i < localJSONArray1.length(); i++) {

                JSONObject oneObject = localJSONArray1.getJSONObject(i);
                String name = oneObject.getString("Name");
                String lat = oneObject.getString("Latitude");
                String lng = oneObject.getString("Longitude");
                String bkey= oneObject.getString("BusinessKey");
                String type=oneObject.getString("Type");
                String sid=oneObject.getString("ID");
                String status=oneObject.getString("Status");
                String created_by=oneObject.getString("CreatedBy");
                String mod_t=oneObject.getString("ModificationDate");
                String c_t=oneObject.getString("CreationDate");
                String seg=oneObject.getString("Segment");

                POI p=new POI();
                p.setName(name);
                p.setLat(lat);
                p.setLng(lng);
                p.setBuis_key(bkey);
                p.setType(type);
                p.setSid(sid);
                p.setStatus(status);
                p.setCreated_by(created_by);
                p.setMod_time(mod_t);
                p.setCreate_time(c_t);
                p.setSegment(seg);


                //parse array


                JSONArray typeSpecArray = oneObject.getJSONArray("TypeSpecificFields");

                String address="";
                String phone="";
                String city="";
                String pic="";
                try {
                    JSONObject oneObjectAddress = typeSpecArray.getJSONObject(0);
                    address = oneObjectAddress.getString("Value");
                }
                catch (Exception e){


                }
                try {
                    JSONObject oneObjectPhone = typeSpecArray.getJSONObject(1);
                    phone = oneObjectPhone.getString("Value");
                }
                catch (Exception e){


                }

                try {
                    JSONObject oneObjectCity = typeSpecArray.getJSONObject(2);
                    city = oneObjectCity.getString("Value");
                }
                catch (Exception e){


                }

                try {
                    JSONObject oneObjectPic = typeSpecArray.getJSONObject(3);
                    pic = oneObjectPic.getString("Value");
                }
                catch (Exception e){


                }









                p.setCity(city);
                p.setPhone(phone);
                p.setAddress(address);
                p.setPic(pic);




                Log.d(TAG, name);
                Log.d(TAG, lat);
                Log.d(TAG, lng);



                holder.add(p);


            }
        }
        catch (Exception e){

            Log.e(TAG,"json parser exec. POI object "+e.getLocalizedMessage());
        }





        return holder;
    }

    public boolean uploadResponse(String json){


        boolean check=false;

        /*06-11 13:25:37.733: D/JSON(1898): {"message": "Problem while creating the survey. Incorrect padding", "return": "error"}
*/
        try {
            JSONObject object = new JSONObject(json);
            String message=object.getString("message");
            Log.d(TAG,"JSON:"+message);
            String result=object.getString("return");
            Log.d(TAG,"JSON:"+result);

            if (!result.equalsIgnoreCase("error")){
                check=true;
            }

        }
        catch (Exception e){
            Log.d(TAG,"Json exception "+e.getMessage());
        }
        return check;

    }

    public User parseUser(){

        Log.d(TAG,"Starting parse user");


        User u=new User();
        try {

            String org=new JSONObject(toParse).getString("CustomerName");
            Log.d(TAG,"Org:"+org);

            JSONObject jsonobject = new JSONObject(toParse).getJSONObject("UserDetails");
            //JSONArray localJSONArray1=jsonobject.getJSONArray("UserDetails");


            Log.i(TAG,
                    "Number of entries " + jsonobject.getString("email"));

                String lastname=jsonobject.getString("lastname");
                String email=jsonobject.getString("email");
                String firstname=jsonobject.getString("firstname");

                u.setFname(firstname);
                u.setEmail(email);
                u.setLname(lastname);
                u.setOrg(org);












            //Log.d(TAG,test);



        }
        catch (Exception e){

            Log.e(TAG,"json parser exec. "+e.getLocalizedMessage());

        }

        return u;
    }

}
